// !WITH_NEW_INFERENCE
fun foo(x: String?, y: String?, z: String?, w: String?) {
    if (x != null && y != null && (x == z || y == z))
        <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>z<!><!>.length
    else
        z<!NI;UNSAFE_CALL!><!UNSAFE_CALL!>.<!><!>length
    if (x != null || y != null || (<!NI;DEBUG_INFO_CONSTANT!><!DEBUG_INFO_CONSTANT!>x<!><!> != z && <!NI;DEBUG_INFO_CONSTANT!><!DEBUG_INFO_CONSTANT!>y<!><!> != z))
        z<!NI;UNSAFE_CALL!><!UNSAFE_CALL!>.<!><!>length
    else
        <!DEBUG_INFO_CONSTANT!>z<!><!NI;UNSAFE_CALL!><!UNSAFE_CALL!>.<!><!>length
    if (x == null || y == null || (x != z && y != z))
        z<!NI;UNSAFE_CALL!><!UNSAFE_CALL!>.<!><!>length
    else
        <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>z<!><!>.length
    if (x != null && y == x && z == y && w == z)
        <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>w<!><!>.length
    else
        w<!NI;UNSAFE_CALL!><!UNSAFE_CALL!>.<!><!>length
    if ((x != null && y == x) || (z != null && y == z))
        <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>y<!><!>.length
    else
        y<!NI;UNSAFE_CALL!><!UNSAFE_CALL!>.<!><!>length
}