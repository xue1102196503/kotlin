// !WITH_NEW_INFERENCE
//KT-1127 Wrong type computed for Arrays.asList()

package d

fun <T> asList(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>t<!><!>: T) : List<T>? {<!NI;NO_RETURN_IN_FUNCTION_WITH_BLOCK_BODY!><!NO_RETURN_IN_FUNCTION_WITH_BLOCK_BODY!>}<!><!>

fun main(args : Array<String>) {
    val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>list<!><!> : List<String> = <!NI;TYPE_MISMATCH!><!TYPE_INFERENCE_EXPECTED_TYPE_MISMATCH!>asList("")<!><!>
}