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

package org.jetbrains.kotlin.resolve.scopes

import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.incremental.components.LookupLocation
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.types.TypeConstructor
import org.jetbrains.kotlin.utils.addToStdlib.cast

data class SyntheticScopesMetadata(
        val type: KotlinType? = null,
        val needExtensionProperties: Boolean = false,
        val needMemberFunctions: Boolean = false,
        val needStaticFunctions: Boolean = false,
        val needConstructors: Boolean = false
)

interface SyntheticScopeProvider {
    fun provideSyntheticScope(scope: ResolutionScope, metadata: SyntheticScopesMetadata): ResolutionScope
}

interface SyntheticScopes {
    val scopeProviders: Collection<SyntheticScopeProvider>

    fun provideSyntheticScope(scope: ResolutionScope, metadata: SyntheticScopesMetadata): ResolutionScope {
        var result = scope
        for (provider in scopeProviders) {
            result = provider.provideSyntheticScope(result, metadata)
        }
        if (scope === result) return SyntheticResolutionScope.Empty
        return result
    }

    object Empty : SyntheticScopes {
        override val scopeProviders: Collection<SyntheticScopeProvider> = emptyList()
    }
}

fun SyntheticScopes.collectSyntheticExtensionProperties(receiverTypes: Collection<KotlinType>, name: Name, location: LookupLocation): List<PropertyDescriptor> =
        receiverTypes.traverseClassDescriptorsAndSupertypesOnlyOnce { type ->
            val scope = provideSyntheticScope(type.memberScope, SyntheticScopesMetadata(type = type, needExtensionProperties = true))
            val contributedVariables = scope.getContributedVariables(name, location).cast<Collection<PropertyDescriptor>>()
            contributedVariables.singleOrNull()
        }

fun SyntheticScopes.collectSyntheticMemberFunctions(receiverTypes: Collection<KotlinType>, name: Name, location: LookupLocation): List<FunctionDescriptor> =
        receiverTypes.flatMap { type ->
            val scope = provideSyntheticScope(type.memberScope, SyntheticScopesMetadata(type = type, needMemberFunctions = true))
            scope.getContributedFunctions(name, location)
        }

fun SyntheticScopes.collectSyntheticStaticFunctions(scope: ResolutionScope, name: Name, location: LookupLocation): Collection<FunctionDescriptor> {
    val syntheticScope = provideSyntheticScope(scope, SyntheticScopesMetadata(needStaticFunctions = true))
    return syntheticScope.getContributedFunctions(name, location)
}

fun SyntheticScopes.collectSyntheticConstructors(scope: ResolutionScope, name: Name, location: LookupLocation): Collection<FunctionDescriptor> {
    val syntheticScope = provideSyntheticScope(scope, SyntheticScopesMetadata(needConstructors = true))
    return syntheticScope.getContributedFunctions(name, location)
}

fun SyntheticScopes.collectSyntheticExtensionProperties(receiverTypes: Collection<KotlinType>): List<PropertyDescriptor> {
    return receiverTypes.traverseClassDescriptorsAndSupertypesOnlyOnce {
        val scope = provideSyntheticScope(it.memberScope, SyntheticScopesMetadata(type = it, needExtensionProperties = true))
        scope.getContributedDescriptors().cast<Collection<PropertyDescriptor>>()
    }.flatten()
}

fun SyntheticScopes.collectSyntheticMemberFunctions(receiverTypes: Collection<KotlinType>): List<FunctionDescriptor> =
        receiverTypes.flatMap { type ->
            val scope = provideSyntheticScope(type.memberScope, SyntheticScopesMetadata(type = type, needMemberFunctions = true))
            scope.getContributedDescriptors().cast<Collection<FunctionDescriptor>>()
        }

fun SyntheticScopes.collectSyntheticStaticFunctions(scope: ResolutionScope): Collection<FunctionDescriptor> {
    val syntheticScope = provideSyntheticScope(scope, SyntheticScopesMetadata(needStaticFunctions = true))
    return syntheticScope.getContributedDescriptors().cast()
}

fun SyntheticScopes.collectSyntheticConstructors(scope: ResolutionScope): Collection<FunctionDescriptor> {
    val syntheticScope = provideSyntheticScope(scope, SyntheticScopesMetadata(needConstructors = true))
    return syntheticScope.getContributedDescriptors().cast()
}

fun SyntheticScopes.collectSyntheticConstructors(constructor: ConstructorDescriptor): Collection<ConstructorDescriptor> {
    val scope = object : ResolutionScope {
        override fun getContributedClassifier(name: Name, location: LookupLocation): ClassifierDescriptor? = null
        override fun getContributedVariables(name: Name, location: LookupLocation): Collection<VariableDescriptor> = emptyList()
        override fun getContributedFunctions(name: Name, location: LookupLocation): Collection<FunctionDescriptor> = emptyList()
        override fun getContributedDescriptors(kindFilter: DescriptorKindFilter, nameFilter: (Name) -> Boolean): Collection<DeclarationDescriptor> =
                listOf(constructor)
    }
    val syntheticScope = provideSyntheticScope(scope, SyntheticScopesMetadata(needConstructors = true))
    return syntheticScope.getContributedDescriptors().cast()
}

private fun <T> Collection<KotlinType>.traverseClassDescriptorsAndSupertypesOnlyOnce(doStuff: (KotlinType) -> T?): List<T> {
    fun traverse(type: KotlinType, processedTypes: MutableSet<TypeConstructor>): List<T> {
        if (!processedTypes.add(type.constructor)) return emptyList()

        val descriptor = type.constructor.declarationDescriptor
        return if (descriptor is ClassDescriptor) {
            val res = doStuff(type)
            if (res == null) emptyList() else listOf(res)
        }
        else type.constructor.supertypes.flatMap { traverse(it, processedTypes) }
    }

    val processedTypes = hashSetOf<TypeConstructor>()
    return this.flatMap { traverse(it, processedTypes) }
}