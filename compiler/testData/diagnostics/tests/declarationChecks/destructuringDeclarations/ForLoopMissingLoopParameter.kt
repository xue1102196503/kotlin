// !WITH_NEW_INFERENCE
fun useDeclaredVariables() {
    for ((a, b)<!NI;SYNTAX!><!><!SYNTAX!><!>) {
        <!NI;UNUSED_EXPRESSION!><!UNUSED_EXPRESSION!><!DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!>a<!><!><!>
        <!NI;UNUSED_EXPRESSION!><!UNUSED_EXPRESSION!><!DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!>b<!><!><!>
    }
}

fun checkersShouldRun() {
    for ((@A <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>a<!><!>, _)<!NI;SYNTAX!><!><!SYNTAX!><!>) {

    }
}

annotation class A
