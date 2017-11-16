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

package org.jetbrains.kotlin.gradle.tasks

import org.gradle.api.Project
import org.gradle.api.tasks.CacheableTask
import org.jetbrains.kotlin.gradle.internal.KaptGenerateStubsTask
import org.jetbrains.kotlin.gradle.internal.KaptTask
import org.jetbrains.kotlin.gradle.utils.ParsedGradleVersion

@CacheableTask
open class CacheableKotlinCompile : KotlinCompile()

@CacheableTask
open class CacheableKotlin2JsCompile : Kotlin2JsCompile()

@CacheableTask
internal open class CacheableKotlinCompileCommon : KotlinCompileCommon()

@CacheableTask
open class CacheableKaptTask : KaptTask()

@CacheableTask
open class CacheableKaptGenerateStubsTask : KaptGenerateStubsTask()

@CacheableTask
open class CacheableKotlinJsDce : KotlinJsDce()

internal fun shouldEnableGradleCache(project: Project) =
        ParsedGradleVersion.parse(project.gradle.gradleVersion)?.let { it >= ParsedGradleVersion(4, 3) } ?: false

internal fun <T> cacheableIfSupported(taskClass: Class<T>, project: Project): Class<out T> {
    return if (shouldEnableGradleCache(project))
        @Suppress("UNCHECKED_CAST")
        when (taskClass) {
            KotlinCompile::class.java -> CacheableKotlinCompile::class.java
            Kotlin2JsCompile::class.java -> CacheableKotlin2JsCompile::class.java
            KotlinCompileCommon::class.java -> CacheableKotlinCompileCommon::class.java
            KaptTask::class.java -> CacheableKaptTask::class.java
            KaptGenerateStubsTask::class.java -> CacheableKaptGenerateStubsTask::class.java
            KotlinJsDce::class.java -> CacheableKotlinJsDce::class.java
            else -> taskClass
        } as Class<out T>
    else taskClass
}