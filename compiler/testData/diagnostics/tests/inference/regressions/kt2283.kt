// !WITH_NEW_INFERENCE
//KT-2283 Bad diagnostics of failed type inference
package a


interface Foo<A>

fun <A, B> Foo<A>.map(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>f<!><!>: (A) -> B): Foo<B> = object : Foo<B> {}


fun foo() {
    val l: Foo<String> = object : Foo<String> {}
    val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>m<!><!>: Foo<String> = l.<!TYPE_INFERENCE_EXPECTED_TYPE_MISMATCH!>map { <!NI;UNUSED_ANONYMOUS_PARAMETER!><!UNUSED_ANONYMOUS_PARAMETER!>ppp<!><!> -> <!NI;CONSTANT_EXPECTED_TYPE_MISMATCH!>1<!> }<!>
}
