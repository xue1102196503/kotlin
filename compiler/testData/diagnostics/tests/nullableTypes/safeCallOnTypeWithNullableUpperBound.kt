// !WITH_NEW_INFERENCE
fun <T> test(t: T): String? {
    if (t != null) {
        return t<!NI;UNNECESSARY_SAFE_CALL!><!UNNECESSARY_SAFE_CALL!>?.<!><!>toString()
    }
    return <!NI;DEBUG_INFO_CONSTANT!><!DEBUG_INFO_CONSTANT!>t<!><!>?.toString()
}

fun <T> T.testThis(): String? {
    if (this != null) {
        return this<!NI;UNNECESSARY_SAFE_CALL!><!UNNECESSARY_SAFE_CALL!>?.<!><!>toString()
    }
    return <!DEBUG_INFO_CONSTANT!>this<!>?.toString()
}