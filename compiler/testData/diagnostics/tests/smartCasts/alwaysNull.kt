// !WITH_NEW_INFERENCE
fun foo(): String {
    var s: String?
    s = null
    <!NI;DEBUG_INFO_CONSTANT!><!DEBUG_INFO_CONSTANT!>s<!><!>?.length
    <!DEBUG_INFO_CONSTANT!>s<!><!NI;UNSAFE_CALL!><!UNSAFE_CALL!>.<!><!>length
    if (<!NI;SENSELESS_COMPARISON!><!SENSELESS_COMPARISON!><!NI;DEBUG_INFO_CONSTANT!><!DEBUG_INFO_CONSTANT!>s<!><!> == null<!><!>) return <!NI;ALWAYS_NULL!><!ALWAYS_NULL!>s<!><!>!!
    var t: String? = "y"
    if (t == null) t = "x"
    var x: Int? = null
    if (x == null) <!NI;TYPE_MISMATCH!><!TYPE_MISMATCH!><!DEBUG_INFO_CONSTANT!>x<!> += null<!><!>
    return <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>t<!><!> + s
}

fun String?.gav() {}

fun bar(s: String?) {
    if (s != null) return
    <!DEBUG_INFO_CONSTANT!>s<!>.gav()
    <!NI;DEBUG_INFO_CONSTANT!><!DEBUG_INFO_CONSTANT!>s<!><!> <!NI;USELESS_CAST!><!USELESS_CAST!>as? String<!><!>
    <!NI;DEBUG_INFO_CONSTANT!><!DEBUG_INFO_CONSTANT!>s<!><!> <!NI;USELESS_CAST!><!USELESS_CAST!>as String?<!><!>
    <!NI;ALWAYS_NULL!><!ALWAYS_NULL!>s<!><!> as String
}