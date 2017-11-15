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

package org.jetbrains.kotlin.psi

import com.intellij.psi.impl.source.tree.LeafPsiElement
import org.jetbrains.kotlin.descriptors.CallableDescriptor
import org.jetbrains.kotlin.descriptors.ClassDescriptor

class ImplicitThisValueArgument private constructor(
        private val element: KtElement,
        val callableDescriptor: CallableDescriptor? = null,
        val classDescriptor: ClassDescriptor? = null
): ValueArgument {
    override fun getArgumentExpression(): KtExpression? = null

    override fun getArgumentName(): ValueArgumentName? = null

    override fun isNamed(): Boolean = false

    override fun asElement(): KtElement = element

    override fun getSpreadElement(): LeafPsiElement? = null

    override fun isExternal(): Boolean = false

    constructor(element: KtElement, descriptor: CallableDescriptor): this(element, callableDescriptor = descriptor)
    constructor(element: KtElement, descriptor: ClassDescriptor): this(element, classDescriptor = descriptor)
}