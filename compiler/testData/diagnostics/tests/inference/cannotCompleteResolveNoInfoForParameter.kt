// !WITH_NEW_INFERENCE
package f

fun <T> f(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>i<!><!>: Int, <!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>c<!><!>: Collection<T>): List<T> {throw Exception()}
fun <T> f(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>a<!><!>: Any, <!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>l<!><!>: List<T>): Collection<T> {throw Exception()}

fun <T> test(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>l<!><!>: List<T>) {
    <!NI;OVERLOAD_RESOLUTION_AMBIGUITY!><!CANNOT_COMPLETE_RESOLVE!>f<!><!>(1, <!TYPE_INFERENCE_NO_INFORMATION_FOR_PARAMETER!>emptyList<!>())
}

fun <T> emptyList(): List<T> {throw Exception()}
