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

package org.jetbrains.kotlin.codegen.coroutines

import org.jetbrains.kotlin.codegen.inline.RETURN_TYPE_IS_UNIT_METHOD_NAME
import org.jetbrains.kotlin.codegen.inline.SUSPEND_CALL_MARKER_CLASS_NAME
import org.jetbrains.kotlin.codegen.optimization.common.removeAll
import org.jetbrains.kotlin.codegen.optimization.transformer.MethodTransformer
import org.jetbrains.org.objectweb.asm.Opcodes
import org.jetbrains.org.objectweb.asm.Type
import org.jetbrains.org.objectweb.asm.tree.AbstractInsnNode
import org.jetbrains.org.objectweb.asm.tree.FieldInsnNode
import org.jetbrains.org.objectweb.asm.tree.MethodInsnNode
import org.jetbrains.org.objectweb.asm.tree.MethodNode
import org.jetbrains.org.objectweb.asm.tree.analysis.BasicValue
import org.jetbrains.org.objectweb.asm.tree.analysis.Interpreter

object ReturnUnitMethodTransformer: MethodTransformer() {
    override fun transform(internalClassName: String, methodNode: MethodNode) {
        val unitMarks = findReturnsUnitMarks(methodNode)
        if (unitMarks.isEmpty()) return
        val pops = findPopAndReturnUnitSequences(methodNode)
        if (pops.isEmpty()) {
            methodNode.instructions.removeAll(unitMarks)
            return
        }
        val popWithDefs = findDefsOf(internalClassName, methodNode, pops)
        for ((pop, defs) in popWithDefs) {
            if (defs.defs.isEmpty()) continue
            if (defs.defs.all { suspendingCallReturningUnit(it, unitMarks) }) methodNode.instructions.removeAll(arrayListOf(pop, pop.next))
        }
        methodNode.instructions.removeAll(unitMarks)
    }

    private fun suspendingCallReturningUnit(node: AbstractInsnNode, unitMarks: Set<AbstractInsnNode>): Boolean {
        if (node !is MethodInsnNode) return false
        var insn = node.next
        while (insn != null) {
            if (insn.opcode != Opcodes.INVOKESTATIC) return false
            if (insn in unitMarks) return true
            insn = insn.next
        }
        return false
    }

    private fun findDefsOf(internalClassName: String, methodNode: MethodNode, pops: List<AbstractInsnNode>): List<PopWithDefs> {
        val frames = analyze(internalClassName, methodNode, PopsDefsFinder)
        return pops.map { PopWithDefs(it, frames[methodNode.instructions.indexOf(it)].getStack(0) ?: Defs()) }
    }

    private fun findPopAndReturnUnitSequences(methodNode: MethodNode): List<AbstractInsnNode> {
        val res = arrayListOf<AbstractInsnNode>()
        var insn = methodNode.instructions.first
        while (insn != null) {
            if (insn.opcode != Opcodes.POP) {
                insn = insn.next
                continue
            }
            val mustBeGetUnitInstance = insn.next ?: break
            if (mustBeGetUnitInstance.opcode != Opcodes.GETSTATIC) {
                insn = mustBeGetUnitInstance.next
                continue
            }
            val fieldInsnNode = mustBeGetUnitInstance as? FieldInsnNode
            if (fieldInsnNode == null || fieldInsnNode.owner != "kotlin/Unit" || fieldInsnNode.name != "INSTANCE") {
                insn = mustBeGetUnitInstance.next
                continue
            }
            val mustBeAreturn = mustBeGetUnitInstance.next ?: break
            if (mustBeAreturn.opcode == Opcodes.ARETURN) res.add(insn)
            insn = mustBeAreturn.next
        }
        return res
    }

    private fun findReturnsUnitMarks(methodNode: MethodNode): Set<AbstractInsnNode> {
        val res = hashSetOf<AbstractInsnNode>()
        var insn = methodNode.instructions.first
        while(insn != null) {
            if (insn.opcode == Opcodes.INVOKESTATIC && suspendingCallReturningUnitMarker(insn as MethodInsnNode)) res.add(insn)
            insn = insn.next
        }
        return res
    }

    private fun suspendingCallReturningUnitMarker(insn: MethodInsnNode) =
            insn.owner == SUSPEND_CALL_MARKER_CLASS_NAME && insn.name == RETURN_TYPE_IS_UNIT_METHOD_NAME
}

private data class PopWithDefs(val pop: AbstractInsnNode, val defs: Defs)

private data class Defs(val defs: HashSet<AbstractInsnNode> = hashSetOf()): BasicValue(Type.getType(Any::class.java))

private object PopsDefsFinder: Interpreter<Defs>(Opcodes.ASM5) {
    override fun binaryOperation(p0: AbstractInsnNode, p1: Defs?, p2: Defs?) = Defs(hashSetOf(p0))
    override fun unaryOperation(p0: AbstractInsnNode, p1: Defs?) = Defs(hashSetOf(p0))
    override fun copyOperation(p0: AbstractInsnNode?, p1: Defs?) = p1
    override fun returnOperation(p0: AbstractInsnNode?, p1: Defs?, p2: Defs?) {}
    override fun ternaryOperation(p0: AbstractInsnNode, p1: Defs?, p2: Defs?, p3: Defs?) = Defs(hashSetOf(p0))
    override fun newOperation(p0: AbstractInsnNode) = Defs(hashSetOf(p0))
    override fun newValue(p0: Type?) = null
    override fun naryOperation(p0: AbstractInsnNode, p1: MutableList<out Defs>?) = Defs(hashSetOf(p0))

    override fun merge(p0: Defs?, p1: Defs?) = when {
        p0 == null -> p1
        p1 == null -> p0
        else -> {
            p0.defs.addAll(p1.defs)
            p0
        }
    }
}
