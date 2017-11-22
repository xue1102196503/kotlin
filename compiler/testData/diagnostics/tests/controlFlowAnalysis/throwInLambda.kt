// !WITH_NEW_INFERENCE
// See KT-10913 Bogus unreachable code warning

fun fn() : String? = null
fun foo(): String {
    val x = fn()?.let { throw Exception() } ?: "unreachable?"
    return x
}
fun bar(): String {
    val x = fn() ?: return ""
    <!OI;UNREACHABLE_CODE!>val <!OI;UNUSED_VARIABLE!>y<!> =<!> x<!UNNECESSARY_SAFE_CALL!>?.<!>let { throw Exception() } <!OI;USELESS_ELVIS, OI;UNREACHABLE_CODE!>?: "unreachable"<!>
    <!OI;UNREACHABLE_CODE!>return y<!>
}