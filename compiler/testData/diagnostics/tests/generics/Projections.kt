// !WITH_NEW_INFERENCE
// !CHECK_TYPE

class In<in T>() {
    fun f(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>t<!><!> : T) : Unit {}
    fun f(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>t<!><!> : Int) : Int = 1
    fun f1(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>t<!><!> : T) : Unit {}
}

class Out<out T>() {
    fun f() : T {throw IllegalStateException()}
    fun f(a : Int) : Int = a
}

class Inv<T>() {
    fun f(t : T) : T = t
    fun inf(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>t<!><!> : T) : Unit {}
    fun outf() : T {throw IllegalStateException()}
}

fun testInOut() {
    In<String>().f("1");
    (null <!NI;CAST_NEVER_SUCCEEDS!><!CAST_NEVER_SUCCEEDS!>as<!><!> In<<!NI;REDUNDANT_PROJECTION!><!REDUNDANT_PROJECTION!>in<!><!> String>).f("1")
    (null <!NI;CAST_NEVER_SUCCEEDS!><!CAST_NEVER_SUCCEEDS!>as<!><!> In<*>).<!NI;NONE_APPLICABLE!><!NONE_APPLICABLE!>f<!><!>("1") // Wrong Arg

    In<String>().f(1);
    (null <!NI;CAST_NEVER_SUCCEEDS!><!CAST_NEVER_SUCCEEDS!>as<!><!> In<<!NI;REDUNDANT_PROJECTION!><!REDUNDANT_PROJECTION!>in<!><!> String>).f(1)
    (null <!NI;CAST_NEVER_SUCCEEDS!><!CAST_NEVER_SUCCEEDS!>as<!><!> In<*>).f(1);

    Out<Int>().f(1)
    (null <!NI;CAST_NEVER_SUCCEEDS!><!CAST_NEVER_SUCCEEDS!>as<!><!> Out<<!NI;REDUNDANT_PROJECTION!><!REDUNDANT_PROJECTION!>out<!><!> Int>).f(1)
    (null <!NI;CAST_NEVER_SUCCEEDS!><!CAST_NEVER_SUCCEEDS!>as<!><!> Out<*>).f(1)

    Out<Int>().f()
    (null <!NI;CAST_NEVER_SUCCEEDS!><!CAST_NEVER_SUCCEEDS!>as<!><!> Out<<!NI;REDUNDANT_PROJECTION!><!REDUNDANT_PROJECTION!>out<!><!> Int>).f()
    (null <!NI;CAST_NEVER_SUCCEEDS!><!CAST_NEVER_SUCCEEDS!>as<!><!> Out<*>).f()

    Inv<Int>().f(1)
    (null <!NI;CAST_NEVER_SUCCEEDS!><!CAST_NEVER_SUCCEEDS!>as<!><!> Inv<in Int>).f(1)
    (null <!NI;CAST_NEVER_SUCCEEDS!><!CAST_NEVER_SUCCEEDS!>as<!><!> Inv<out Int>).<!MEMBER_PROJECTED_OUT!>f<!>(<!NI;CONSTANT_EXPECTED_TYPE_MISMATCH!>1<!>) // !!
    (null <!NI;CAST_NEVER_SUCCEEDS!><!CAST_NEVER_SUCCEEDS!>as<!><!> Inv<*>).<!MEMBER_PROJECTED_OUT!>f<!>(<!NI;CONSTANT_EXPECTED_TYPE_MISMATCH!>1<!>) // !!

    Inv<Int>().inf(1)
    (null <!NI;CAST_NEVER_SUCCEEDS!><!CAST_NEVER_SUCCEEDS!>as<!><!> Inv<in Int>).inf(1)
    (null <!NI;CAST_NEVER_SUCCEEDS!><!CAST_NEVER_SUCCEEDS!>as<!><!> Inv<out Int>).<!MEMBER_PROJECTED_OUT!>inf<!>(<!NI;CONSTANT_EXPECTED_TYPE_MISMATCH!>1<!>) // !!
    (null <!NI;CAST_NEVER_SUCCEEDS!><!CAST_NEVER_SUCCEEDS!>as<!><!> Inv<*>).<!MEMBER_PROJECTED_OUT!>inf<!>(<!NI;CONSTANT_EXPECTED_TYPE_MISMATCH!>1<!>) // !!

    Inv<Int>().outf()
    checkSubtype<Int>(<!NI;TYPE_MISMATCH!><!TYPE_MISMATCH!>(null <!NI;CAST_NEVER_SUCCEEDS!><!CAST_NEVER_SUCCEEDS!>as<!><!> Inv<in Int>).outf()<!><!>) // Type mismatch
    (null <!NI;CAST_NEVER_SUCCEEDS!><!CAST_NEVER_SUCCEEDS!>as<!><!> Inv<out Int>).outf()
    (null <!NI;CAST_NEVER_SUCCEEDS!><!CAST_NEVER_SUCCEEDS!>as<!><!> Inv<*>).outf()

    Inv<Int>().outf(<!NI;TOO_MANY_ARGUMENTS!><!TOO_MANY_ARGUMENTS!>1<!><!>) // Wrong Arg
}