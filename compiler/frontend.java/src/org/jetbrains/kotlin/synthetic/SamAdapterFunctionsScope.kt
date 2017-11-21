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

import org.jetbrains.kotlin.config.LanguageVersionSettings
import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.descriptors.annotations.Annotations
import org.jetbrains.kotlin.descriptors.impl.SimpleFunctionDescriptorImpl
import org.jetbrains.kotlin.descriptors.impl.TypeAliasConstructorDescriptor
import org.jetbrains.kotlin.descriptors.impl.TypeAliasConstructorDescriptorImpl
import org.jetbrains.kotlin.descriptors.synthetic.SyntheticMemberDescriptor
import org.jetbrains.kotlin.incremental.components.LookupLocation
import org.jetbrains.kotlin.incremental.components.NoLookupLocation
import org.jetbrains.kotlin.load.java.components.SamConversionResolver
import org.jetbrains.kotlin.load.java.descriptors.JavaClassConstructorDescriptor
import org.jetbrains.kotlin.load.java.descriptors.JavaClassDescriptor
import org.jetbrains.kotlin.load.java.descriptors.JavaMethodDescriptor
import org.jetbrains.kotlin.load.java.lazy.descriptors.LazyJavaClassDescriptor
import org.jetbrains.kotlin.load.java.sam.SamAdapterDescriptor
import org.jetbrains.kotlin.load.java.sam.SamConstructorDescriptor
import org.jetbrains.kotlin.load.java.sam.SingleAbstractMethodUtils
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.resolve.DeprecationResolver
import org.jetbrains.kotlin.resolve.calls.inference.wrapWithCapturingSubstitution
import org.jetbrains.kotlin.resolve.scopes.DescriptorKindFilter
import org.jetbrains.kotlin.resolve.scopes.MemberScope
import org.jetbrains.kotlin.resolve.scopes.ResolutionScope
import org.jetbrains.kotlin.resolve.scopes.SyntheticScope
import org.jetbrains.kotlin.storage.StorageManager
import org.jetbrains.kotlin.types.*
import org.jetbrains.kotlin.types.checker.findCorrespondingSupertype
import org.jetbrains.kotlin.utils.Printer
import kotlin.properties.Delegates

interface SamAdapterExtensionFunctionDescriptor : FunctionDescriptor, SyntheticMemberDescriptor<FunctionDescriptor> {
    override val baseDescriptorForSynthetic: FunctionDescriptor
}

// todo: make real decorator
class SamAdapterFunctionsScopeDecorator(
        storageManager: StorageManager,
        private val samResolver: SamConversionResolver,
        private val deprecationResolver: DeprecationResolver,
        private val type: KotlinType
) : MemberScope {
    val memberScope = type.memberScope
    val functions = storageManager.createMemoizedFunction<Name, List<SimpleFunctionDescriptor>> {
        doGetFunctions(it)
    }

    private fun doGetFunctions(name: Name) =
            memberScope.getContributedFunctions(name, NoLookupLocation.FROM_SYNTHETIC_SCOPE).mapNotNull {
                wrapFunctionDescriptor(it.original)?.substituteForReceiverType(type)
            }

    private fun wrapFunctionDescriptor(function: FunctionDescriptor): SimpleFunctionDescriptor? {
        if (!function.visibility.isVisibleOutside()) return null
        if (!function.hasJavaOriginInHierarchy()) return null //TODO: should we go into base at all?
        if (!SingleAbstractMethodUtils.isSamAdapterNecessary(function)) return null
        if (function.returnType == null) return null
        if (deprecationResolver.isHiddenInResolution(function)) return null
        return SamAdapterFunctionsScope.MyFunctionDescriptor.create(function, samResolver)
    }

    override fun getContributedVariables(name: Name, location: LookupLocation): Collection<PropertyDescriptor> = emptySet()

    override fun getContributedFunctions(name: Name, location: LookupLocation): Collection<SimpleFunctionDescriptor> = functions(name)

    override fun getFunctionNames(): Set<Name> = emptySet()

    override fun getVariableNames(): Set<Name> = emptySet()

    override fun getClassifierNames(): Set<Name>? = null

    override fun printScopeStructure(p: Printer) {}

    override fun getContributedClassifier(name: Name, location: LookupLocation): ClassifierDescriptor? = null

    override fun getContributedDescriptors(kindFilter: DescriptorKindFilter, nameFilter: (Name) -> Boolean): Collection<DeclarationDescriptor>
            = emptySet()
}
class SamAdapterFunctionsScope(
        private val storageManager: StorageManager,
        private val languageVersionSettings: LanguageVersionSettings,
        private val samResolver: SamConversionResolver,
        private val deprecationResolver: DeprecationResolver
) : SyntheticScope {
    private val decorateScope = storageManager.createMemoizedFunction<KotlinType, MemberScope> {
        SamAdapterFunctionsScopeDecorator(storageManager, samResolver, deprecationResolver, it)
    }

    private val samAdapterForStaticFunction =
            storageManager.createMemoizedFunction<JavaMethodDescriptor, SamAdapterDescriptor<JavaMethodDescriptor>> { function ->
                SingleAbstractMethodUtils.createSamAdapterFunction(function, samResolver)
            }

    private val samConstructorForClassifier =
            storageManager.createMemoizedFunction<JavaClassDescriptor, SamConstructorDescriptor> { classifier ->
                SingleAbstractMethodUtils.createSamConstructorFunction(classifier.containingDeclaration, classifier, samResolver)
            }

    private val samConstructorForJavaConstructor =
            storageManager.createMemoizedFunction<JavaClassConstructorDescriptor, ClassConstructorDescriptor> { constructor ->
                SingleAbstractMethodUtils.createSamAdapterConstructor(constructor, samResolver) as ClassConstructorDescriptor
            }

    private val samConstructorForTypeAliasConstructor =
            storageManager.createMemoizedFunctionWithNullableValues<Pair<ClassConstructorDescriptor, TypeAliasDescriptor>, TypeAliasConstructorDescriptor> { (constructor, typeAliasDescriptor) ->
                TypeAliasConstructorDescriptorImpl.createIfAvailable(storageManager, typeAliasDescriptor, constructor)
            }

    override fun getSyntheticMemberFunctions(receiverTypes: Collection<KotlinType>, name: Name, location: LookupLocation) =
            receiverTypes.flatMap { type ->
                decorateScope(type).getContributedFunctions(name, location)
            }

    override fun getSyntheticMemberFunctions(receiverTypes: Collection<KotlinType>): Collection<FunctionDescriptor> {
        return receiverTypes.flatMap { type ->
            type.memberScope.getContributedDescriptors(DescriptorKindFilter.FUNCTIONS)
                    .filterIsInstance<FunctionDescriptor>()
                    .flatMap {
                        decorateScope(type).getContributedFunctions(it.name, NoLookupLocation.FROM_SYNTHETIC_SCOPE)
                    }
        }
    }

    override fun getSyntheticExtensionProperties(receiverTypes: Collection<KotlinType>, name: Name, location: LookupLocation): Collection<PropertyDescriptor> = emptyList()

    override fun getSyntheticExtensionProperties(receiverTypes: Collection<KotlinType>): Collection<PropertyDescriptor> = emptyList()

    override fun getSyntheticStaticFunctions(scope: ResolutionScope, name: Name, location: LookupLocation): Collection<FunctionDescriptor> {
        return getSamFunctions(scope.getContributedFunctions(name, location))
    }

    override fun getSyntheticConstructors(scope: ResolutionScope, name: Name, location: LookupLocation): Collection<FunctionDescriptor> {
        val classifier = scope.getContributedClassifier(name, location) ?: return emptyList()
        return getAllSamConstructors(classifier)
    }

    override fun getSyntheticStaticFunctions(scope: ResolutionScope): Collection<FunctionDescriptor> {
        return getSamFunctions(scope.getContributedDescriptors(DescriptorKindFilter.FUNCTIONS))
    }

    override fun getSyntheticConstructors(scope: ResolutionScope): Collection<FunctionDescriptor> {
        return scope.getContributedDescriptors(DescriptorKindFilter.CLASSIFIERS)
                .filterIsInstance<ClassifierDescriptor>()
                .flatMap { getAllSamConstructors(it) }
    }

    override fun getSyntheticConstructor(constructor: ConstructorDescriptor): ConstructorDescriptor? {
        when (constructor) {
            is JavaClassConstructorDescriptor -> return createJavaSamAdapterConstructor(constructor)
            is TypeAliasConstructorDescriptor -> {
                val underlyingConstructor = constructor.underlyingConstructorDescriptor as? JavaClassConstructorDescriptor ?: return null
                val underlyingSamConstructor = createJavaSamAdapterConstructor(underlyingConstructor) ?: return null

                return samConstructorForTypeAliasConstructor(Pair(underlyingSamConstructor, constructor.typeAliasDescriptor))
            }
            else -> return null
        }
    }

    private fun createJavaSamAdapterConstructor(constructor: JavaClassConstructorDescriptor): ClassConstructorDescriptor? {
        if (!SingleAbstractMethodUtils.isSamAdapterNecessary(constructor)) return null
        return samConstructorForJavaConstructor(constructor)
    }

    private fun getSamFunctions(functions: Collection<DeclarationDescriptor>): List<SamAdapterDescriptor<JavaMethodDescriptor>> {
        return functions.mapNotNull { function ->
            if (function !is JavaMethodDescriptor) return@mapNotNull null
            if (function.dispatchReceiverParameter != null) return@mapNotNull null // consider only statics
            if (!SingleAbstractMethodUtils.isSamAdapterNecessary(function)) return@mapNotNull null

            samAdapterForStaticFunction(function)
        }
    }

    private fun getAllSamConstructors(classifier: ClassifierDescriptor): List<FunctionDescriptor> {
        return getSamAdaptersFromConstructors(classifier) + listOfNotNull(getSamConstructor(classifier))
    }

    private fun getSamAdaptersFromConstructors(classifier: ClassifierDescriptor): List<FunctionDescriptor> {
        if (classifier !is JavaClassDescriptor) return emptyList()

        return arrayListOf<FunctionDescriptor>().apply {
            for (constructor in classifier.constructors) {
                val samConstructor = getSyntheticConstructor(constructor) ?: continue
                add(samConstructor)
            }
        }
    }

    private fun getSamConstructor(classifier: ClassifierDescriptor): SamConstructorDescriptor? {
        if (classifier is TypeAliasDescriptor) {
            return getTypeAliasSamConstructor(classifier)
        }

        if (classifier !is LazyJavaClassDescriptor || classifier.defaultFunctionTypeForSamInterface == null) return null
        return samConstructorForClassifier(classifier)
    }

    private fun getTypeAliasSamConstructor(classifier: TypeAliasDescriptor): SamConstructorDescriptor? {
        val classDescriptor = classifier.classDescriptor ?: return null
        if (classDescriptor !is LazyJavaClassDescriptor || classDescriptor.defaultFunctionTypeForSamInterface == null) return null

        return SingleAbstractMethodUtils.createTypeAliasSamConstructorFunction(
                classifier, samConstructorForClassifier(classDescriptor), samResolver)
    }

    class MyFunctionDescriptor(
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
}

private fun FunctionDescriptor.substituteForReceiverType(type: KotlinType): SimpleFunctionDescriptor? {
    val containingClass = containingDeclaration as? ClassDescriptor ?: return null
    val correspondingSupertype = findCorrespondingSupertype(type, containingClass.defaultType) ?: return null

    return substitute(
            TypeConstructorSubstitution
                    .create(correspondingSupertype)
                    .wrapWithCapturingSubstitution(needApproximation = true)
                    .buildSubstitutor()
    ) as SimpleFunctionDescriptor
}