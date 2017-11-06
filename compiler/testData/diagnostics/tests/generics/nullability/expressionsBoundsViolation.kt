// !WITH_NEW_INFERENCE
// !DIAGNOSTICS: -UNUSED_PARAMETER

class A<T : CharSequence>(x: T)

fun <E : CharSequence> foo1(x: E) {}
fun <E : CharSequence> E.foo2() {}

fun <F : String?> bar(x: F) {
    <!TYPE_INFERENCE_UPPER_BOUND_VIOLATED!>A<!>(<!NI;TYPE_MISMATCH!>x<!>)
    A<<!NI;UPPER_BOUND_VIOLATED!><!UPPER_BOUND_VIOLATED!>F<!><!>>(x)

    <!TYPE_INFERENCE_UPPER_BOUND_VIOLATED!>foo1<!>(<!NI;TYPE_MISMATCH!>x<!>)
    foo1<<!NI;UPPER_BOUND_VIOLATED!><!UPPER_BOUND_VIOLATED!>F<!><!>>(x)

    x<!NI;UNSAFE_CALL!>.<!><!TYPE_INFERENCE_UPPER_BOUND_VIOLATED!>foo2<!>()
    x.foo2<<!NI;UPPER_BOUND_VIOLATED!><!UPPER_BOUND_VIOLATED!>F<!><!>>()
}

