// !WITH_NEW_INFERENCE
// See KT-8277

val v = { true } <!NI;USELESS_ELVIS!><!USELESS_ELVIS!>?: ( { true } <!NI;USELESS_ELVIS_ON_LAMBDA_EXPRESSION!><!USELESS_ELVIS_ON_LAMBDA_EXPRESSION!>?:<!><!> null!! )<!><!>

val w = if (true) {
    { true }
}
else {
    { true } <!NI;USELESS_ELVIS_ON_LAMBDA_EXPRESSION!><!USELESS_ELVIS_ON_LAMBDA_EXPRESSION!>?:<!><!> null!!
}

val ww = if (true) {
    <!TYPE_MISMATCH!>{ true }<!> <!NI;USELESS_ELVIS_ON_LAMBDA_EXPRESSION!><!USELESS_ELVIS_ON_LAMBDA_EXPRESSION!>?:<!><!> null!!
}
else if (true) {
    <!TYPE_MISMATCH!>{ true }<!> <!NI;USELESS_ELVIS_ON_LAMBDA_EXPRESSION!><!USELESS_ELVIS_ON_LAMBDA_EXPRESSION!>?:<!><!> null!!
}
else {
    null!!
}

val <!IMPLICIT_NOTHING_PROPERTY_TYPE!>n<!> = null ?: (null ?: <!TYPE_MISMATCH!>{ true }<!>)

fun l(): (() -> Boolean)? = null

val b = null ?: ( l() ?: false)

val bb = null ?: ( l() ?: null!!)

val bbb = null ?: ( l() <!NI;USELESS_ELVIS_RIGHT_IS_NULL!><!USELESS_ELVIS_RIGHT_IS_NULL!>?: null<!><!>)

val bbbb = ( l() <!NI;USELESS_ELVIS_RIGHT_IS_NULL!><!USELESS_ELVIS_RIGHT_IS_NULL!>?: null<!><!>) ?: ( l() <!NI;USELESS_ELVIS_RIGHT_IS_NULL!><!USELESS_ELVIS_RIGHT_IS_NULL!>?: null<!><!>)

fun f(x : Long?): Long {
    var a = x ?: (<!NI;TYPE_MISMATCH!><!TYPE_MISMATCH!>fun() {}<!><!> <!USELESS_ELVIS!><!NI;USELESS_ELVIS_ON_LAMBDA_EXPRESSION!>?:<!> <!NI;TYPE_MISMATCH!><!TYPE_MISMATCH!>fun() {}<!><!><!>)
    return <!DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!>a<!>
}
