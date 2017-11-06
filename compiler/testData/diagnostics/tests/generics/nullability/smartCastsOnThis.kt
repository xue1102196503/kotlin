// !WITH_NEW_INFERENCE
// !DIAGNOSTICS: -UNUSED_EXPRESSION,-UNUSED_VARIABLE

fun <T : CharSequence?> T.bar1() {}
fun CharSequence?.bar2() {}

fun <T : CharSequence> T.bar3() {}
fun CharSequence.bar4() {}

fun <T : String?> T.foo() {
    if (this != null) {
        if (<!NI;SENSELESS_COMPARISON!><!SENSELESS_COMPARISON!>this != null<!><!>) {}

        <!NI;UNSAFE_CALL!>length<!>
        this<!NI;UNNECESSARY_SAFE_CALL!><!UNNECESSARY_SAFE_CALL!>?.<!><!>length

        bar1()
        bar2()
        <!NI;UNSAFE_CALL!><!TYPE_INFERENCE_UPPER_BOUND_VIOLATED!>bar3<!><!>()
        <!NI;UNSAFE_CALL!>bar4<!>()


        this<!NI;UNNECESSARY_SAFE_CALL!><!UNNECESSARY_SAFE_CALL!>?.<!><!>bar1()
    }

    <!NI;UNSAFE_CALL!><!UNSAFE_CALL!>length<!><!>

    if (this is String) {
        <!NI;DEBUG_INFO_IMPLICIT_RECEIVER_SMARTCAST!><!DEBUG_INFO_IMPLICIT_RECEIVER_SMARTCAST!>length<!><!>
        this<!NI;UNNECESSARY_SAFE_CALL!><!UNNECESSARY_SAFE_CALL!>?.<!><!>length

        bar1()
        bar2()
        <!TYPE_INFERENCE_UPPER_BOUND_VIOLATED!>bar3<!>()
    }
}