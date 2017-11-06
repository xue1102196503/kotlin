// !WITH_NEW_INFERENCE
package uninitialized_reassigned_variables

fun doSmth(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>s<!><!>: String) {}
fun doSmth(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>i<!><!>: Int) {}

// ------------------------------------------------
// uninitialized variables

fun t1(b : Boolean) {
    val v : Int
    if (<!NI;UNINITIALIZED_VARIABLE!><!UNINITIALIZED_VARIABLE!>v<!><!> == 0) {}

    var u: String
    if (b) {
        u = "s"
    }
    doSmth(<!NI;UNINITIALIZED_VARIABLE!><!UNINITIALIZED_VARIABLE!>u<!><!>)

    var r: String
    if (b) {
        r = "s"
    }
    else {
        r = "tg"
    }
    doSmth(r)

    var t: String
    if (b)
        doSmth(<!NI;UNINITIALIZED_VARIABLE!><!UNINITIALIZED_VARIABLE!>t<!><!>)
    else
        t = "ss"
    doSmth(t) //repeat for t

    val i = 3
    doSmth(i)
    if (b) {
        return;
    }
    doSmth(i)
    if (<!NI;USELESS_IS_CHECK!><!USELESS_IS_CHECK!>i is Int<!><!>) {
        return;
    }
}

fun t2() {
    val s = "ss"

    for (i in 0..2) {
        doSmth(s)
    }
}

class A() {}

fun t4(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>a<!><!>: A) {
    <!NI;UNUSED_VALUE!><!UNUSED_VALUE!><!NI;VAL_REASSIGNMENT!><!VAL_REASSIGNMENT!>a<!><!> =<!><!> A()
}

// ------------------------------------------------
// reassigned vals

fun t1() {
    val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>a<!><!> : Int = 1
    <!NI;UNUSED_VALUE!><!UNUSED_VALUE!><!NI;VAL_REASSIGNMENT!><!VAL_REASSIGNMENT!>a<!><!> =<!><!> 2

    var <!NI;ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE!><!ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE!>b<!><!> : Int = 1
    <!NI;UNUSED_VALUE!><!UNUSED_VALUE!>b =<!><!> 3
}

enum class ProtocolState {
  WAITING {
    override fun signal() = ProtocolState.TALKING
  },

  TALKING {
    override fun signal() = ProtocolState.WAITING
  };

  abstract fun signal() : ProtocolState
}

fun t3() {
   val x: ProtocolState = ProtocolState.WAITING
   <!NI;VAL_REASSIGNMENT!><!VAL_REASSIGNMENT!>x<!><!> = x.signal()
   x = x.signal() //repeat for x
}

fun t4() {
    val x = 1
    <!NI;VAL_REASSIGNMENT!><!VAL_REASSIGNMENT!>x<!><!> += 2
    val y = 3
    <!NI;VAL_REASSIGNMENT!><!VAL_REASSIGNMENT!>y<!><!> *= 4
    var z = 5
    z -= y
}

fun t5() {
    for (i in 0..2) {
        <!NI;VAL_REASSIGNMENT!><!VAL_REASSIGNMENT!>i<!><!> += 1
        fun t5() {
            i += 3
        }
    }
}

// ------------------------------------------------
// backing fields

var x = 10
val y = 10
val z = 10

class AnonymousInitializers(var a: String, val b: String) {
    init {
        a = "30"
        a = "s"

        <!NI;VAL_REASSIGNMENT!><!VAL_REASSIGNMENT!>b<!><!> = "3"
        b = "tt" //repeat for b
    }

    val i: Int
    init {
        i = 121
    }

    init {
        x = 11
        <!NI;VAL_REASSIGNMENT!><!VAL_REASSIGNMENT!>z<!><!> = 10
    }

    val j: Int
    get() = 20

    init {
        <!NI;VAL_REASSIGNMENT!><!VAL_REASSIGNMENT!>i<!><!> = 13
        <!NI;VAL_REASSIGNMENT!><!VAL_REASSIGNMENT!>j<!><!> = 34
    }

    val k: String
    init {
        if (1 < 3) {
            k = "a"
        }
        else {
            k = "b"
        }
    }

    val l: String
    init {
        if (1 < 3) {
            l = "a"
        }
        else {
            l = "b"
        }
    }

    <!NI;MUST_BE_INITIALIZED_OR_BE_ABSTRACT!><!MUST_BE_INITIALIZED_OR_BE_ABSTRACT!>val o: String<!><!>
    init {
        if (1 < 3) {
            o = "a"
        }
    }

    var m: Int = 30

    init {
        m = 400
    }

    val n: Int

    init {
        while (<!NI;UNINITIALIZED_VARIABLE!><!UNINITIALIZED_VARIABLE!>n<!><!> == 0) {
        }
        n = 10
        while (n == 0) {
        }
    }

    var p = 1
    init {
        p++
    }
}

fun reassignFunParams(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>a<!><!>: Int) {
    <!NI;UNUSED_VALUE!><!UNUSED_VALUE!><!NI;VAL_REASSIGNMENT!><!VAL_REASSIGNMENT!>a<!><!> =<!><!> 1
}

open class Open(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>a<!><!>: Int, <!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>w<!><!>: Int) {}

class LocalValsVsProperties(val a: Int, w: Int) : Open(a, w) {
    val x : Int
    <!NI;MUST_BE_INITIALIZED_OR_BE_ABSTRACT!><!MUST_BE_INITIALIZED_OR_BE_ABSTRACT!>val y : Int<!><!>
    init {
        x = 1
        val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>b<!><!> = x
    }
    val b = a

    fun foo() {
        val r : Int
        doSmth(x)
        doSmth(y)
        doSmth(<!NI;UNINITIALIZED_VARIABLE!><!UNINITIALIZED_VARIABLE!>r<!><!>)
        doSmth(a)
    }
    var xx = w
    var yy : Int
    init {
        <!NI;VAL_REASSIGNMENT!><!VAL_REASSIGNMENT!>w<!><!> += 1
        yy = w
    }
}

class Outer() {
    val a : Int
    var b : Int

    init {
        a = 1
        b = 1
    }

    inner class Inner() {
        init {
            <!NI;VAL_REASSIGNMENT!><!VAL_REASSIGNMENT!>a<!><!>++
            b++
        }
    }

    fun foo() {
        <!NI;VAL_REASSIGNMENT!><!VAL_REASSIGNMENT!>a<!><!>++
        b++
    }
}

class ForwardAccessToBackingField() { //kt-147
    val a = <!NI;TYPECHECKER_HAS_RUN_INTO_RECURSIVE_PROBLEM!><!NI;DEBUG_INFO_MISSING_UNRESOLVED!><!TYPECHECKER_HAS_RUN_INTO_RECURSIVE_PROBLEM!><!UNINITIALIZED_VARIABLE!>a<!><!><!><!> // error
    val b = <!NI;UNINITIALIZED_VARIABLE!><!UNINITIALIZED_VARIABLE!>c<!><!> // error
    val c = 1
}

class ClassObject() {
    companion object {
        val x : Int

        init {
            x = 1
        }


        fun foo() {
            val a : Int
            doSmth(<!NI;UNINITIALIZED_VARIABLE!><!UNINITIALIZED_VARIABLE!>a<!><!>)
        }
    }
}

fun foo() {
    val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>a<!><!> = object {
        val x : Int
        <!NI;MUST_BE_INITIALIZED_OR_BE_ABSTRACT!><!MUST_BE_INITIALIZED_OR_BE_ABSTRACT!>val y : Int<!><!>
        val z : Int
        init {
            x = 1
            z = 3
        }
        fun foo() {
            y = 10
            <!NI;VAL_REASSIGNMENT!><!VAL_REASSIGNMENT!>z<!><!> = 13
        }
    }
}

class TestObjectExpression() {
    <!NI;MUST_BE_INITIALIZED_OR_BE_ABSTRACT!><!MUST_BE_INITIALIZED_OR_BE_ABSTRACT!>val a : Int<!><!>
    fun foo() {
        val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>a<!><!> = object {
            val x : Int
            <!NI;MUST_BE_INITIALIZED_OR_BE_ABSTRACT!><!MUST_BE_INITIALIZED_OR_BE_ABSTRACT!>val y : Int<!><!>
            init {
                if (true)
                    x = 12
                else
                    x = 1
            }
            fun inner1() {
                y = 101
                <!NI;VAL_REASSIGNMENT!><!VAL_REASSIGNMENT!>a<!><!> = 231
            }
            fun inner2() {
                y = 101
                a = 231
            }
        }
    }
}



object TestObjectDeclaration {
    val x : Int
    <!NI;MUST_BE_INITIALIZED_OR_BE_ABSTRACT!><!MUST_BE_INITIALIZED_OR_BE_ABSTRACT!>val y : Int<!><!>
    init {
        x = 1
    }

    fun foo() {
        <!NI;VAL_REASSIGNMENT!><!VAL_REASSIGNMENT!>y<!><!> = 10
        val i: Int
        if (1 < 3) {
            i = 10
        }
        doSmth(<!NI;UNINITIALIZED_VARIABLE!><!UNINITIALIZED_VARIABLE!>i<!><!>)
    }
}

fun func() {
    val b = 1
    val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>a<!><!> = object {
        val x = b
        init {
            <!NI;VAL_REASSIGNMENT!><!VAL_REASSIGNMENT!>b<!><!> = 4
        }
    }
}

// ------------------------------------------------
// dot qualifiers
class M() {
    val x = 11
    var y = 12
}

fun test(m : M) {
    <!NI;VAL_REASSIGNMENT!><!VAL_REASSIGNMENT!>m.x<!><!> = 23
    m.y = 23
}

fun test1(m : M) {
    <!NI;VAL_REASSIGNMENT!><!VAL_REASSIGNMENT!>m.x<!><!>++
    m.y--
}
