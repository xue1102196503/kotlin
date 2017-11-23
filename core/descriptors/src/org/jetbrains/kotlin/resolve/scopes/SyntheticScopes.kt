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

interface SyntheticProperty: PropertyDescriptor
interface SyntheticMemberFunction: FunctionDescriptor
interface SyntheticStaticFunction : FunctionDescriptor
interface SyntheticConstructorFunction: FunctionDescriptor

interface SyntheticScope {
    fun contriveType(type: KotlinType): KotlinType = type
    fun contriveScope(scope: ResolutionScope): ResolutionScope = scope
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

    fun contriveScope(scope: ResolutionScope): ResolutionScope {
        var result = scope
        for (provider in scopes) {
            result = provider.contriveScope(result)
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

    override fun getContributedClassifier(name: Name, location: LookupLocation): ClassifierDescriptor? =
            if (wrappedScope is SyntheticResolutionScope) wrappedScope.getContributedClassifier(name, location) else null

    override fun getContributedVariables(name: Name, location: LookupLocation): Collection<VariableDescriptor> =
            if (wrappedScope is SyntheticResolutionScope) wrappedScope.getContributedVariables(name, location) else emptyList()

    override fun getContributedFunctions(name: Name, location: LookupLocation): Collection<FunctionDescriptor> =
            if (wrappedScope is SyntheticResolutionScope) wrappedScope.getContributedFunctions(name, location) else emptyList()

    override fun getContributedDescriptors(kindFilter: DescriptorKindFilter, nameFilter: (Name) -> Boolean): Collection<DeclarationDescriptor> =
            if (wrappedScope is SyntheticResolutionScope) wrappedScope.getContributedDescriptors(kindFilter, nameFilter) else emptyList()

    companion object {
        fun createScopeForConstructor(constructor: ConstructorDescriptor): ResolutionScope =
                object : ResolutionScope {
                    override fun getContributedClassifier(name: Name, location: LookupLocation): ClassifierDescriptor? = null
                    override fun getContributedVariables(name: Name, location: LookupLocation): Collection<VariableDescriptor> = emptyList()
                    override fun getContributedFunctions(name: Name, location: LookupLocation): Collection<FunctionDescriptor> = emptyList()
                    override fun getContributedDescriptors(kindFilter: DescriptorKindFilter, nameFilter: (Name) -> Boolean): Collection<DeclarationDescriptor> =
                            listOf(constructor)
                }
    }
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

    override fun getContributedClassifier(name: Name, location: LookupLocation): ClassifierDescriptor? =
            if (wrappedScope is SyntheticMemberScope) wrappedScope.getContributedClassifier(name, location) else null

    override fun getContributedVariables(name: Name, location: LookupLocation): Collection<PropertyDescriptor> =
            if (wrappedScope is SyntheticMemberScope) wrappedScope.getContributedVariables(name, location) else emptyList()

    override fun getContributedFunctions(name: Name, location: LookupLocation): Collection<SimpleFunctionDescriptor> =
            if (wrappedScope is SyntheticMemberScope) wrappedScope.getContributedFunctions(name, location) else emptyList()

    override fun getContributedDescriptors(kindFilter: DescriptorKindFilter, nameFilter: (Name) -> Boolean): Collection<DeclarationDescriptor> =
            if (wrappedScope is SyntheticMemberScope) wrappedScope.getContributedDescriptors(kindFilter, nameFilter) else emptyList()

    override fun getFunctionNames(): Set<Name> =
            if (wrappedScope is SyntheticMemberScope) wrappedScope.getFunctionNames() else emptySet()

    override fun getVariableNames(): Set<Name> =
            if (wrappedScope is SyntheticMemberScope) wrappedScope.getVariableNames() else emptySet()

    override fun getClassifierNames(): Set<Name>? =
            if (wrappedScope is SyntheticMemberScope) wrappedScope.getClassifierNames() else null

    override fun printScopeStructure(p: Printer) {
        if (wrappedScope is SyntheticMemberScope) wrappedScope.printScopeStructure(p)
    }
}

fun SyntheticScopes.collectSyntheticExtensionProperties(receiverTypes: Collection<KotlinType>, name: Name, location: LookupLocation): List<PropertyDescriptor> {
    val result = receiverTypes.traverseClassDescriptorsAndSupertypesOnlyOnce { type ->
        val memberScope = contriveType(type).memberScope
        val contributedVariables = memberScope.getContributedVariables(name, location).filterIsInstance<SyntheticProperty>()
        contributedVariables.singleOrNull()
    }
    return result
}

fun SyntheticScopes.collectSyntheticMemberFunctions(receiverTypes: Collection<KotlinType>, name: Name, location: LookupLocation) =
        receiverTypes.flatMap { type ->
            val memberScope = contriveType(type).memberScope
            val contributedFunctions = memberScope.getContributedFunctions(name, location)
            contributedFunctions.filterIsInstance<SyntheticMemberFunction>()
        }

fun SyntheticScopes.collectSyntheticStaticFunctions(scope: ResolutionScope, name: Name, location: LookupLocation): List<SyntheticStaticFunction> {
    val contributedFunctions = contriveScope(scope).getContributedFunctions(name, location)
    val result = contributedFunctions.filterIsInstance<SyntheticStaticFunction>()
    return result
}

fun SyntheticScopes.collectSyntheticConstructors(scope: ResolutionScope, name: Name, location: LookupLocation): List<SyntheticConstructorFunction> {
    val contributedFunctions = contriveScope(scope).getContributedFunctions(name, location)
    val result = contributedFunctions.filterIsInstance<SyntheticConstructorFunction>()
    return result
}

fun SyntheticScopes.collectSyntheticExtensionProperties(receiverTypes: Collection<KotlinType>): List<PropertyDescriptor> {
    return receiverTypes.traverseClassDescriptorsAndSupertypesOnlyOnce {
        val memberScope = contriveType(it).memberScope
        val contributedDescriptors = memberScope.getContributedDescriptors()
        contributedDescriptors.filterIsInstance<SyntheticProperty>()
    }.flatten()
}

fun SyntheticScopes.collectSyntheticMemberFunctions(receiverTypes: Collection<KotlinType>) =
        receiverTypes.flatMap { type ->
            val memberScope = contriveType(type).memberScope
            val contributedDescriptors = memberScope.getContributedDescriptors()
            contributedDescriptors.filterIsInstance<SyntheticMemberFunction>()
        }

fun SyntheticScopes.collectSyntheticStaticFunctions(scope: ResolutionScope): List<SyntheticStaticFunction> {
    val contributedDescriptors = contriveScope(scope).getContributedDescriptors()
    val result = contributedDescriptors.filterIsInstance<SyntheticStaticFunction>()
    return result
}

fun SyntheticScopes.collectSyntheticConstructors(scope: ResolutionScope): List<SyntheticConstructorFunction> {
    val contributedDescriptors = contriveScope(scope).getContributedDescriptors()
    val result = contributedDescriptors.filterIsInstance<SyntheticConstructorFunction>()
    return result
}

fun SyntheticScopes.collectSyntheticConstructors(constructor: ConstructorDescriptor): Collection<ConstructorDescriptor> {
    val contributedDescriptors = contriveScope(SyntheticResolutionScope.createScopeForConstructor(constructor)).getContributedDescriptors()
    val instance = contributedDescriptors.filterIsInstance<SyntheticConstructorFunction>()
    val result = instance.cast<Collection<ConstructorDescriptor>>()
    return result
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