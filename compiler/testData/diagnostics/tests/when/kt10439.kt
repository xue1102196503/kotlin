// !WITH_NEW_INFERENCE
fun foo(x: Int) = x

fun test0(flag: Boolean) {
    foo(<!NI;TYPE_MISMATCH!><!NI;TYPE_MISMATCH!>if (flag) <!CONSTANT_EXPECTED_TYPE_MISMATCH!>true<!> else <!TYPE_MISMATCH!>""<!><!><!>)
}

fun test1(flag: Boolean) {
    foo(<!NI;TYPE_MISMATCH!><!NI;TYPE_MISMATCH!>when (flag) {
        true -> <!CONSTANT_EXPECTED_TYPE_MISMATCH!>true<!>
        else -> <!TYPE_MISMATCH!>""<!>
    }<!><!>)
}
