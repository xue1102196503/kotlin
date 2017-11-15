/*
 * Copyright 2010-2017 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.resolve.calls.tower

import org.jetbrains.kotlin.builtins.isFunctionType
import org.jetbrains.kotlin.descriptors.CallableDescriptor
import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.descriptors.FunctionDescriptor
import org.jetbrains.kotlin.descriptors.annotations.AnnotationDescriptor
import org.jetbrains.kotlin.descriptors.synthetic.SyntheticMemberDescriptor
import org.jetbrains.kotlin.incremental.components.NoLookupLocation
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.resolve.annotations.argumentValue
import org.jetbrains.kotlin.resolve.calls.inference.returnTypeOrNothing
import org.jetbrains.kotlin.resolve.calls.model.ExpressionValueArgument
import org.jetbrains.kotlin.resolve.calls.model.MutableResolvedCall
import org.jetbrains.kotlin.resolve.calls.model.ResolvedCallImpl
import org.jetbrains.kotlin.resolve.calls.tasks.ExplicitReceiverKind
import org.jetbrains.kotlin.resolve.calls.tower.NewResolutionOldInference.MyCandidate
import org.jetbrains.kotlin.resolve.calls.util.CallMaker
import org.jetbrains.kotlin.resolve.descriptorUtil.module
import org.jetbrains.kotlin.resolve.scopes.MemberScope
import org.jetbrains.kotlin.resolve.scopes.SyntheticScopes
import org.jetbrains.kotlin.resolve.scopes.getDescriptorsFiltered
import org.jetbrains.kotlin.resolve.scopes.receivers.ExpressionReceiver
import org.jetbrains.kotlin.resolve.scopes.receivers.ImplicitReceiver
import org.jetbrains.kotlin.resolve.scopes.receivers.ReceiverValue
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.types.SimpleType
import org.jetbrains.kotlin.types.checker.KotlinTypeChecker
import java.lang.IllegalStateException

internal object ResolvedCallCompatReplacer {
    private val annotationFqName = FqName("kotlin.annotations.jvm.internal.Compat")

    fun replace(
            syntheticScopes: SyntheticScopes,
            candidates: Collection<MyCandidate>
    ): Collection<MyCandidate> = candidates.map { replaceCandidate(it, syntheticScopes) }

    private fun replaceCandidate(
            candidate: MyCandidate,
            syntheticScopes: SyntheticScopes
    ): MyCandidate {
        val receiver = candidate.resolvedCall.dispatchReceiver ?: return candidate
        val resolvedCall = candidate.resolvedCall
        val callDescriptor = resolvedCall.candidateDescriptor ?: return candidate
        val receiverDescriptor = receiver.type.constructor.declarationDescriptor as? ClassDescriptor ?: return candidate
        val compatClasses = findCompatClasses(receiverDescriptor)

        // Find appropriate compat class/method and replace the call
        val compatMethod = compatClasses.mapNotNull { (compatPrototype, compat) ->
            findCompatMethod(compat, callDescriptor, syntheticScopes, compatPrototype.defaultType, receiver)
        }.singleOrNull() ?: return candidate

        return MyCandidate(candidate.diagnostics, createResolvedCall(callDescriptor, compatMethod, resolvedCall, receiver))
    }

    private fun findCompatMethod(
            compat: ClassDescriptor,
            callDescriptor: CallableDescriptor,
            syntheticScopes: SyntheticScopes,
            compatPrototypeType: SimpleType,
            receiver: ReceiverValue
    ): CallableDescriptor? {
        var compatMethod: CallableDescriptor? = null
        // Find method in compat class
        for (compatMethodDescriptor in compat.staticScope.getDescriptorsFiltered { it == callDescriptor.name }) {
            if (compatMethodDescriptor !is FunctionDescriptor) continue
            val equalMethods = functionSignaturesEqual(
                    callDescriptor,
                    compatMethodDescriptor,
                    compat.staticScope,
                    syntheticScopes,
                    compatPrototypeType,
                    receiver.type
            )
            if (!equalMethods) continue
            compatMethod = syntheticScopes.scopes.flatMap { it.getSyntheticStaticFunctions(compat.staticScope) }.firstOrNull {
                functionSignaturesEqual(callDescriptor, it, compat.staticScope, syntheticScopes, compatPrototypeType)
            } ?: compatMethodDescriptor
        }
        return compatMethod
    }

    private fun functionSignaturesEqual(
            call: CallableDescriptor,
            candidate: CallableDescriptor,
            scope: MemberScope,
            syntheticScopes: SyntheticScopes,
            compatPrototypeType: KotlinType? = null,
            receiverType: KotlinType? = null
    ): Boolean {
        if (call.name != candidate.name) return false
        if (!KotlinTypeChecker.DEFAULT.equalTypes(call.returnTypeOrNothing, candidate.returnTypeOrNothing)) return false
        if (call.typeParameters.size != candidate.typeParameters.size) return false
        if (call.typeParameters.indices.any { call.typeParameters[it] != candidate.typeParameters[it] }) return false
        if (call.valueParameters.size + (if (compatPrototypeType != null) 1 else 0) != candidate.valueParameters.size) return false
        if (compatPrototypeType != null && !KotlinTypeChecker.DEFAULT.equalTypes(compatPrototypeType, candidate.valueParameters[0].type)) return false

        for (prototypeParamIndex in call.valueParameters.indices) {
            val compatParamIndex = if (compatPrototypeType != null) prototypeParamIndex + 1 else prototypeParamIndex
            if (KotlinTypeChecker.DEFAULT.equalTypes(candidate.valueParameters[compatParamIndex].type, call.valueParameters[prototypeParamIndex].type)) continue
            if (call.valueParameters[prototypeParamIndex].type.isFunctionType) {
                val synthetics = syntheticScopes.scopes.flatMap {
                    if (receiverType == null) it.getSyntheticStaticFunctions(scope)
                    else it.getSyntheticMemberFunctions(listOf(receiverType))
                }

                val originFun = synthetics.singleOrNull {
                    functionSignaturesEqual(call, it, scope, syntheticScopes)
                } as? SyntheticMemberDescriptor<*> ?: return false

                val realParam = (originFun.baseDescriptorForSynthetic as? FunctionDescriptor)?.valueParameters?.get(prototypeParamIndex) ?: return false
                if (!KotlinTypeChecker.DEFAULT.equalTypes(candidate.valueParameters[compatParamIndex].type, realParam.type)) return false
            }
            else return false
        }
        return true
    }

    // Find all compat classes of the type and its supertypes including interfaces
    // TODO: Replace IllegalStateException with error reporting
    private fun findCompatClasses(origin: ClassDescriptor): Map<ClassDescriptor, ClassDescriptor> {
        val res = hashMapOf<ClassDescriptor, ClassDescriptor>()
        for ((classDescriptor, annotation) in findCompatAnnotations(origin)) {
            val annotationValue = annotation.argumentValue("value") ?: throw IllegalStateException("Compat annotation must have value")
            val valueString = annotationValue as? String ?: throw IllegalStateException("$annotationValue must be string")

            var packageName = ""
            var className = valueString
            if (valueString.contains('.')) {
                packageName = valueString.substring(0, valueString.lastIndexOf('.'))
                className = valueString.substring(valueString.lastIndexOf('.') + 1)
            }

            res[classDescriptor] = classDescriptor.module.getPackage(FqName(packageName))
                             .memberScope
                             .getContributedClassifier(
                                     Name.identifier(className),
                                     NoLookupLocation.WHEN_FIND_BY_FQNAME
                             ) as? ClassDescriptor ?: throw IllegalStateException("Compat must be a class")
        }
        return res
    }

    // Find all compat annotations on the type and its supertypes
    private fun findCompatAnnotations(origin: ClassDescriptor): HashMap<ClassDescriptor, AnnotationDescriptor> {
        val type = origin.defaultType
        val allTypes = type.constructor.supertypes + type
        val res = hashMapOf<ClassDescriptor, AnnotationDescriptor>()
        for (t in allTypes) {
            val annotation = findCompatAnnotationOnType(t)
            val cd = t.constructor.declarationDescriptor as? ClassDescriptor ?: throw IllegalStateException("$t must be class or interface")
            if (annotation != null) res[cd] = annotation
        }
        return res
    }

    private fun findCompatAnnotationOnType(t: KotlinType): AnnotationDescriptor? =
            t.constructor.declarationDescriptor?.annotations?.firstOrNull { it.fqName == annotationFqName }

    private fun createResolvedCall(
            callDescriptor: CallableDescriptor,
            compatMethod: CallableDescriptor,
            resolvedCall: MutableResolvedCall<*>,
            receiver: ReceiverValue
    ): ResolvedCallImpl<CallableDescriptor> {
        val (compatPrototypeValue, compatCall) = createCall(resolvedCall, callDescriptor, receiver)
        val compatResolverCall = ResolvedCallImpl(
                compatCall,
                compatMethod,
                null,
                null,
                ExplicitReceiverKind.NO_EXPLICIT_RECEIVER,
                resolvedCall.knownTypeParametersSubstitutor,
                resolvedCall.trace,
                resolvedCall.tracingStrategy,
                resolvedCall.dataFlowInfoForArguments
        )

        compatResolverCall.recordValueArgument(compatMethod.valueParameters.first(), ExpressionValueArgument(compatPrototypeValue))

        resolvedCall.valueArguments.forEach {
            compatResolverCall.recordValueArgument(compatMethod.valueParameters[it.key.index + 1], it.value)
        }

        compatResolverCall.setStatusToSuccess()
        return compatResolverCall
    }

    private fun createCall(
            resolvedCall: MutableResolvedCall<*>,
            callDescriptor: CallableDescriptor,
            receiver: ReceiverValue
    ): Pair<ValueArgument, Call> {
        // Replace the call
        val psiFactory = KtPsiFactory(resolvedCall.call.callElement, markGenerated = false)
        val calleeExpression = psiFactory.createSimpleName(callDescriptor.name.asString())

        val compatPrototypeValue  =
                if (receiver is ExpressionReceiver) CallMaker.makeValueArgument(receiver.expression)
                else {
                    val implicitThisDescriptor = (receiver as? ImplicitReceiver)?.declarationDescriptor ?: error("Implicit this might be either class or closure")
                    when (implicitThisDescriptor) {
                        is CallableDescriptor -> ImplicitThisValueArgument(resolvedCall.call.callElement, implicitThisDescriptor)
                        is ClassDescriptor -> ImplicitThisValueArgument(resolvedCall.call.callElement, implicitThisDescriptor)
                        else -> error("Implicit this might be either class or closure")
                    }
                }

        val compatCall = CallMaker.makeCall(
                resolvedCall.call.callElement,
                null,
                resolvedCall.call.callOperationNode,
                calleeExpression,
                listOf(compatPrototypeValue) + resolvedCall.call.valueArguments
        )
        return Pair(compatPrototypeValue, compatCall)
    }
}