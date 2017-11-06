// !WITH_NEW_INFERENCE
//KT-352 Function variable declaration type isn't checked inside a function body

package kt352

val f : (Any) -> Unit = {  <!NI;EXPECTED_PARAMETERS_NUMBER_MISMATCH!><!><!EXPECTED_PARAMETERS_NUMBER_MISMATCH!><!>-> }  //type mismatch

fun foo() {
    val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>f<!><!> : (Any) -> Unit = { <!NI;EXPECTED_PARAMETERS_NUMBER_MISMATCH!><!><!EXPECTED_PARAMETERS_NUMBER_MISMATCH!><!>-> }  //!!! no error
}

class A() {
    val f : (Any) -> Unit = { <!NI;EXPECTED_PARAMETERS_NUMBER_MISMATCH!><!><!EXPECTED_PARAMETERS_NUMBER_MISMATCH!><!>-> }  //type mismatch
}

//more tests
val g : () -> Unit = { <!NI;UNUSED_EXPRESSION!><!UNUSED_EXPRESSION!>42<!><!> }
val gFunction : () -> Unit = <!TYPE_MISMATCH!>fun(): Int = 1<!>

val h : () -> Unit = { doSmth() }

fun doSmth(): Int = 42
fun doSmth(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>a<!><!>: String) {}

val testIt : (Any) -> Unit = {
    if (it is String) {
        doSmth(<!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>it<!><!>)
    }
}