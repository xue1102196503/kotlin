// !WITH_NEW_INFERENCE
fun foo() {
    var v: String? = null
    v<!NI;UNSAFE_CALL!><!UNSAFE_CALL!>.<!><!>length
    v = "abc"
    <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>v<!><!>.length
    v = null
    <!DEBUG_INFO_CONSTANT!>v<!><!NI;UNSAFE_CALL!><!UNSAFE_CALL!>.<!><!>length
    v = "abc"
    <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>v<!><!>.length
}