// !WITH_NEW_INFERENCE
fun <T> test(t: T): T {
    if (t != null) {
        return t<!NI;UNNECESSARY_NOT_NULL_ASSERTION!><!UNNECESSARY_NOT_NULL_ASSERTION!>!!<!><!>
    }
    return <!NI;ALWAYS_NULL!><!ALWAYS_NULL!>t<!><!>!!
}

fun <T> T.testThis(): String {
    if (this != null) {
        return this<!NI;UNNECESSARY_NOT_NULL_ASSERTION!><!UNNECESSARY_NOT_NULL_ASSERTION!>!!<!><!>.toString()
    }
    <!NI;UNREACHABLE_CODE!>return<!> this!!<!NI;UNREACHABLE_CODE!>.toString()<!>
}

