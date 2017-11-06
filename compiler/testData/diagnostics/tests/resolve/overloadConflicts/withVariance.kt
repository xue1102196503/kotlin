// !WITH_NEW_INFERENCE
object X1
object X2

class A<T>

<!NI;CONFLICTING_OVERLOADS!><!CONFLICTING_OVERLOADS!>fun <T1> A<T1>.foo()<!><!> = X1
<!NI;CONFLICTING_OVERLOADS!><!CONFLICTING_OVERLOADS!>fun <T2> A<out T2>.foo()<!><!> = X2

fun <T> A<out T>.test() = <!NI;OVERLOAD_RESOLUTION_AMBIGUITY!><!CANNOT_COMPLETE_RESOLVE!>foo<!><!>() // TODO fix constraint system