// !WITH_NEW_INFERENCE
// !CHECK_TYPE
// !DIAGNOSTICS: -UNUSED_PARAMETER, -UNUSED_VARIABLE

class TestClass {
    companion object {
        inline operator fun <T> invoke(task: () -> T) = task()
    }
}

fun test(s: String): String {
    val a = TestClass { "K" }
    a checkType { _<String>() }

    <!UNREACHABLE_CODE!>val b =<!> TestClass { return s }
    <!UNREACHABLE_CODE!>b checkType { <!NI;UNRESOLVED_REFERENCE_WRONG_RECEIVER, NI;DEBUG_INFO_UNRESOLVED_WITH_TARGET!>_<!><Nothing>() }<!>
}