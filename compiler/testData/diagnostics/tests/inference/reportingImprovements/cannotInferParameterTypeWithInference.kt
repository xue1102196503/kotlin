// !WITH_NEW_INFERENCE
package aa

fun <T, R> foo(block: (T)-> R) = block

fun test1() {
    <!TYPE_INFERENCE_NO_INFORMATION_FOR_PARAMETER!>foo<!> {
        <!NI;UNUSED_ANONYMOUS_PARAMETER!><!CANNOT_INFER_PARAMETER_TYPE!><!UNUSED_ANONYMOUS_PARAMETER!>x<!><!><!> ->  // here we have 'cannot infer parameter type' error
        43
    }
}

fun bar(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>f<!><!>: (<!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>A<!><!>)->Unit) {}

fun test2() {
    bar { <!NI;UNUSED_ANONYMOUS_PARAMETER!><!UNUSED_ANONYMOUS_PARAMETER!>a<!><!> -> } // here we don't have 'cannot infer parameter type' error
}
