// !WITH_NEW_INFERENCE
// NI_EXPECTED_FILE
// KT-6822 Smart cast doesn't work inside local returned expression in lambda 

val a /* :(Int?) -> Int? */ = l@ { it: Int? -> // but must be (Int?) -> Int
    if (it != null) return@l it
    5
}

fun <R> let(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>f<!><!>: (Int?) -> R): R = null!!

val b /*: Int? */ = let { // but must be Int
    if (it != null) return@let <!NI;DEBUG_INFO_SMARTCAST!>it<!>
    5
}

val c /*: Int*/ = let {
    if (it != null) <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>it<!><!> else 5
}