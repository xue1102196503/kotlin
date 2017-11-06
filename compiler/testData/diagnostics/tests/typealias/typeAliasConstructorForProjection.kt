// !WITH_NEW_INFERENCE
// NI_EXPECTED_FILE

class C<T>

typealias CStar = C<*>
typealias CIn = C<in Int>
typealias COut = C<out Int>
typealias CT<T> = C<T>

val test1 = <!EXPANDED_TYPE_CANNOT_BE_CONSTRUCTED!>CStar()<!>
val test2 = <!EXPANDED_TYPE_CANNOT_BE_CONSTRUCTED!>CIn()<!>
val test3 = <!EXPANDED_TYPE_CANNOT_BE_CONSTRUCTED!>COut()<!>
val test4 = CT<<!NI;PROJECTION_ON_NON_CLASS_TYPE_ARGUMENT!><!PROJECTION_ON_NON_CLASS_TYPE_ARGUMENT!>*<!><!>>()
val test5 = CT<CT<*>>()