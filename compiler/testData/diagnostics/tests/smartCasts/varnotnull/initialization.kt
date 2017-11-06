// !WITH_NEW_INFERENCE
fun foo() {
    var v: String? = "xyz"
    // It is possible in principle to provide smart cast here
    v<!NI;UNSAFE_CALL!><!UNSAFE_CALL!>.<!><!>length
    v = null
    <!DEBUG_INFO_CONSTANT!>v<!><!NI;UNSAFE_CALL!><!UNSAFE_CALL!>.<!><!>length
}