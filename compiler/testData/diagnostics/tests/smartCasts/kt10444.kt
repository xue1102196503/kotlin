// !WITH_NEW_INFERENCE
// !DIAGNOSTICS: -UNUSED_PARAMETER
// KT-10444 Do not ignore smart (unchecked) casts to the same classifier

class Qwe<T : Any>(val a: T?) {
    fun test1(obj: Any) {
        obj <!NI;UNCHECKED_CAST!><!UNCHECKED_CAST!>as Qwe<T><!><!>
        check(<!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>obj<!><!>.a)
    }

    fun test1(obj: Qwe<*>) {
        obj <!NI;UNCHECKED_CAST!><!UNCHECKED_CAST!>as Qwe<T><!><!>
        check(<!DEBUG_INFO_SMARTCAST!>obj<!>.a)
    }

    fun check(a: T?) {
    }
}

open class Foo
open class Bar<T: Foo>(open val a: T?, open val b: T?) {
    @Suppress("UNCHECKED_CAST")
    fun compare(obj: Any) {
        if (obj !is Bar<*>) {
            throw IllegalArgumentException()
        }
        if (System.currentTimeMillis() > 100) {
            val b = (obj as Bar<T>).b
            if (b == null) throw IllegalArgumentException()
            check(<!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>obj<!><!>.a, <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>b<!><!>)
        }
    }
    fun check(a: T?, b: T) {
    }
}
