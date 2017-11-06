// !WITH_NEW_INFERENCE
package kt1075

//KT-1075 No type check for 'in range' condition in 'when' expression

fun foo(b: String) {
    if (<!NI;TYPE_MISMATCH!><!TYPE_MISMATCH!>b<!><!> in 1..10) {} //type mismatch
    when (<!NI;TYPE_MISMATCH!>b<!>) {
        <!NI;TYPE_MISMATCH_IN_RANGE!><!TYPE_MISMATCH_IN_RANGE!>in<!><!> 1..10 -> <!NI;UNUSED_EXPRESSION!><!UNUSED_EXPRESSION!>1<!><!> //no type mismatch, but it should be here
        else -> <!NI;UNUSED_EXPRESSION!><!UNUSED_EXPRESSION!>2<!><!>
    }
}