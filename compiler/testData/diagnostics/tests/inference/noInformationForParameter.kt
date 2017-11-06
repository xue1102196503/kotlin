// !WITH_NEW_INFERENCE
package noInformationForParameter
//+JDK

import java.util.*

fun test() {
    val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>n<!><!> = <!TYPE_INFERENCE_NO_INFORMATION_FOR_PARAMETER!>newList<!>()

    val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>n1<!><!> : List<String> = newList()
}

fun <S> newList() : ArrayList<S> {
    return ArrayList<S>()
}