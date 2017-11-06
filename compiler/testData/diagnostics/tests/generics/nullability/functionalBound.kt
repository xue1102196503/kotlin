// !WITH_NEW_INFERENCE
fun <E : String?, T : ((CharSequence) -> Unit)?> foo(x: E, y: T) {
    if (x != null) {
        <!NI;UNSAFE_CALL!><!UNSAFE_IMPLICIT_INVOKE_CALL!>y<!><!>(<!NI;TYPE_MISMATCH!><!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>x<!><!><!>)
    }

    if (y != null) {
        <!NI;UNSAFE_CALL!><!DEBUG_INFO_SMARTCAST!>y<!><!>(<!NI;TYPE_MISMATCH!><!TYPE_MISMATCH!>x<!><!>)
    }

    if (x != null && y != null) {
        <!NI;UNSAFE_CALL!><!DEBUG_INFO_SMARTCAST!>y<!><!>(<!NI;TYPE_MISMATCH!><!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>x<!><!><!>)
    }
}