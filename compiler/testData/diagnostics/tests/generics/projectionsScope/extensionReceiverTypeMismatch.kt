// !WITH_NEW_INFERENCE
class A<T> {
    fun T.foo() {}
    fun Out<T>.bar() {}
}
class Out<out E>

fun test(x: A<out CharSequence>, y: Out<CharSequence>) {
    with(x) {
        // TODO: this diagnostic could be replaced with TYPE_MISMATCH_DUE_TO_TYPE_PROJECTION
        "".<!NI;DEBUG_INFO_UNRESOLVED_WITH_TARGET, UNRESOLVED_REFERENCE_WRONG_RECEIVER!>foo<!>()
        <!OI;TYPE_MISMATCH_DUE_TO_TYPE_PROJECTIONS!>y<!>.<!NI;UNRESOLVED_REFERENCE_WRONG_RECEIVER, NI;DEBUG_INFO_UNRESOLVED_WITH_TARGET!>bar<!>()

        with(y) {
            <!NI;UNRESOLVED_REFERENCE_WRONG_RECEIVER, NI;DEBUG_INFO_UNRESOLVED_WITH_TARGET, OI;TYPE_MISMATCH_DUE_TO_TYPE_PROJECTIONS!>bar<!>()
        }
    }
}
