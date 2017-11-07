// !WITH_NEW_INFERENCE

import kotlin.collections.map as map1
import kotlin.Array as KotlinArray

fun f() {
    listOf(1).map1 { it.hashCode() }
    listOf(1).<!NI;NONE_APPLICABLE!><!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>map<!><!> { <!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>it<!><!>.<!NI;DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!><!DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!>hashCode<!><!>() }
}

fun g(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>a1<!><!>: KotlinArray<Int>, <!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>a2<!><!>: <!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>Array<!><!><Int>){}