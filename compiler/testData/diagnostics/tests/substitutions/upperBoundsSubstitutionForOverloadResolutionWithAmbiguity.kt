// !WITH_NEW_INFERENCE
// FILE: foo.kt
package foo

fun <T> f(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>l<!><!>: List<T>) {}

// FILE: bar.kt
package bar

fun <T> f(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>l<!><!>: List<T>) {}

// FILE: main.kt

import foo.*
import bar.*

fun <T> test(l: List<T>) {
    <!NI;OVERLOAD_RESOLUTION_AMBIGUITY!><!CANNOT_COMPLETE_RESOLVE!>f<!><!>(l)
}