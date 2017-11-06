// !WITH_NEW_INFERENCE
package a

fun <V: U, U> foo(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>v<!><!>: V, u: U) = u
fun <U, V: U> bar(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>v<!><!>: V, u: U) = u

fun test(a: Any, s: String) {
    val b = foo(a, s)
    checkItIsExactlyAny(a, arrayListOf(b))
    val c = bar(a, s)
    checkItIsExactlyAny(a, arrayListOf(c))
}

fun <T> checkItIsExactlyAny(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>t<!><!>: T, <!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>l<!><!>: MutableList<T>) {}

fun <V : U, U> baz(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>v<!><!>: V, u: MutableSet<U>) = u

fun test(a: Any, s: MutableSet<String>) {
    <!TYPE_INFERENCE_UPPER_BOUND_VIOLATED!>baz<!>(a, <!NI;TYPE_MISMATCH!><!NI;TYPE_MISMATCH!><!NI;TYPE_MISMATCH!>s<!><!><!>)
}

//from standard library
fun <T> arrayListOf(vararg <!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>t<!><!>: T): MutableList<T> {<!NI;NO_RETURN_IN_FUNCTION_WITH_BLOCK_BODY!><!NO_RETURN_IN_FUNCTION_WITH_BLOCK_BODY!>}<!><!>