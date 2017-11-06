// !WITH_NEW_INFERENCE
// !DIAGNOSTICS: -UNUSED_PARAMETER

fun foo(x: String) {}
fun foo(x: Int) {}
fun foo(x: Int, y: String) {}

fun bar(nullX: Int?, nullY: String?, notNullY: String) {
    <!NI;NONE_APPLICABLE!>foo<!>(<!TYPE_MISMATCH!>nullX<!>)
    foo(<!NI;TYPE_MISMATCH!><!TYPE_MISMATCH!>nullX<!><!>, notNullY)
    foo(<!NI;TYPE_MISMATCH!><!TYPE_MISMATCH!>nullX<!><!>, <!NI;TYPE_MISMATCH!><!TYPE_MISMATCH!>nullY<!><!>)
    <!NI;NONE_APPLICABLE!><!NONE_APPLICABLE!>foo<!><!>()
}