// !WITH_NEW_INFERENCE
// !DIAGNOSTICS: -UNUSED_EXPRESSION,-UNUSED_VARIABLE

fun <T : CharSequence?> T.bar1() {}
fun CharSequence?.bar2() {}

fun <T : CharSequence> T.bar3() {}
fun CharSequence.bar4() {}

fun <T : CharSequence?> foo(x: T) {

    if (x != null) {
        if (<!NI;SENSELESS_COMPARISON!><!SENSELESS_COMPARISON!>x != null<!><!>) {}

        <!DEBUG_INFO_SMARTCAST!>x<!><!NI;UNSAFE_CALL!>.<!>length
        x<!NI;UNNECESSARY_SAFE_CALL!><!UNNECESSARY_SAFE_CALL!>?.<!><!>length

        x.bar1()
        x.bar2()
        x<!NI;UNSAFE_CALL!>.<!><!TYPE_INFERENCE_UPPER_BOUND_VIOLATED!>bar3<!>()
        <!DEBUG_INFO_SMARTCAST!>x<!><!NI;UNSAFE_CALL!>.<!>bar4()


        x<!NI;UNNECESSARY_SAFE_CALL!><!UNNECESSARY_SAFE_CALL!>?.<!><!>bar1()
    }

    x<!NI;UNSAFE_CALL!><!UNSAFE_CALL!>.<!><!>length

    if (x is String) {
        <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>x<!><!>.length
        x<!NI;UNNECESSARY_SAFE_CALL!><!UNNECESSARY_SAFE_CALL!>?.<!><!>length

        x.bar1()
        x.bar2()
        <!DEBUG_INFO_SMARTCAST!>x<!>.bar3()
    }

    if (x is CharSequence) {
        <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>x<!><!>.length
        x<!NI;UNNECESSARY_SAFE_CALL!><!UNNECESSARY_SAFE_CALL!>?.<!><!>length

        <!NI;DEBUG_INFO_SMARTCAST!>x<!>.bar1()
        x.bar2()
        <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>x<!><!>.bar3()
    }
}
