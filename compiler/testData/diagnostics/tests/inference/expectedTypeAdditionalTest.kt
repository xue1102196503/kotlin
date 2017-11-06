// !WITH_NEW_INFERENCE
// !LANGUAGE: +ExpectedTypeFromCast

fun foo() = 1

fun <T> foo() = foo() <!NI;UNCHECKED_CAST!><!UNCHECKED_CAST!>as T<!><!>

fun <T> foo2(): T = TODO()

val test = <!TYPE_INFERENCE_NO_INFORMATION_FOR_PARAMETER!>foo2<!>().<!DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!>plus<!>("") <!NI;USELESS_CAST!>as String<!>

fun <T> T.bar() = this
val barTest = "".bar() <!NI;CAST_NEVER_SUCCEEDS!><!CAST_NEVER_SUCCEEDS!>as<!><!> Number