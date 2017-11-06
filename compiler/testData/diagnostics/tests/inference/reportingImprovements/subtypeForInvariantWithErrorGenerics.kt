// !WITH_NEW_INFERENCE
package a

fun <R> foo (f: ()->R, r: MutableList<R>) = r.add(f())
fun <R> bar (r: MutableList<R>, f: ()->R) = r.add(f())

fun test() {
    val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>a<!><!> = <!TYPE_INFERENCE_CONFLICTING_SUBSTITUTIONS!>foo<!>({1}, arrayListOf("")) //no type inference error on 'arrayListOf'
    val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>b<!><!> = <!TYPE_INFERENCE_CONFLICTING_SUBSTITUTIONS!>bar<!>(arrayListOf(""), {1})
}

// from standard library
fun <T> arrayListOf(vararg <!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>values<!><!>: T) : MutableList<T> {<!NI;NO_RETURN_IN_FUNCTION_WITH_BLOCK_BODY!><!NO_RETURN_IN_FUNCTION_WITH_BLOCK_BODY!>}<!><!>