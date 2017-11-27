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
import org.jetbrains.kotlin.resolve.scopes.DescriptorKindFilter.Companion.FUNCTIONS_MASK
import org.jetbrains.kotlin.resolve.scopes.DescriptorKindFilter.Companion.NON_SINGLETON_CLASSIFIERS_MASK
import org.jetbrains.kotlin.resolve.scopes.DescriptorKindFilter.Companion.PACKAGES_MASK
import org.jetbrains.kotlin.resolve.scopes.DescriptorKindFilter.Companion.SINGLETON_CLASSIFIERS_MASK
import org.jetbrains.kotlin.resolve.scopes.DescriptorKindFilter.Companion.TYPE_ALIASES_MASK
import org.jetbrains.kotlin.resolve.scopes.DescriptorKindFilter.Companion.VARIABLES_MASK
import org.jetbrains.kotlin.storage.StorageManager

/**
 * A chain of synthetic scope which enhance [ResolutionScope].
 * Use [SyntheticScopeProvider] to retrieve one.
 */
abstract class SyntheticResolutionScope(
        storageManager: StorageManager,
        private val wrappedScope: ResolutionScope
) : ResolutionScope {
    private val originalScope = storageManager.createLazyValue {
        doGetOriginal()
    }

    private fun doGetOriginal(): ResolutionScope {
        var result = wrappedScope
        while (result is SyntheticResolutionScope) {
            result = result.wrappedScope
        }
        return result
    }

    override fun getContributedClassifier(name: Name, location: LookupLocation): ClassifierDescriptor? =
            getSyntheticContributedClassifier(name, location)

    private fun getSyntheticContributedClassifier(name: Name, location: LookupLocation) =
            (wrappedScope as? SyntheticResolutionScope)?.getContributedClassifier(name, location)

    override fun getContributedVariables(name: Name, location: LookupLocation): Collection<VariableDescriptor> =
            getSyntheticContributedVariables(name, location)

    private fun getSyntheticContributedVariables(name: Name, location: LookupLocation) =
            (wrappedScope as? SyntheticResolutionScope)?.getContributedVariables(name, location) ?: emptyList()

    override fun getContributedFunctions(name: Name, location: LookupLocation): Collection<FunctionDescriptor> =
            getSyntheticContributedFunctions(name, location)

    private fun getSyntheticContributedFunctions(name: Name, location: LookupLocation) =
            (wrappedScope as? SyntheticResolutionScope)?.getContributedFunctions(name, location) ?: emptyList()

    override fun getContributedDescriptors(kindFilter: DescriptorKindFilter, nameFilter: (Name) -> Boolean): Collection<DeclarationDescriptor> =
            getSyntheticContributedDescriptors(kindFilter, nameFilter)

    private fun getSyntheticContributedDescriptors(kindFilter: DescriptorKindFilter, nameFilter: (Name) -> Boolean) =
            (wrappedScope as? SyntheticResolutionScope)?.getContributedDescriptors(kindFilter, nameFilter) ?: emptyList()

    override fun recordLookup(name: Name, location: LookupLocation) {
        originalScope().recordLookup(name, location)
    }

    // If the synthetic scopes chain contains the classifier, it shadows original. Otherwise, return original classifier.
    protected fun getContributedClassisfierShadowOriginal(name: Name, location: LookupLocation): ClassifierDescriptor? {
        val synthetic = getSyntheticContributedClassifier(name, location)
        val original = originalScope().getContributedClassifier(name, location)
        return synthetic ?: original
    }

    protected fun getContributedVariablesShadowOriginal(name: Name, location: LookupLocation): Collection<VariableDescriptor> {
        val synthetic = getSyntheticContributedVariables(name, location)
        return if (synthetic.isNotEmpty()) synthetic
        else originalScope().getContributedVariables(name, location)
    }

    protected fun getContributedFunctionsShadowOriginal(name: Name, location: LookupLocation): Collection<FunctionDescriptor> {
        val synthetic = getSyntheticContributedFunctions(name, location)
        return if (synthetic.isNotEmpty()) synthetic
        else originalScope().getContributedFunctions(name, location)
    }

    protected fun getContributedDescriptorsShadowOriginal(
            kindFilter: DescriptorKindFilter = DescriptorKindFilter.ALL,
            nameFilter: (Name) -> Boolean = MemberScope.ALL_NAME_FILTER
    ): Collection<DeclarationDescriptor> {
        val res = hashSetOf<DeclarationDescriptor>()
        if (kindFilter.kindMask and NON_SINGLETON_CLASSIFIERS_MASK != 0) {
            res.addAll(shadowDescriptors(DescriptorKindFilter.NON_SINGLETON_CLASSIFIERS, nameFilter))
        }
        if (kindFilter.kindMask and SINGLETON_CLASSIFIERS_MASK != 0) {
            res.addAll(shadowDescriptors(DescriptorKindFilter.SINGLETON_CLASSIFIERS, nameFilter))
        }
        if (kindFilter.kindMask and TYPE_ALIASES_MASK != 0) {
            res.addAll(shadowDescriptors(DescriptorKindFilter.TYPE_ALIASES, nameFilter))
        }
        if (kindFilter.kindMask and PACKAGES_MASK != 0) {
            res.addAll(shadowDescriptors(DescriptorKindFilter.PACKAGES, nameFilter))
        }
        if (kindFilter.kindMask and FUNCTIONS_MASK != 0) {
            res.addAll(shadowDescriptors(DescriptorKindFilter.FUNCTIONS, nameFilter))
        }
        if (kindFilter.kindMask and VARIABLES_MASK != 0) {
            res.addAll(shadowDescriptors(DescriptorKindFilter.VARIABLES, nameFilter))
        }
        return res
    }

    private fun shadowDescriptors(kindFilter: DescriptorKindFilter, nameFilter: (Name) -> Boolean): Collection<DeclarationDescriptor> {
        val synthetic = getSyntheticContributedDescriptors(kindFilter, nameFilter)
        val syntheticNames = synthetic.map { it.name }
        val original = originalScope().getContributedDescriptors(kindFilter, nameFilter).filterNot { it.name in syntheticNames }
        return synthetic + original
    }

    object Empty : ResolutionScope {
        override fun getContributedClassifier(name: Name, location: LookupLocation): ClassifierDescriptor? = null
        override fun getContributedVariables(name: Name, location: LookupLocation): Collection<VariableDescriptor> = emptyList()
        override fun getContributedFunctions(name: Name, location: LookupLocation): Collection<FunctionDescriptor> = emptyList()
        override fun getContributedDescriptors(kindFilter: DescriptorKindFilter, nameFilter: (Name) -> Boolean): Collection<DeclarationDescriptor> = emptyList()
    }
}