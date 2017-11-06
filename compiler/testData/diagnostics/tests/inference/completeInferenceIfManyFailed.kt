// !WITH_NEW_INFERENCE
// !CHECK_TYPE

package d

fun <T: Any> joinT(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>x<!><!>: Int, vararg <!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>a<!><!>: T): T? {
    return null
}

fun <T: Any> joinT(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>x<!><!>: Comparable<*>, <!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>y<!><!>: T): T? {
    return null
}

fun test() {
    val x2 = <!TYPE_INFERENCE_PARAMETER_CONSTRAINT_ERROR!>joinT<!>(<!NI;TYPE_MISMATCH!><!TYPE_MISMATCH!>Unit<!><!>, "2")
    checkSubtype<String?>(x2)
}