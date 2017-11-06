// !WITH_NEW_INFERENCE
fun foo(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>f<!><!>: String.() -> Int) {}
val test = foo(<!NI;TYPE_MISMATCH!><!TYPE_MISMATCH(String.\(\) -> Int; \(\) -> [ERROR : Error function type])!>fun () = <!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>length<!><!><!><!>)