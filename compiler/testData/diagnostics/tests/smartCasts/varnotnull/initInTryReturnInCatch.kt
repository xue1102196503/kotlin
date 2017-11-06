// !WITH_NEW_INFERENCE
// JAVAC_EXPECTED_FILE
// See also KT-10735
fun test() {
    var a: Int?
    try {
        a = 3
    }
    catch (e: Exception) {
        return
    }
    <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>a<!><!>.hashCode() // a is never null here
}
class A: Exception()
class B: Exception()
fun test2() {
    var a: Int?
    try {
        a = 4
    }
    catch (e: A) {
        return
    }
    catch (e: B) {
        return
    }
    <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>a<!><!>.hashCode() // a is never null here
}
fun test3() {
    var a: Int? = null
    try {
        a = 5
    }
    catch (e: A) {
        // do nothing
    }
    catch (e: B) {
        return
    }
    a<!NI;UNSAFE_CALL!><!UNSAFE_CALL!>.<!><!>hashCode() // a is nullable here
}
fun test4() {
    var a: Int? = null
    try {
        // do nothing
    }
    catch (e: A) {
        return
    }
    catch (e: B) {
        return
    }
    a<!NI;UNSAFE_CALL!><!UNSAFE_CALL!>.<!><!>hashCode() // a is nullable here
}
fun test5() {
    var a: Int?// = null
    try {
        <!NI;UNUSED_VALUE!><!UNUSED_VALUE!>a =<!><!> 3
    }
    catch (e: Exception) {
        return
    }
    finally {
        a = 5
    }
    <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>a<!><!>.hashCode() // a is never null here
}
fun test6() {
    var a: Int?// = null
    try {
        <!NI;UNUSED_VALUE!><!UNUSED_VALUE!>a =<!><!> 3
    }
    catch (e: Exception) {
        return
    }
    finally {
        a = null
    }
    <!DEBUG_INFO_CONSTANT!>a<!><!NI;UNSAFE_CALL!><!UNSAFE_CALL!>.<!><!>hashCode() // a is null here
}
