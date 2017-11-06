// !WITH_NEW_INFERENCE
// !DIAGNOSTICS: -UNUSED_PARAMETER

fun <T> foo(t: T) = t

fun test1() {
    foo<!NI;WRONG_NUMBER_OF_TYPE_ARGUMENTS!><!WRONG_NUMBER_OF_TYPE_ARGUMENTS!><Int, String><!><!>(<!TYPE_MISMATCH!>""<!>)
}


fun <T, R> bar(t: T, r: R) {}

fun test2() {
    bar<!NI;WRONG_NUMBER_OF_TYPE_ARGUMENTS!><!WRONG_NUMBER_OF_TYPE_ARGUMENTS!><Int><!><!>(<!TYPE_MISMATCH!>""<!>, "")
}