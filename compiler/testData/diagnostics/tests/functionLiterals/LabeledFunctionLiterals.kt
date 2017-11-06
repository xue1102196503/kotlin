// !WITH_NEW_INFERENCE
package h

//traits to make ambiguity with function literal as an argument
interface A
interface B
interface C: A, B

fun <T> foo(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>a<!><!>: A, f: () -> T): T = f()
fun <T> foo(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>b<!><!>: B, f: () -> T): T = f()

fun test(c: C) {
    <!NI;OVERLOAD_RESOLUTION_AMBIGUITY!><!CANNOT_COMPLETE_RESOLVE!>foo<!><!>(c) f@ {
        c<!NI;UNNECESSARY_NOT_NULL_ASSERTION!><!UNNECESSARY_NOT_NULL_ASSERTION!>!!<!><!>
    }
}
