// !WITH_NEW_INFERENCE
class A(val x: (String.() -> Unit)?)

fun test(a: A) {
    if (a.x != null) {
        "".<!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>(a.x)<!><!>()
        a.<!NI;UNSAFE_CALL!><!UNSAFE_IMPLICIT_INVOKE_CALL!>x<!><!>("") // todo
        <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>(a.x)<!><!>("")
    }
    "".<!NI;UNSAFE_IMPLICIT_INVOKE_CALL!><!UNSAFE_IMPLICIT_INVOKE_CALL!>(a.x)<!><!>()
    a.<!NI;UNSAFE_CALL!><!UNSAFE_IMPLICIT_INVOKE_CALL!>x<!><!>("")
    <!NI;UNSAFE_IMPLICIT_INVOKE_CALL!><!UNSAFE_IMPLICIT_INVOKE_CALL!>(a.x)<!><!>("")

    with("") {
        a.<!NI;UNSAFE_CALL!><!UNSAFE_IMPLICIT_INVOKE_CALL!>x<!><!>(<!NI;NO_VALUE_FOR_PARAMETER!><!NO_VALUE_FOR_PARAMETER!>)<!><!>
        <!NI;UNSAFE_IMPLICIT_INVOKE_CALL!><!UNSAFE_IMPLICIT_INVOKE_CALL!>(a.x)<!><!>()
        if (a.x != null) {
            a.<!NI;UNSAFE_CALL!><!UNSAFE_IMPLICIT_INVOKE_CALL!>x<!><!>(<!NI;NO_VALUE_FOR_PARAMETER!><!NO_VALUE_FOR_PARAMETER!>)<!><!> // todo
            <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>(a.x)<!><!>()
        }
    }
}
