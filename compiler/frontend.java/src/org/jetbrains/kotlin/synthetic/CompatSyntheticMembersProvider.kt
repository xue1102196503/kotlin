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
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.resolve.DescriptorEquivalenceForOverrides
import org.jetbrains.kotlin.resolve.annotations.argumentValue
import org.jetbrains.kotlin.resolve.descriptorUtil.module
import org.jetbrains.kotlin.resolve.scopes.*
import org.jetbrains.kotlin.storage.StorageManager
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.types.checker.KotlinTypeChecker
import kotlin.properties.Delegates

interface CompatSyntheticFunctionDescriptor: FunctionDescriptor, SyntheticMemberDescriptor<FunctionDescriptor>

private class CompatSyntheticMemberScope(
        storageManager: StorageManager,
        private val type: KotlinType,
        override val wrappedScope: ResolutionScope
) : SyntheticResolutionScope() {
    private val ownerClass = type.constructor.declarationDescriptor as ClassDescriptor
    private val annotationFqName = FqName("kotlin.annotations.jvm.internal.Compat")
    private val functions = storageManager.createMemoizedFunction<Name, Collection<FunctionDescriptor>> {
        doGetFunctions(it)
    }
    private val compat = storageManager.createNullableLazyValue {
        doGetCompat()
    }

    private fun doGetCompat(): ClassDescriptor? {
        val annotation = ownerClass.annotations.firstOrNull { it.fqName == annotationFqName } ?: return null
        val annotationValue = annotation.argumentValue("value") ?: error("Compat annotation must have value")
        val valueString = annotationValue as? String ?: error("$annotationValue must be string")

        var packageName = ""
        var className = valueString
        if (valueString.contains('.')) {
            packageName = valueString.substring(0, valueString.lastIndexOf('.'))
            className = valueString.substring(valueString.lastIndexOf('.') + 1)
        }

        return ownerClass.module.getPackage(FqName(packageName))
                       .memberScope
                       .getContributedClassifier(
                               Name.identifier(className),
                               NoLookupLocation.FROM_SYNTHETIC_SCOPE
                       ) as? ClassDescriptor ?: error("Compat must be a class")
    }

    private fun doGetFunctions(name: Name): Collection<FunctionDescriptor> {
        val compatFunctions = compat()?.staticScope?.getContributedFunctions(name, NoLookupLocation.FROM_SYNTHETIC_SCOPE) ?: return emptyList()
        val visibleCompatFunctions = compatFunctions.filter { it.visibility == Visibilities.PUBLIC }
        val visibleCompatFunctionsWhichTakeOriginAsFistParam = visibleCompatFunctions.filter {
            it.valueParameters.size > 0 && KotlinTypeChecker.DEFAULT.equalTypes(type, it.valueParameters.first().type)
        }
        return visibleCompatFunctionsWhichTakeOriginAsFistParam.map { MyFunctionDescriptor.create(ownerClass, it) }
    }

    override fun getContributedFunctions(name: Name, location: LookupLocation): Collection<FunctionDescriptor> {
        return shadowOriginalFunctions(name, location) {
            functions(name)
        }
    }

    // Looks like origin function, acts like compat function
    private class MyFunctionDescriptor(
            containingDeclaration: DeclarationDescriptor,
            original: SimpleFunctionDescriptor?,
            annotations: Annotations,
            name: Name,
            kind: CallableMemberDescriptor.Kind,
            source: SourceElement
    ) :
            SimpleFunctionDescriptorImpl(containingDeclaration, original, annotations, name, kind, source),
            CompatSyntheticFunctionDescriptor
    {
        override var baseDescriptorForSynthetic: FunctionDescriptor by Delegates.notNull()
            private set

        companion object {
            fun create(ownerClass: ClassDescriptor, compat: FunctionDescriptor): MyFunctionDescriptor {
                val result = MyFunctionDescriptor(
                        ownerClass,
                        null,
                        compat.annotations,
                        compat.name,
                        CallableMemberDescriptor.Kind.SYNTHESIZED,
                        compat.original.source
                )
                result.baseDescriptorForSynthetic = compat
                result.initialize(
                        null,
                        ownerClass.thisAsReceiverParameter,
                        compat.typeParameters,
                        compat.valueParameters.drop(1),
                        compat.returnType,
                        compat.modality,
                        compat.visibility
                )
                return result
            }
        }
    }
}

class CompatSyntheticMembersProvider(storageManager: StorageManager) : SyntheticScopeProvider {
    private val makeSynthetic = storageManager.createMemoizedFunction<Pair<KotlinType, ResolutionScope>, ResolutionScope> { (type, scope) ->
        CompatSyntheticMemberScope(storageManager, type, scope)
    }

    override fun provideSyntheticScope(scope: ResolutionScope, metadata: SyntheticScopesMetadata): ResolutionScope {
        val type = metadata.type
        if (!metadata.needMemberFunctions || type == null || type.constructor.declarationDescriptor !is ClassDescriptor) return scope
        return makeSynthetic(Pair(type, scope))
    }
}
