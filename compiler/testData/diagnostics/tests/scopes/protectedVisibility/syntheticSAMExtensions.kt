// !WITH_NEW_INFERENCE
// FILE: abc/A.java
package abc;
public class A {
    protected void foo(Runnable x) {}
}

// FILE: main.kt
import abc.A;

class Data(var x: A)

class B : A() {
    fun baz(a: A, b: B, d: Data) {
        a.<!NI;INVISIBLE_MEMBER!><!INVISIBLE_MEMBER!>foo<!><!> { }

        b.foo { }

        if (a is B) {
            <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>a<!><!>.foo {}
        }

        if (d.x is B) {
            <!NI;SMARTCAST_IMPOSSIBLE!><!SMARTCAST_IMPOSSIBLE!>d.x<!><!>.<!NI;INVISIBLE_MEMBER!>foo<!> {}
        }
    }
}

fun baz(a: A) {
    a.<!NI;INVISIBLE_MEMBER!><!INVISIBLE_MEMBER!>foo<!><!> { }
}
