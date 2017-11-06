// !WITH_NEW_INFERENCE
//KT-13330 AssertionError: Illegal resolved call to variable with invoke

fun foo(exec: (String.() -> Unit)?) = ""<!NI;UNSAFE_CALL!>.<!><!UNSAFE_IMPLICIT_INVOKE_CALL!>exec<!><!NI;WRONG_NUMBER_OF_TYPE_ARGUMENTS!><!WRONG_NUMBER_OF_TYPE_ARGUMENTS!><<!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>caret<!><!>><!><!>() // <caret> is test data tag here