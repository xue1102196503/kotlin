// !WITH_NEW_INFERENCE
package a

import java.util.*

fun <T> g (<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>f<!><!>: () -> List<T>) : T {<!NI;NO_RETURN_IN_FUNCTION_WITH_BLOCK_BODY!><!NO_RETURN_IN_FUNCTION_WITH_BLOCK_BODY!>}<!><!>

fun test() {
    //here possibly can be a cycle on constraints
    <!NI;UNREACHABLE_CODE!>val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>x<!><!> =<!> g { Collections.<!TYPE_INFERENCE_NO_INFORMATION_FOR_PARAMETER!>emptyList<!>() }

    <!NI;UNREACHABLE_CODE!>val <!UNUSED_VARIABLE!>y<!> = g<Int> { Collections.emptyList() }<!>
    <!NI;UNREACHABLE_CODE!>val <!UNUSED_VARIABLE!>z<!> : List<Int> = g { Collections.<!TYPE_INFERENCE_NO_INFORMATION_FOR_PARAMETER!>emptyList<!>() }<!>
}