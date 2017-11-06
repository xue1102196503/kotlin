// !WITH_NEW_INFERENCE
class A() {
    operator infix fun plus(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>i<!><!> : Int) {}
    operator fun unaryMinus() {}
    operator infix fun contains(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>a<!><!> : Any?) : Boolean = true
}

operator infix fun A.div(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>i<!><!> : Int) {}
operator infix fun A?.times(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>i<!><!> : Int) {}

fun test(x : Int?, a : A?) {
    x<!UNSAFE_CALL!>.<!><!NI;UNRESOLVED_REFERENCE_WRONG_RECEIVER!><!NI;DEBUG_INFO_UNRESOLVED_WITH_TARGET!>plus<!><!>(1)
    x?.plus(1)
    x <!NI;UNRESOLVED_REFERENCE_WRONG_RECEIVER!><!NI;DEBUG_INFO_UNRESOLVED_WITH_TARGET!><!UNSAFE_OPERATOR_CALL!>+<!><!><!> 1
    <!NI;UNSAFE_CALL!><!UNSAFE_CALL!>-<!><!>x
    x<!NI;UNSAFE_CALL!><!UNSAFE_CALL!>.<!><!>unaryMinus()
    x?.unaryMinus()

    a<!UNSAFE_CALL!>.<!><!NI;UNRESOLVED_REFERENCE_WRONG_RECEIVER!><!NI;DEBUG_INFO_UNRESOLVED_WITH_TARGET!>plus<!><!>(1)
    a?.plus(1)
    a <!NI;UNSAFE_INFIX_CALL!><!UNSAFE_INFIX_CALL!>plus<!><!> 1
    a <!NI;UNRESOLVED_REFERENCE_WRONG_RECEIVER!><!NI;DEBUG_INFO_UNRESOLVED_WITH_TARGET!><!UNSAFE_OPERATOR_CALL!>+<!><!><!> 1
    <!NI;UNSAFE_CALL!><!UNSAFE_CALL!>-<!><!>a
    a<!NI;UNSAFE_CALL!><!UNSAFE_CALL!>.<!><!>unaryMinus()
    a?.unaryMinus()

    a<!NI;UNSAFE_CALL!><!UNSAFE_CALL!>.<!><!>div(1)
    a <!NI;UNSAFE_OPERATOR_CALL!><!UNSAFE_OPERATOR_CALL!>/<!><!> 1
    a <!NI;UNSAFE_INFIX_CALL!><!UNSAFE_INFIX_CALL!>div<!><!> 1
    a?.div(1)

    a.times(1)
    a * 1
    a times 1
    a?.times(1)

    1 <!NI;UNSAFE_OPERATOR_CALL!><!UNSAFE_OPERATOR_CALL!>in<!><!> a
    a <!NI;UNSAFE_INFIX_CALL!><!UNSAFE_INFIX_CALL!>contains<!><!> 1
    a<!NI;UNSAFE_CALL!><!UNSAFE_CALL!>.<!><!>contains(1)
    a?.contains(1)
}
