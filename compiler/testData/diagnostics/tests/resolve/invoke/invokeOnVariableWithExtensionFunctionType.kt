// !WITH_NEW_INFERENCE
// FILE: 1.kt
package fooIsExtension

class A
class B

val A.foo: B.() -> Unit get() = {}

fun test(a: A, b: B) {
    b.(a.foo)()
    (a.foo)(b)
    a.foo(b)

    with(a) {
        b.foo()

        b.(foo)()

        (b.<!NI;UNRESOLVED_REFERENCE_WRONG_RECEIVER!><!NI;DEBUG_INFO_UNRESOLVED_WITH_TARGET!><!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!><!><!>)()

        foo(b)
        (foo)(b)
    }

    with(b) {
        a.foo(<!NI;NO_VALUE_FOR_PARAMETER!><!NO_VALUE_FOR_PARAMETER!>)<!><!>
        a.<!NI;FUNCTION_EXPECTED!><!FUNCTION_EXPECTED!>(<!NI;UNRESOLVED_REFERENCE_WRONG_RECEIVER!><!NI;DEBUG_INFO_UNRESOLVED_WITH_TARGET!><!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!><!><!>)<!><!>()

        (a.foo)()

        (a.foo)(this)
        a.foo(this)
    }

    with(a) {
        with(b) {
            foo()
            (foo)()
        }
    }
}

// FILE: 1.kt
package fooIsMember

class A {
    val foo: B.() -> Unit get() = {}
}
class B

fun test(a: A, b: B) {
    b.(a.foo)()
    (a.foo)(b)
    a.foo(b)

    with(a) {
        b.foo()

        b.(foo)()

        <!NI;FUNCTION_EXPECTED!><!FUNCTION_EXPECTED!>(b.<!NI;FUNCTION_CALL_EXPECTED!><!FUNCTION_CALL_EXPECTED!>foo<!><!>)<!><!>()

        foo(b)
        (foo)(b)
    }

    with(b) {
        a.foo(<!NI;NO_VALUE_FOR_PARAMETER!><!NO_VALUE_FOR_PARAMETER!>)<!><!>
        a.<!NI;FUNCTION_EXPECTED!><!FUNCTION_EXPECTED!>(<!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>foo<!><!>)<!><!>()

        (a.foo)()

        (a.foo)(this)
        a.foo(this)
    }

    with(a) {
        with(b) {
            foo()
            (foo)()
        }
    }
}
