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
import org.jetbrains.kotlin.storage.StorageManager
import org.jetbrains.kotlin.types.KotlinType
import org.jetbrains.kotlin.types.TypeConstructor
import org.jetbrains.kotlin.types.WrappedType
import org.jetbrains.kotlin.utils.Printer
import org.jetbrains.kotlin.utils.addToStdlib.cast

class SyntheticType(
        override val delegate: KotlinType,
        override val memberScope: MemberScope
) : WrappedType()

interface SyntheticScope {
    fun contriveType(type: KotlinType): KotlinType = type

    fun getSyntheticMemberFunctions(receiverTypes: Collection<KotlinType>, name: Name, location: LookupLocation): Collection<FunctionDescriptor>
    fun getSyntheticStaticFunctions(scope: ResolutionScope, name: Name, location: LookupLocation): Collection<FunctionDescriptor>
    fun getSyntheticConstructors(scope: ResolutionScope, name: Name, location: LookupLocation): Collection<FunctionDescriptor>

    fun getSyntheticMemberFunctions(receiverTypes: Collection<KotlinType>): Collection<FunctionDescriptor>
    fun getSyntheticStaticFunctions(scope: ResolutionScope): Collection<FunctionDescriptor>
    fun getSyntheticConstructors(scope: ResolutionScope): Collection<FunctionDescriptor>

    fun getSyntheticConstructor(constructor: ConstructorDescriptor): ConstructorDescriptor?
}

interface SyntheticScopes {
    val scopes: Collection<SyntheticScope>

    fun contriveType(type: KotlinType): KotlinType {
        var result = type
        for (scope in scopes) {
            result = scope.contriveType(result)
        }
        return result
    }

    object Empty : SyntheticScopes {
        override val scopes: Collection<SyntheticScope> = emptyList()
    }
}

abstract class SyntheticResolutionScope(storageManager: StorageManager) : ResolutionScope {
    protected abstract val wrappedScope: ResolutionScope
    protected val originalScope = storageManager.createLazyValue {
        doGetOriginal()
    }

    open protected fun doGetOriginal(): ResolutionScope {
        var result = wrappedScope
        while (result is SyntheticResolutionScope) {
            result = result.wrappedScope
        }
        return result
    }

    override fun getContributedClassifier(name: Name, location: LookupLocation): ClassifierDescriptor? = null
    override fun getContributedVariables(name: Name, location: LookupLocation): Collection<VariableDescriptor> = emptyList()
    override fun getContributedFunctions(name: Name, location: LookupLocation): Collection<FunctionDescriptor> = emptyList()
    override fun getContributedDescriptors(kindFilter: DescriptorKindFilter, nameFilter: (Name) -> Boolean): Collection<DeclarationDescriptor> = emptyList()
}

abstract class SyntheticMemberScope(storageManager: StorageManager) : MemberScope {
    protected abstract val wrappedScope: MemberScope
    protected val originalScope = storageManager.createLazyValue {
        doGetOriginal()
    }

    open protected fun doGetOriginal(): MemberScope {
        var result = wrappedScope
        while (result is SyntheticMemberScope) {
            result = result.wrappedScope
        }
        return result
    }

    override fun getContributedClassifier(name: Name, location: LookupLocation): ClassifierDescriptor? = null
    override fun getContributedVariables(name: Name, location: LookupLocation): Collection<PropertyDescriptor> = emptyList()
    override fun getContributedFunctions(name: Name, location: LookupLocation): Collection<SimpleFunctionDescriptor> = emptyList()
    override fun getContributedDescriptors(kindFilter: DescriptorKindFilter, nameFilter: (Name) -> Boolean): Collection<DeclarationDescriptor> = emptyList()
    override fun getFunctionNames(): Set<Name> = emptySet()
    override fun getVariableNames(): Set<Name> = emptySet()
    override fun getClassifierNames(): Set<Name>? = null
    override fun printScopeStructure(p: Printer) {}
}

fun SyntheticScopes.collectSyntheticExtensionProperties(receiverTypes: Collection<KotlinType>, name: Name, location: LookupLocation): List<PropertyDescriptor> {
    return receiverTypes.traverseClassDescriptorsAndSupertypesOnlyOnce { type ->
        contriveType(type).memberScope.getContributedVariables(name, location).singleOrNull()
    }
}

fun SyntheticScopes.collectSyntheticMemberFunctions(receiverTypes: Collection<KotlinType>, name: Name, location: LookupLocation)
        = scopes.flatMap { it.getSyntheticMemberFunctions(receiverTypes, name, location) }

fun SyntheticScopes.collectSyntheticStaticFunctions(scope: ResolutionScope, name: Name, location: LookupLocation)
        = scopes.flatMap { it.getSyntheticStaticFunctions(scope, name, location) }

fun SyntheticScopes.collectSyntheticConstructors(scope: ResolutionScope, name: Name, location: LookupLocation)
        = scopes.flatMap { it.getSyntheticConstructors(scope, name, location) }

fun SyntheticScopes.collectSyntheticExtensionProperties(receiverTypes: Collection<KotlinType>): List<PropertyDescriptor> {
    return receiverTypes.traverseClassDescriptorsAndSupertypesOnlyOnce {
        contriveType(it).memberScope.getContributedDescriptors().cast<Collection<PropertyDescriptor>>()
    }.flatten()
}

fun SyntheticScopes.collectSyntheticMemberFunctions(receiverTypes: Collection<KotlinType>)
        = scopes.flatMap { it.getSyntheticMemberFunctions(receiverTypes) }

fun SyntheticScopes.collectSyntheticStaticFunctions(scope: ResolutionScope)
        = scopes.flatMap { it.getSyntheticStaticFunctions(scope) }

fun SyntheticScopes.collectSyntheticConstructors(scope: ResolutionScope)
        = scopes.flatMap { it.getSyntheticConstructors(scope) }

fun SyntheticScopes.collectSyntheticConstructors(constructor: ConstructorDescriptor)
        = scopes.mapNotNull { it.getSyntheticConstructor(constructor) }

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