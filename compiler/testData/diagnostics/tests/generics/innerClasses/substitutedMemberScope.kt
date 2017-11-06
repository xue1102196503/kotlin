// !WITH_NEW_INFERENCE
// !CHECK_TYPE
// !DIAGNOSTICS: -UNUSED_EXPRESSION -UNUSED_PARAMETER

class Outer<E> {
    inner class Inner<F> {
        fun instance() = this@Outer
        fun foo(): E = null!!
        fun bar(e: E, f: F) {}
        fun baz(): F = null!!

        fun act() {
            foo().checkType { _<E>() }
            outerE().checkType { _<E>() }
            instance().checkType { _<Outer<E>>() }
            instance().outerE().checkType { _<E>() }

            bar(foo(), baz())
            bar(outerE(), baz())
            bar(instance().outerE(), baz())

            bar(<!NI;TYPE_MISMATCH!>topLevel().Inner<E>().baz()<!>, <!NI;TYPE_MISMATCH!><!TYPE_MISMATCH!>topLevel().Inner<E>().baz()<!><!>)
            bar(<!NI;TYPE_MISMATCH!><!TYPE_MISMATCH!>topLevel().Inner<E>().foo()<!><!>, <!NI;TYPE_MISMATCH!><!TYPE_MISMATCH!>topLevel().Inner<E>().baz()<!><!>)

            setE(foo())
        }
    }

    fun outerE(): E = null!!

    fun setE(e: E) {}
    fun setInner(inner: Inner<Int>) {}
}

fun topLevel(): Outer<String> = null!!

fun foo() {
    val strInt: Outer<String>.Inner<Int> = Outer<String>().Inner()

    strInt.foo().checkType { _<String>() }
    strInt.baz().checkType { _<Int>() }

    strInt.instance().setE("")
    strInt.instance().outerE().checkType { _<String>() }

    strInt.instance().Inner<Double>().checkType { <!NI;UNRESOLVED_REFERENCE_WRONG_RECEIVER!><!NI;DEBUG_INFO_UNRESOLVED_WITH_TARGET!>_<!><!><Outer<String>.Inner<Double>>() }

    Outer<String>().setInner(strInt)
    Outer<CharSequence>().setInner(<!NI;TYPE_MISMATCH!><!TYPE_MISMATCH!>strInt<!><!>)
}
