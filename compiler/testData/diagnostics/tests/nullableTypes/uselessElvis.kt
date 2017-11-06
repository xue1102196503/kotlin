// !WITH_NEW_INFERENCE
// !DIAGNOSTICS: -UNUSED_PARAMETER, -SENSELESS_COMPARISON, -DEBUG_INFO_SMARTCAST

fun <T: Any?> test1(t: Any?): Any {
    return t <!NI;UNCHECKED_CAST!><!UNCHECKED_CAST!>as T<!><!> ?: ""
}

fun <T: Any> test2(t: Any?): Any {
    return t <!NI;UNCHECKED_CAST!><!UNCHECKED_CAST!>as T<!><!> <!NI;USELESS_ELVIS!><!USELESS_ELVIS!>?: ""<!><!>
}

fun <T: Any?> test3(t: Any?): Any {
    if (t != null) {
        return t <!NI;USELESS_ELVIS!><!USELESS_ELVIS!>?: ""<!><!>
    }

    return 1
}

fun takeNotNull(s: String) {}
fun <T> notNull(): T = TODO()
fun <T> nullable(): T? = null
fun <T> dependOn(x: T) = x

fun test() {
    takeNotNull(notNull() <!NI;USELESS_ELVIS!><!USELESS_ELVIS!>?: ""<!><!>)
    takeNotNull(nullable() ?: "")

    val x: String? = null
    takeNotNull(dependOn(x) <!NI;USELESS_ELVIS!>?: ""<!>)
    takeNotNull(dependOn(dependOn(x)) <!NI;USELESS_ELVIS!>?: ""<!>)
    takeNotNull(dependOn(dependOn(x as String)) <!NI;USELESS_ELVIS!><!USELESS_ELVIS!>?: ""<!><!>)

    if (x != null) {
        takeNotNull(dependOn(x) <!NI;USELESS_ELVIS!><!USELESS_ELVIS!>?: ""<!><!>)
        takeNotNull(dependOn(dependOn(x)) <!NI;USELESS_ELVIS!><!USELESS_ELVIS!>?: ""<!><!>)
        takeNotNull(dependOn(dependOn(x) as? String) <!NI;USELESS_ELVIS!>?: ""<!>)
    }

    takeNotNull(<!NI;TYPE_MISMATCH!>bar()!!<!>)
}

inline fun <reified T : Any> reifiedNull(): T? = null

fun testFrom13648() {
    takeNotNull(reifiedNull() ?: "")
}

fun bar() = <!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>unresolved<!><!>