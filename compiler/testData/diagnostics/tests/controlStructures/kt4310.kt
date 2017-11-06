// !WITH_NEW_INFERENCE
package f

fun test(a: Boolean, b: Boolean): Int {
    return if(a) {
        1
    } else <!NI;TYPE_MISMATCH!>{
        <!TYPE_MISMATCH!><!NI;INVALID_IF_AS_EXPRESSION!><!INVALID_IF_AS_EXPRESSION!>if<!><!> (b) {
            3
        }<!>
    }<!>
}