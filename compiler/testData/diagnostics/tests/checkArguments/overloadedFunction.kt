// !WITH_NEW_INFERENCE
// !DIAGNOSTICS: -UNUSED_EXPRESSION -UNUSED_PARAMETER -UNUSED_VARIABLE

fun foo() {}
fun foo(s: Int) {}


fun bar(a: Any) {}
fun bar(a: Int) {}

fun test() {
    <!NI;NONE_APPLICABLE!><!NONE_APPLICABLE!>foo<!><!>(1, 2)
    foo(<!NI;TYPE_MISMATCH!><!TYPE_MISMATCH!>""<!><!>)

    <!NONE_APPLICABLE!>bar<!>(1, <!NI;TOO_MANY_ARGUMENTS!>2<!>)
    <!NI;NONE_APPLICABLE!><!NONE_APPLICABLE!>bar<!><!>()
}