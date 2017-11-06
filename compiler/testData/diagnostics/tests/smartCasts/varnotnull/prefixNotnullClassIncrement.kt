// !WITH_NEW_INFERENCE
class MyClass

operator fun MyClass.inc(): MyClass { <!UNREACHABLE_CODE!>return<!> null!! }

public fun box() {
    var i : MyClass?
    i = MyClass()
    // Type of j should be inferred as MyClass?
    var j = ++<!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>i<!><!>
    // j is null so call is unsafe
    j.hashCode()
}