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

package org.jetbrains.kotlin.synthetic

import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.descriptors.annotations.Annotations
import org.jetbrains.kotlin.descriptors.impl.SimpleFunctionDescriptorImpl
import org.jetbrains.kotlin.descriptors.synthetic.SyntheticMemberDescriptor
import org.jetbrains.kotlin.incremental.components.LookupLocation
import org.jetbrains.kotlin.incremental.components.NoLookupLocation
import org.jetbrains.kotlin.load.java.components.SamConversionResolver
import org.jetbrains.kotlin.load.java.sam.SingleAbstractMethodUtils
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.resolve.DeprecationResolver
import org.jetbrains.kotlin.resolve.calls.inference.wrapWithCapturingSubstitution
import org.jetbrains.kotlin.resolve.scopes.*
import org.jetbrains.kotlin.storage.StorageManager
import org.jetbrains.kotlin.types.*
import org.jetbrains.kotlin.types.checker.findCorrespondingSupertype
import kotlin.properties.Delegates

interface SamAdapterExtensionFunctionDescriptor : FunctionDescriptor, SyntheticMemberDescriptor<FunctionDescriptor> {
    override val baseDescriptorForSynthetic: FunctionDescriptor
}

private class SamAdapterFunctionsScope(
        storageManager: StorageManager,
        private val samResolver: SamConversionResolver,
        private val deprecationResolver: DeprecationResolver,
        private val type: KotlinType,
        override val wrappedScope: ResolutionScope
) : SyntheticResolutionScope() {
    private val functions = storageManager.createMemoizedFunction<Name, List<SimpleFunctionDescriptor>> {
        doGetFunctions(it)
    }
    private val descriptors = storageManager.createLazyValue {
        doGetDescriptors()
    }

    private fun doGetDescriptors() = super.getContributedDescriptors(DescriptorKindFilter.FUNCTIONS, MemberScope.ALL_NAME_FILTER)
            .filterIsInstance<FunctionDescriptor>()
            .flatMap { getContributedFunctions(it.name, NoLookupLocation.FROM_SYNTHETIC_SCOPE) }

    private fun doGetFunctions(name: Name) =
            super.getContributedFunctions(name, NoLookupLocation.FROM_SYNTHETIC_SCOPE).mapNotNull {
                wrapFunction(it.original)?.substituteForReceiverType(type) as? SimpleFunctionDescriptor
            }

    private fun wrapFunction(function: FunctionDescriptor): FunctionDescriptor? {
        if (!function.visibility.isVisibleOutside()) return null
        if (!function.hasJavaOriginInHierarchy()) return null //TODO: should we go into base at all?
        if (!SingleAbstractMethodUtils.isSamAdapterNecessary(function)) return null
        if (function.returnType == null) return null
        if (deprecationResolver.isHiddenInResolution(function)) return null
        return MyFunctionDescriptor.create(function, samResolver)
    }

    override fun getContributedFunctions(name: Name, location: LookupLocation): Collection<FunctionDescriptor> {
        return shadowOriginalFunctions(name, location) {
            functions(name)
        }
    }

    override fun getContributedDescriptors(kindFilter: DescriptorKindFilter, nameFilter: (Name) -> Boolean): Collection<DeclarationDescriptor> {
        return shadowOriginalDescriptors(kindFilter, nameFilter) { filter ->
            if (filter != DescriptorKindFilter.FUNCTIONS) emptyList()
            else descriptors()
        }
    }

    private class MyFunctionDescriptor(
            containingDeclaration: DeclarationDescriptor,
            original: SimpleFunctionDescriptor?,
            annotations: Annotations,
            name: Name,
            kind: CallableMemberDescriptor.Kind,
            source: SourceElement
    ) : SamAdapterExtensionFunctionDescriptor, SimpleFunctionDescriptorImpl(containingDeclaration, original, annotations, name, kind, source) {

        override var baseDescriptorForSynthetic: FunctionDescriptor by Delegates.notNull()
            private set

        private val fromSourceFunctionTypeParameters: Map<TypeParameterDescriptor, TypeParameterDescriptor> by lazy {
            baseDescriptorForSynthetic.typeParameters.zip(typeParameters).toMap()
        }

        companion object {
            fun create(sourceFunction: FunctionDescriptor, samResolver: SamConversionResolver): MyFunctionDescriptor {
                val descriptor = MyFunctionDescriptor(sourceFunction.containingDeclaration,
                                                      null,
                                                      sourceFunction.annotations,
                                                      sourceFunction.name,
                                                      CallableMemberDescriptor.Kind.SYNTHESIZED,
                                                      sourceFunction.original.source)
                descriptor.baseDescriptorForSynthetic = sourceFunction

                val sourceTypeParams = (sourceFunction.typeParameters).toMutableList()
                val ownerClass = sourceFunction.containingDeclaration as ClassDescriptor

                val typeParameters = ArrayList<TypeParameterDescriptor>(sourceTypeParams.size)
                val typeSubstitutor = DescriptorSubstitutor.substituteTypeParameters(sourceTypeParams, TypeSubstitution.EMPTY, descriptor, typeParameters)

                val returnType = typeSubstitutor.safeSubstitute(sourceFunction.returnType!!, Variance.INVARIANT)
                val valueParameters = SingleAbstractMethodUtils.createValueParametersForSamAdapter(
                        sourceFunction, descriptor, typeSubstitutor, samResolver)

                val visibility = syntheticVisibility(sourceFunction, isUsedForExtension = false)

                descriptor.initialize(null, ownerClass.thisAsReceiverParameter, typeParameters, valueParameters, returnType,
                                      Modality.FINAL, visibility)

                descriptor.isOperator = sourceFunction.isOperator
                descriptor.isInfix = sourceFunction.isInfix

                return descriptor
            }
        }

        override fun hasStableParameterNames() = baseDescriptorForSynthetic.hasStableParameterNames()
        override fun hasSynthesizedParameterNames() = baseDescriptorForSynthetic.hasSynthesizedParameterNames()

        override fun createSubstitutedCopy(
                newOwner: DeclarationDescriptor,
                original: FunctionDescriptor?,
                kind: CallableMemberDescriptor.Kind,
                newName: Name?,
                annotations: Annotations,
                source: SourceElement
        ): MyFunctionDescriptor {
            return MyFunctionDescriptor(
                    containingDeclaration, original as SimpleFunctionDescriptor?, annotations, newName ?: name, kind, source
            ).apply {
                baseDescriptorForSynthetic = this@MyFunctionDescriptor.baseDescriptorForSynthetic
            }
        }

        override fun newCopyBuilder(substitutor: TypeSubstitutor): CopyConfiguration =
                super.newCopyBuilder(substitutor).setOriginal(this.original)

        override fun doSubstitute(configuration: CopyConfiguration): FunctionDescriptor? {
            val descriptor = super.doSubstitute(configuration) as MyFunctionDescriptor? ?: return null
            val original = configuration.original
                           ?: throw UnsupportedOperationException("doSubstitute with no original should not be called for synthetic extension $this")

            original as MyFunctionDescriptor
            assert(original.original == original) { "original in doSubstitute should have no other original" }

            val sourceFunctionSubstitutor =
                    CompositionTypeSubstitution(configuration.substitution, fromSourceFunctionTypeParameters).buildSubstitutor()

            descriptor.baseDescriptorForSynthetic = original.baseDescriptorForSynthetic.substitute(sourceFunctionSubstitutor) ?: return null
            return descriptor
        }
    }

    private fun FunctionDescriptor.substituteForReceiverType(type: KotlinType): FunctionDescriptor? {
        val containingClass = containingDeclaration as? ClassDescriptor ?: return null
        val correspondingSupertype = findCorrespondingSupertype(type, containingClass.defaultType) ?: return null

        val result = substitute(
                TypeConstructorSubstitution
                        .create(correspondingSupertype)
                        .wrapWithCapturingSubstitution(needApproximation = true)
                        .buildSubstitutor()
        )
        return result
    }
}

class SamAdapterSyntheticMembersProvider(
        private val storageManager: StorageManager,
        private val samResolver: SamConversionResolver,
        private val deprecationResolver: DeprecationResolver
) : SyntheticScopeProvider {
    private val makeSynthetic = storageManager.createMemoizedFunction<Pair<ResolutionScope, KotlinType>, ResolutionScope> { (scope, type) ->
        SamAdapterFunctionsScope(storageManager, samResolver, deprecationResolver, type, scope)
    }

    override fun provideSyntheticScope(scope: ResolutionScope, metadata: SyntheticScopesMetadata): ResolutionScope {
        if (!metadata.needMemberFunctions) return scope
        val type = metadata.type ?: error("SAM members provider requires type")
        return makeSynthetic(Pair(scope, type))
    }
}