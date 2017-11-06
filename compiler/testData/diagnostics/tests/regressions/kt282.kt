// !WITH_NEW_INFERENCE
// KT-282 Nullability in extension functions and in binary calls

class Set {
    operator fun contains(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>x<!><!> : Int) : Boolean = true
}

operator fun Set?.plus(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>x<!><!> : Int) : Int = 1

operator fun Int?.contains(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>x<!><!> : Int) : Boolean = false

fun f(): Unit {
    var set : Set? = null
    val i : Int? = null
    i <!NI;UNRESOLVED_REFERENCE_WRONG_RECEIVER!><!NI;DEBUG_INFO_UNRESOLVED_WITH_TARGET!><!UNSAFE_OPERATOR_CALL!>+<!><!><!> 1
    set + 1
    1 <!NI;UNRESOLVED_REFERENCE_WRONG_RECEIVER!><!UNSAFE_OPERATOR_CALL!>in<!><!> set
    1 in 2
}
