// !WITH_NEW_INFERENCE
//KT-2164 !! does not propagate nullability information
package kt2164

fun foo(x: Int): Int = x + 1

fun main(args : Array<String>) {
    val x: Int? = null

    foo(<!NI;TYPE_MISMATCH!><!TYPE_MISMATCH!>x<!><!>)

    if (x != null) {
        foo(<!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>x<!><!>)
        foo(x<!NI;UNNECESSARY_NOT_NULL_ASSERTION!><!UNNECESSARY_NOT_NULL_ASSERTION!>!!<!><!>)
        foo(<!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>x<!><!>)
    }

    foo(<!NI;TYPE_MISMATCH!><!TYPE_MISMATCH!>x<!><!>)

    if (x != null) {
        foo(<!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>x<!><!>)
        foo(x<!NI;UNNECESSARY_NOT_NULL_ASSERTION!><!UNNECESSARY_NOT_NULL_ASSERTION!>!!<!><!>)
        foo(<!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>x<!><!>)
    } else {
        foo(<!NI;TYPE_MISMATCH!><!NI;DEBUG_INFO_CONSTANT!><!TYPE_MISMATCH!><!DEBUG_INFO_CONSTANT!>x<!><!><!><!>)
        <!UNREACHABLE_CODE!>foo(<!><!NI;ALWAYS_NULL!><!ALWAYS_NULL!>x<!><!>!!<!UNREACHABLE_CODE!>)<!>
        <!UNREACHABLE_CODE!>foo(<!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>x<!><!>)<!>
    }

    foo(<!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>x<!><!>)
    foo(x<!NI;UNNECESSARY_NOT_NULL_ASSERTION!><!UNNECESSARY_NOT_NULL_ASSERTION!>!!<!><!>)
    foo(<!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>x<!><!>)
    
    val y: Int? = null
    y!!
    y<!NI;UNNECESSARY_NOT_NULL_ASSERTION!><!UNNECESSARY_NOT_NULL_ASSERTION!>!!<!><!>
    foo(<!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>y<!><!>)
    foo(y<!NI;UNNECESSARY_NOT_NULL_ASSERTION!><!UNNECESSARY_NOT_NULL_ASSERTION!>!!<!><!>)
}
