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
import org.jetbrains.kotlin.storage.StorageManager

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

    object Empty : ResolutionScope {
        override fun getContributedClassifier(name: Name, location: LookupLocation): ClassifierDescriptor? = null
        override fun getContributedVariables(name: Name, location: LookupLocation): Collection<VariableDescriptor> = emptyList()
        override fun getContributedFunctions(name: Name, location: LookupLocation): Collection<FunctionDescriptor> = emptyList()
        override fun getContributedDescriptors(kindFilter: DescriptorKindFilter, nameFilter: (Name) -> Boolean): Collection<DeclarationDescriptor> = emptyList()
    }
}