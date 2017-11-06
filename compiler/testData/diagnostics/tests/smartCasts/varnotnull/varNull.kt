// !WITH_NEW_INFERENCE
fun foo(): Int {
    var s: String? = <!NI;VARIABLE_WITH_REDUNDANT_INITIALIZER!><!VARIABLE_WITH_REDUNDANT_INITIALIZER!>"abc"<!><!>
    s = null
    return <!DEBUG_INFO_CONSTANT!>s<!><!NI;UNSAFE_CALL!><!UNSAFE_CALL!>.<!><!>length
}