// !WITH_NEW_INFERENCE
class MyClass

operator fun MyClass.inc(): MyClass { <!UNREACHABLE_CODE!>return<!> null!! }

public fun box() : MyClass? {
    var i : MyClass?
    i = MyClass()
    // type of j can be inferred as MyClass()
    var j = <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>i<!><!>++
    <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>j<!><!>.hashCode()
    return i
}