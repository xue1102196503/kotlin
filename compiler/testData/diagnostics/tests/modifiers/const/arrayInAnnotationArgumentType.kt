// !WITH_NEW_INFERENCE
annotation class A(val a: IntArray = <!NI;TYPE_MISMATCH!><!TYPE_INFERENCE_EXPECTED_TYPE_MISMATCH!>arrayOf(1)<!><!>)
annotation class B(val a: IntArray = intArrayOf(1))