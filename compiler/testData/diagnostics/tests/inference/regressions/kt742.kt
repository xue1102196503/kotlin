// !WITH_NEW_INFERENCE
//KT-742 Stack overflow in type inference
package a

fun <T : Any> T?.sure() : T = this!!

class List<T>(val head: T, val tail: List<T>? = null)

fun <T, Q> List<T>.map1(f: (T)-> Q): List<T>? = tail!!.map1(f)

fun <T, Q> List<T>.map2(f: (T)-> Q): List<T>? = tail.sure().map2(f)

fun <T, Q> List<T>.map3(f: (T)-> Q): List<T>? = <!NI;TYPE_MISMATCH!><!TYPE_MISMATCH!>tail<!>.<!NI;UNRESOLVED_REFERENCE_WRONG_RECEIVER!><!NI;DEBUG_INFO_UNRESOLVED_WITH_TARGET!>sure<!><!><<!UPPER_BOUND_VIOLATED!>T<!>>().<!NI;DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!><!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>map3<!><!>(f)<!>