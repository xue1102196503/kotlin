// !WITH_NEW_INFERENCE
fun text() {
    "direct:a" to "mock:a"
    "direct:a" on {it.body == "<hello/>"} to "mock:a"
    "direct:a" on {it -> it.body == "<hello/>"} to "mock:a"
    bar <!NI;TYPE_MISMATCH!><!NI;EXPECTED_PARAMETERS_NUMBER_MISMATCH!><!EXPECTED_PARAMETERS_NUMBER_MISMATCH!>{<!><!>1}<!>
    bar <!NI;TYPE_MISMATCH!><!NI;EXPECTED_PARAMETERS_NUMBER_MISMATCH!><!EXPECTED_PARAMETERS_NUMBER_MISMATCH!>{<!><!><!NI;TYPE_MISMATCH!><!NI;TYPE_MISMATCH!><!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>it<!><!> <!NI;DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!><!DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!>+<!><!> 1<!><!>}<!>
    bar {it, <!NI;UNUSED_ANONYMOUS_PARAMETER!><!UNUSED_ANONYMOUS_PARAMETER!>it1<!><!> -> it}

    bar1 {1}
    bar1 {it + 1}

    bar2 {<!NI;TYPE_MISMATCH!><!><!TYPE_MISMATCH!><!>}
    bar2 {1}
    bar2 {<!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>it<!><!>}
    bar2 <!NI;TYPE_MISMATCH!>{<!EXPECTED_PARAMETERS_NUMBER_MISMATCH!><!CANNOT_INFER_PARAMETER_TYPE!>it<!><!> -> <!NI;TYPE_MISMATCH!><!NI;TYPE_MISMATCH!><!DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!>it<!><!><!>}<!>
}

fun bar(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>f<!><!> :  (Int, Int) -> Int) {}
fun bar1(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>f<!><!> :  (Int) -> Int) {}
fun bar2(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>f<!><!> :  () -> Int) {}

infix fun String.to(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>dest<!><!> : String) {

}

infix fun String.on(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>predicate<!><!> :  (s : URI) -> Boolean) : URI {
    return URI(this)
}

class URI(val body : Any) {
    infix fun to(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>dest<!><!> : String) {}
}
