// !WITH_NEW_INFERENCE
fun foo(x : String?, y : String?) {
    if (y != null && x == y) {
        // Both not null
        <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>x<!><!>.length
        <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>y<!><!>.length
    }
    else {
        x<!NI;UNSAFE_CALL!><!UNSAFE_CALL!>.<!><!>length
        y<!NI;UNSAFE_CALL!><!UNSAFE_CALL!>.<!><!>length
    }
    if (y != null || x == <!NI;DEBUG_INFO_CONSTANT!><!DEBUG_INFO_CONSTANT!>y<!><!>) {
        x<!NI;UNSAFE_CALL!><!UNSAFE_CALL!>.<!><!>length
        y<!NI;UNSAFE_CALL!><!UNSAFE_CALL!>.<!><!>length
    }
    else {
        // y == null but x != y
        <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>x<!><!>.length
        <!DEBUG_INFO_CONSTANT!>y<!><!NI;UNSAFE_CALL!><!UNSAFE_CALL!>.<!><!>length
    }
    if (y == null && x != <!NI;DEBUG_INFO_CONSTANT!><!DEBUG_INFO_CONSTANT!>y<!><!>) {
        // y == null but x != y
        <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>x<!><!>.length
        <!DEBUG_INFO_CONSTANT!>y<!><!NI;UNSAFE_CALL!><!UNSAFE_CALL!>.<!><!>length
    }
    else {
        x<!NI;UNSAFE_CALL!><!UNSAFE_CALL!>.<!><!>length
        y<!NI;UNSAFE_CALL!><!UNSAFE_CALL!>.<!><!>length
    }
    if (y == null || x != y) {
        x<!NI;UNSAFE_CALL!><!UNSAFE_CALL!>.<!><!>length
        y<!NI;UNSAFE_CALL!><!UNSAFE_CALL!>.<!><!>length
    }
    else {
        // Both not null
        <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>x<!><!>.length
        <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>y<!><!>.length
    }
}