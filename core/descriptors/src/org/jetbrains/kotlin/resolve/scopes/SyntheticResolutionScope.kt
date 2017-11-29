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

import org.jetbrains.kotlin.descriptors.ClassifierDescriptor
import org.jetbrains.kotlin.descriptors.DeclarationDescriptor
import org.jetbrains.kotlin.descriptors.FunctionDescriptor
import org.jetbrains.kotlin.descriptors.VariableDescriptor
import org.jetbrains.kotlin.incremental.components.LookupLocation
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.resolve.DescriptorEquivalenceForOverrides
import org.jetbrains.kotlin.resolve.scopes.DescriptorKindFilter.Companion.FUNCTIONS_MASK
import org.jetbrains.kotlin.resolve.scopes.DescriptorKindFilter.Companion.NON_SINGLETON_CLASSIFIERS_MASK
import org.jetbrains.kotlin.resolve.scopes.DescriptorKindFilter.Companion.PACKAGES_MASK
import org.jetbrains.kotlin.resolve.scopes.DescriptorKindFilter.Companion.SINGLETON_CLASSIFIERS_MASK
import org.jetbrains.kotlin.resolve.scopes.DescriptorKindFilter.Companion.TYPE_ALIASES_MASK
import org.jetbrains.kotlin.resolve.scopes.DescriptorKindFilter.Companion.VARIABLES_MASK

/**
 * A chain of synthetic scope which enhance [ResolutionScope].
 * Use [SyntheticScopeProvider] to retrieve one.
 */
abstract class SyntheticResolutionScope : ResolutionScope {
    abstract val wrappedScope: ResolutionScope

    override fun getContributedClassifier(name: Name, location: LookupLocation): ClassifierDescriptor? =
            wrappedScope.getContributedClassifier(name, location)

    override fun getContributedVariables(name: Name, location: LookupLocation): Collection<VariableDescriptor> =
            wrappedScope.getContributedVariables(name, location)

    override fun getContributedFunctions(name: Name, location: LookupLocation): Collection<FunctionDescriptor> =
            wrappedScope.getContributedFunctions(name, location)

    override fun getContributedDescriptors(kindFilter: DescriptorKindFilter, nameFilter: (Name) -> Boolean): Collection<DeclarationDescriptor> =
            wrappedScope.getContributedDescriptors(kindFilter, nameFilter)

    override fun recordLookup(name: Name, location: LookupLocation) =
            wrappedScope.recordLookup(name, location)
}

// Replace the symbols from wrappedScope with newly created symbols.
// For example, if [wrappedScope.getContributedVariables] returns "private foo: Int"
// and [newSymbols] returns another "public foo: Int", as
// SyntheticPropertiesScope does, the resulting symbol will be "public foo: Int" as
// original "private foo: Int" will be shadowed.
fun SyntheticResolutionScope.shadowOriginalClassifier(
        name: Name,
        location: LookupLocation,
        newClassifier: () -> ClassifierDescriptor?
): ClassifierDescriptor? = newClassifier() ?: wrappedScope.getContributedClassifier(name, location)

fun SyntheticResolutionScope.shadowOriginalVariables(
        name: Name,
        location: LookupLocation,
        newVariables: () -> Collection<VariableDescriptor>
): Collection<VariableDescriptor> {
    val synthetics = newVariables()
    val syntheticNames = synthetics.map { it.name }
    val original = wrappedScope.getContributedVariables(name, location)
    val notShadowed = original.filterNot { it.name in syntheticNames }
    return synthetics + notShadowed
}

fun SyntheticResolutionScope.shadowOriginalFunctions(
        name: Name,
        location: LookupLocation,
        newFunctions: () -> Collection<FunctionDescriptor>
): Collection<FunctionDescriptor> {
    val synthetics = newFunctions()
    return synthetics + wrappedScope.getContributedFunctions(name, location).filterNot { original ->
        synthetics.any { synthetic ->
            DescriptorEquivalenceForOverrides.areCallableDescriptorsEquivalent(original, synthetic)
        }
    }
}

fun SyntheticResolutionScope.shadowOriginalDescriptors(
        kindFilter: DescriptorKindFilter,
        nameFilter: (Name) -> Boolean,
        newDescriptors: (DescriptorKindFilter) -> Collection<DeclarationDescriptor>
): Collection<DeclarationDescriptor> {
    fun doShadow(
            kindFilter: DescriptorKindFilter,
            nameFilter: (Name) -> Boolean,
            newDescriptors: (DescriptorKindFilter) -> Collection<DeclarationDescriptor>
    ): Collection<DeclarationDescriptor> {
        val synthetics = newDescriptors(kindFilter)
        val syntheticNames = synthetics.map { it.name }
        val original = wrappedScope.getContributedDescriptors(kindFilter, nameFilter)
        return synthetics + original.filterNot { it.name in syntheticNames }
    }

    val res = hashSetOf<DeclarationDescriptor>()
    if (kindFilter.acceptsKinds(NON_SINGLETON_CLASSIFIERS_MASK)) {
        res.addAll(doShadow(DescriptorKindFilter.NON_SINGLETON_CLASSIFIERS, nameFilter, newDescriptors))
    }
    if (kindFilter.acceptsKinds(SINGLETON_CLASSIFIERS_MASK)) {
        res.addAll(doShadow(DescriptorKindFilter.SINGLETON_CLASSIFIERS, nameFilter, newDescriptors))
    }
    if (kindFilter.acceptsKinds(TYPE_ALIASES_MASK)) {
        res.addAll(doShadow(DescriptorKindFilter.TYPE_ALIASES, nameFilter, newDescriptors))
    }
    if (kindFilter.acceptsKinds(PACKAGES_MASK)) {
        res.addAll(doShadow(DescriptorKindFilter.PACKAGES, nameFilter, newDescriptors))
    }
    if (kindFilter.acceptsKinds(FUNCTIONS_MASK)) {
        res.addAll(doShadow(DescriptorKindFilter.FUNCTIONS, nameFilter, newDescriptors))
    }
    if (kindFilter.acceptsKinds(VARIABLES_MASK)) {
        res.addAll(doShadow(DescriptorKindFilter.VARIABLES, nameFilter, newDescriptors))
    }
    return res
}