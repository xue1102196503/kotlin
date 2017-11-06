// !WITH_NEW_INFERENCE
fun foo(n: Number) = n

fun test() {
    foo(<!NI;CONSTANT_EXPECTED_TYPE_MISMATCH!><!CONSTANT_EXPECTED_TYPE_MISMATCH!>'a'<!><!>)
    
    val c = 'c'
    foo(<!NI;TYPE_MISMATCH!><!TYPE_MISMATCH!>c<!><!>)

    val d: Char? = 'd'
    foo(<!NI;TYPE_MISMATCH!><!TYPE_MISMATCH!>d<!>!!<!>)
}
