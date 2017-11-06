// !WITH_NEW_INFERENCE
// !CHECK_TYPE

package foo

fun Any.foo() : () -> Unit {
  return {}
}

fun Any.foo1() : (i : Int) -> Unit {
  return {}
}

fun foo2() : (i : () -> Unit) -> Unit {
  return {}
}

fun <T> fooT1(t : T) : () -> T {
  return {t}
}

fun <T> fooT2() : (t : T) -> T {
  return {it}
}

fun main(args : Array<String>) {
    args.foo()()
    args.foo1()(<!NI;NO_VALUE_FOR_PARAMETER!><!NO_VALUE_FOR_PARAMETER!>)<!><!>
    <!NI;FUNCTION_EXPECTED!><!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>a<!><!>.<!NI;DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!><!DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!>foo1<!><!>()<!>()
    <!NI;FUNCTION_EXPECTED!><!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>a<!><!>.<!NI;DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!><!DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!>foo1<!><!>()<!>(<!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>a<!><!>)

    args.foo1()(1)
    args.foo1()(<!NI;TYPE_MISMATCH!><!TYPE_MISMATCH!>"1"<!><!>)
    <!NI;FUNCTION_EXPECTED!><!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>a<!><!>.<!NI;DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!><!DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!>foo1<!><!>()<!>("1")
    <!NI;FUNCTION_EXPECTED!><!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>a<!><!>.<!NI;DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!><!DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!>foo1<!><!>()<!>(<!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>a<!><!>)

    foo2()({})
    foo2()<!NI;TOO_MANY_ARGUMENTS!><!TOO_MANY_ARGUMENTS!>{}<!><!>
    (foo2()){}
    (foo2()){<!NI;EXPECTED_PARAMETERS_NUMBER_MISMATCH!><!NI;CANNOT_INFER_PARAMETER_TYPE!><!NI;UNUSED_ANONYMOUS_PARAMETER!><!EXPECTED_PARAMETERS_NUMBER_MISMATCH!><!CANNOT_INFER_PARAMETER_TYPE!><!UNUSED_ANONYMOUS_PARAMETER!>x<!><!><!><!><!><!> -> }
    foo2()({<!NI;EXPECTED_PARAMETERS_NUMBER_MISMATCH!><!NI;CANNOT_INFER_PARAMETER_TYPE!><!NI;UNUSED_ANONYMOUS_PARAMETER!><!EXPECTED_PARAMETERS_NUMBER_MISMATCH!><!CANNOT_INFER_PARAMETER_TYPE!><!UNUSED_ANONYMOUS_PARAMETER!>x<!><!><!><!><!><!> -> })

    val a = fooT1(1)()
    checkSubtype<Int>(a)

    val b = fooT2<Int>()(1)
    checkSubtype<Int>(b)
    <!TYPE_INFERENCE_NO_INFORMATION_FOR_PARAMETER!>fooT2<!>()(1) // : Any?

    <!NI;FUNCTION_EXPECTED!><!FUNCTION_EXPECTED!>1<!><!>()
    <!NI;FUNCTION_EXPECTED!><!FUNCTION_EXPECTED!>1<!><!>{}
    <!NI;FUNCTION_EXPECTED!><!FUNCTION_EXPECTED!>1<!><!>(){}
}

fun f() :  Int.() -> Unit = {}

fun main1() {
    1.(fun Int.() = 1)();
    {1}();
    (fun (x : Int) = x)(1)
    1.(fun Int.(x : Int) = x)(1);
    l@{1}()
    1.((fun Int.() = 1))()
    1.(f())()
    1.if(true){f()}else{f()}()
    1.if(true)(fun Int.() {})else{f()}()

    1.<!NI;FUNCTION_EXPECTED!><!FUNCTION_EXPECTED!>"sdf"<!><!>()

    1.<!NI;ILLEGAL_SELECTOR!><!ILLEGAL_SELECTOR!>"sdf"<!><!>
    1.<!NI;ILLEGAL_SELECTOR!><!ILLEGAL_SELECTOR!>{}<!><!>
    1.<!NI;ILLEGAL_SELECTOR!><!ILLEGAL_SELECTOR!>if (true) {}<!><!>
}

fun test() {
    {<!NI;UNUSED_ANONYMOUS_PARAMETER!><!UNUSED_ANONYMOUS_PARAMETER!>x<!><!> : Int -> 1}(<!NI;NO_VALUE_FOR_PARAMETER!><!NO_VALUE_FOR_PARAMETER!>)<!><!>;
    (fun Int.() = 1)(<!NI;NO_VALUE_FOR_PARAMETER!><!NO_VALUE_FOR_PARAMETER!>)<!><!>
    <!NI;TYPE_MISMATCH!><!TYPE_MISMATCH!>"sd"<!><!>.(fun Int.() = 1)()
    val i : Int? = null
    i<!NI;UNSAFE_CALL!><!UNSAFE_CALL!>.<!><!>(fun Int.() = 1)();
    {}<!NI;WRONG_NUMBER_OF_TYPE_ARGUMENTS!><!WRONG_NUMBER_OF_TYPE_ARGUMENTS!><Int><!><!>()
    1<!NI;UNNECESSARY_SAFE_CALL!><!UNNECESSARY_SAFE_CALL!>?.<!><!>(fun Int.() = 1)()
    1.<!NI;NO_RECEIVER_ALLOWED!><!NO_RECEIVER_ALLOWED!>{}<!><!>()
}
