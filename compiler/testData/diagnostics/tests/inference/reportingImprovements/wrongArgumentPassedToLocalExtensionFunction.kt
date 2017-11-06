// !WITH_NEW_INFERENCE
// WITH_RUNTIME

fun Runnable.test(f: Runnable.(Int) -> Unit) {
    f(<!NI;TYPE_MISMATCH!><!TYPE_MISMATCH!>""<!><!>)
}

fun test(f: Runnable.(Int) -> Unit, runnable: Runnable) {
    with (runnable) {
        f(<!NI;TYPE_MISMATCH!><!TYPE_MISMATCH!>""<!><!>)
    }
}

fun Int.test(f: String.(Int) -> Unit) {
    f("", 0)
    <!NI;DEBUG_INFO_MISSING_UNRESOLVED!><!NI;UNRESOLVED_REFERENCE_WRONG_RECEIVER!><!NI;DEBUG_INFO_UNRESOLVED_WITH_TARGET!>f<!><!>(""<!NO_VALUE_FOR_PARAMETER!>)<!><!>
    with("") {
        f(0)
        f(<!NI;CONSTANT_EXPECTED_TYPE_MISMATCH!><!CONSTANT_EXPECTED_TYPE_MISMATCH!>0.0<!><!>)
    }
}