// !WITH_NEW_INFERENCE
// !DIAGNOSTICS: -UNUSED_PARAMETER
// !CHECK_TYPE
interface A<T : A<T?>?> {
    fun foo(): T?
}
fun testA(a: A<*>) {
    a.foo() checkType { <!NI;UNRESOLVED_REFERENCE_WRONG_RECEIVER, NI;DEBUG_INFO_UNRESOLVED_WITH_TARGET!>_<!><A<*>?>() }
}
