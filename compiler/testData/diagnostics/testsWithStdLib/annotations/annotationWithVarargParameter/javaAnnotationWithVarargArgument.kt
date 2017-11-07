// !WITH_NEW_INFERENCE

// FILE: A.java
public @interface A {
    String[] value();
}

// FILE: b.kt
@A(*<!NI;TYPE_MISMATCH!><!TYPE_INFERENCE_EXPECTED_TYPE_MISMATCH(Array<out String>; IGNORE)!>arrayOf(<!NI;CONSTANT_EXPECTED_TYPE_MISMATCH!>1<!>, "b")<!><!>)
fun test() {
}
