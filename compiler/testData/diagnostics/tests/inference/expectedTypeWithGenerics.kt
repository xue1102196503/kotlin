// !WITH_NEW_INFERENCE
// !LANGUAGE: +ExpectedTypeFromCast

class X<S> {
    fun <T : S> foo(): T = TODO()
}

fun test(x: X<Number>) {
    val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>y<!><!> = x.foo() as Int
}

fun <S, D: S> g() {
    fun <T : S> foo(): T = TODO()

    val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>y<!><!> = <!TYPE_INFERENCE_UPPER_BOUND_VIOLATED!>foo<!>() as Int

    val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>y2<!><!> = foo() <!NI;UNCHECKED_CAST!>as D<!>
}