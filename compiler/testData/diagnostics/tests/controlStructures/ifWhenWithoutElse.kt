// !WITH_NEW_INFERENCE
fun idAny(x: Any) = x
fun <T> id(x: T) = x
fun idUnit(x: Unit) = x

class MList {
    // MutableCollection<T>.add returns Boolean, but nobody cares
    fun add(): Boolean = true
}
val mlist = MList()

fun work() {}

val xx1 = <!NI;INVALID_IF_AS_EXPRESSION!><!INVALID_IF_AS_EXPRESSION!>if<!><!> (true) 42
val xx2: Unit = <!NI;INVALID_IF_AS_EXPRESSION!><!INVALID_IF_AS_EXPRESSION!>if<!><!> (true) 42
val xx3 = idAny(<!NI;INVALID_IF_AS_EXPRESSION!><!INVALID_IF_AS_EXPRESSION!>if<!><!> (true) 42)
val xx4 = id(<!NI;INVALID_IF_AS_EXPRESSION!><!INVALID_IF_AS_EXPRESSION!>if<!><!> (true) 42)
val xx5 = idUnit(<!NI;INVALID_IF_AS_EXPRESSION!><!INVALID_IF_AS_EXPRESSION!>if<!><!> (true) 42)
val xx6 = null ?: <!NI;INVALID_IF_AS_EXPRESSION!><!INVALID_IF_AS_EXPRESSION!>if<!><!> (true) 42
val xx7 = "" + <!NI;INVALID_IF_AS_EXPRESSION!><!INVALID_IF_AS_EXPRESSION!>if<!><!> (true) 42

val wxx1 = <!NI;NO_ELSE_IN_WHEN!><!NO_ELSE_IN_WHEN!>when<!><!> { true -> 42 }
val wxx2: Unit = <!NI;NO_ELSE_IN_WHEN!><!NO_ELSE_IN_WHEN!>when<!><!> { true -> <!CONSTANT_EXPECTED_TYPE_MISMATCH!>42<!> }
val wxx3 = idAny(<!NI;NO_ELSE_IN_WHEN!><!NO_ELSE_IN_WHEN!>when<!><!> { true -> 42 })
val wxx4 = id(<!NI;NO_ELSE_IN_WHEN!><!NO_ELSE_IN_WHEN!>when<!><!> { true -> 42 })
val wxx5 = idUnit(<!NI;TYPE_MISMATCH!><!NI;NO_ELSE_IN_WHEN!><!NO_ELSE_IN_WHEN!>when<!><!> { true -> <!CONSTANT_EXPECTED_TYPE_MISMATCH!>42<!> }<!>)
val wxx6 = null ?: <!NI;NO_ELSE_IN_WHEN!><!NO_ELSE_IN_WHEN!>when<!><!> { true -> 42 }
val wxx7 = "" + <!NI;NO_ELSE_IN_WHEN!><!NO_ELSE_IN_WHEN!>when<!><!> { true -> 42 }

val fn1 = { if (true) <!NI;UNUSED_EXPRESSION!><!UNUSED_EXPRESSION!>42<!><!> }
val fn2 = { if (true) mlist.add() }
val fn3 = { if (true) work() }
val fn4 = { <!NI;NO_ELSE_IN_WHEN!><!NO_ELSE_IN_WHEN!>when<!><!> { true -> 42 } }
val fn5 = { <!NI;NO_ELSE_IN_WHEN!><!NO_ELSE_IN_WHEN!>when<!><!> { true -> mlist.add() } }
val fn6 = { when { true -> work() } }

val ufn1: () -> Unit = { if (true) <!NI;UNUSED_EXPRESSION!><!UNUSED_EXPRESSION!>42<!><!> }
val ufn2: () -> Unit = { if (true) mlist.add() }
val ufn3: () -> Unit = { if (true) work() }
val ufn4: () -> Unit = { when { true -> <!NI;UNUSED_EXPRESSION!><!UNUSED_EXPRESSION!>42<!><!> } }
val ufn5: () -> Unit = { when { true -> mlist.add() } }
val ufn6: () -> Unit = { when { true -> work() } }

fun f1() = <!NI;INVALID_IF_AS_EXPRESSION!><!INVALID_IF_AS_EXPRESSION!>if<!><!> (true) work()
fun f2() = <!NI;INVALID_IF_AS_EXPRESSION!><!INVALID_IF_AS_EXPRESSION!>if<!><!> (true) mlist.add()
fun f3() = <!NI;INVALID_IF_AS_EXPRESSION!><!INVALID_IF_AS_EXPRESSION!>if<!><!> (true) 42
fun f4(): Unit = <!NI;INVALID_IF_AS_EXPRESSION!><!INVALID_IF_AS_EXPRESSION!>if<!><!> (true) work()
fun f5(): Unit = <!NI;INVALID_IF_AS_EXPRESSION!><!INVALID_IF_AS_EXPRESSION!>if<!><!> (true) mlist.add()
fun f6(): Unit = <!NI;INVALID_IF_AS_EXPRESSION!><!INVALID_IF_AS_EXPRESSION!>if<!><!> (true) 42
fun g1() = <!NI;NO_ELSE_IN_WHEN!><!NO_ELSE_IN_WHEN!>when<!><!> { true -> work() }
fun g2() = <!NI;NO_ELSE_IN_WHEN!><!NO_ELSE_IN_WHEN!>when<!><!> { true -> mlist.add() }
fun g3() = <!NI;NO_ELSE_IN_WHEN!><!NO_ELSE_IN_WHEN!>when<!><!> { true -> 42 }
fun g4(): Unit = <!NI;NO_ELSE_IN_WHEN!><!NO_ELSE_IN_WHEN!>when<!><!> { true -> work() }
fun g5(): Unit = <!NI;NO_ELSE_IN_WHEN!><!NO_ELSE_IN_WHEN!>when<!><!> { true -> <!TYPE_MISMATCH!>mlist.add()<!> }
fun g6(): Unit = <!NI;NO_ELSE_IN_WHEN!><!NO_ELSE_IN_WHEN!>when<!><!> { true -> <!CONSTANT_EXPECTED_TYPE_MISMATCH!>42<!> }

fun foo1(x: String?) {
    "" + <!NI;INVALID_IF_AS_EXPRESSION!><!INVALID_IF_AS_EXPRESSION!>if<!><!> (true) 42
    w@while (true) {
        x ?: if (true) break
        x ?: when { true -> break@w }
    }
}

fun foo2() {
    if (true) {
        mlist.add()
    }
    else if (true) {
        mlist.add()
    }
    else if (true) {
        mlist.add()
    }

    when {
        true -> mlist.add()
        else -> when {
            true -> mlist.add()
            else -> when {
                true -> mlist.add()
            }
        }
    }
}

