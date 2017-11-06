// !WITH_NEW_INFERENCE
// !CHECK_TYPE

interface A<R, T: A<R, T>> {
    fun r(): R
    fun t(): T
}

fun testA(a: A<*, *>) {
    a.r().checkType { _<Any?>() }
    a.t().checkType { _<A<*, *>>() }
}

interface B<R, T: B<List<R>, <!NI;UPPER_BOUND_VIOLATED!><!UPPER_BOUND_VIOLATED!>T<!><!>>> {
    fun r(): R
    fun t(): T
}

fun testB(b: B<*, *>) {
    <!TYPE_MISMATCH(B<out Any?, out B<List<*>, *>>; B<*, *>)!>b<!>.r().checkType { _<Any?>() }
    <!TYPE_MISMATCH(B<out Any?, out B<List<*>, *>>; B<*, *>)!>b<!>.t().checkType { <!NI;UNRESOLVED_REFERENCE_WRONG_RECEIVER!><!NI;DEBUG_INFO_UNRESOLVED_WITH_TARGET!>_<!><!><B<List<*>, *>>() }

    <!TYPE_MISMATCH(B<List<Any?>, out B<List<*>, *>>; B<List<*>, *>)!><!TYPE_MISMATCH(B<out Any?, out B<List<*>, *>>; B<*, *>)!>b<!>.t()<!>.r().size
}

