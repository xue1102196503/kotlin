// !WITH_NEW_INFERENCE
package f

fun <T> g(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>i<!><!>: Int, <!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>a<!><!>: Any): List<T> {throw Exception()}
fun <T> g(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>a<!><!>: Any, <!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>i<!><!>: Int): Collection<T> {throw Exception()}

fun <T> test() {
    val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>c<!><!>: List<T> = <!NI;OVERLOAD_RESOLUTION_AMBIGUITY!><!CANNOT_COMPLETE_RESOLVE!>g<!><!>(1, 1)
}
