// !WITH_NEW_INFERENCE
// !DIAGNOSTICS: -UNUSED_PARAMETER -UNUSED_VARIABLE

fun println() {}
fun foo(x: Any) {}
fun <T> fooGeneric(x: T) {}

fun testResultOfLambda1() =
        run {
            if (true) 42 else println()
        }

fun testResultOfLambda2() =
        run {
            if (true) 42 else if (true) 42 else println()
        }

fun testResultOfAnonFun1() =
        run(fun () =
                if (true) <!IMPLICIT_CAST_TO_ANY!>42<!>
                else <!IMPLICIT_CAST_TO_ANY!>println()<!>
        )

fun testResultOfAnonFun2() =
        run(fun () {
            if (true) <!NI;UNUSED_EXPRESSION!><!UNUSED_EXPRESSION!>42<!><!> else println()
        })

fun testReturnFromAnonFun() =
        run(fun () {
            return if (true) <!NI;CONSTANT_EXPECTED_TYPE_MISMATCH!><!CONSTANT_EXPECTED_TYPE_MISMATCH!>42<!><!> else println()
        })

fun <!NI;IMPLICIT_NOTHING_RETURN_TYPE!><!IMPLICIT_NOTHING_RETURN_TYPE!>testReturn1<!><!>() =
        run {
            return <!NI;TYPE_MISMATCH!><!TYPE_MISMATCH!>if (true) <!NI;IMPLICIT_CAST_TO_ANY!><!IMPLICIT_CAST_TO_ANY!>42<!><!>
                   else <!NI;IMPLICIT_CAST_TO_ANY!><!IMPLICIT_CAST_TO_ANY!>println()<!><!><!><!>
        }

fun <!NI;IMPLICIT_NOTHING_RETURN_TYPE!><!IMPLICIT_NOTHING_RETURN_TYPE!>testReturn2<!><!>() =
        run {
            return <!NI;TYPE_MISMATCH!><!TYPE_MISMATCH!>if (true) <!NI;IMPLICIT_CAST_TO_ANY!><!IMPLICIT_CAST_TO_ANY!>42<!><!>
                   else if (true) <!NI;IMPLICIT_CAST_TO_ANY!><!IMPLICIT_CAST_TO_ANY!>42<!><!>
                   else <!NI;IMPLICIT_CAST_TO_ANY!><!IMPLICIT_CAST_TO_ANY!>println()<!><!><!><!>
        }

fun testUsage1() =
        if (true) <!NI;IMPLICIT_CAST_TO_ANY!><!IMPLICIT_CAST_TO_ANY!>42<!><!>
        else <!NI;IMPLICIT_CAST_TO_ANY!><!IMPLICIT_CAST_TO_ANY!>println()<!><!>

fun testUsage2() =
        foo(if (true) 42 else println())

fun testUsage2Generic() =
        fooGeneric(if (true) 42 else println())

val testUsage3 =
        if (true) <!NI;IMPLICIT_CAST_TO_ANY!><!IMPLICIT_CAST_TO_ANY!>42<!><!>
        else <!NI;IMPLICIT_CAST_TO_ANY!><!IMPLICIT_CAST_TO_ANY!>println()<!><!>

val testUsage4: Any get() =
        if (true) 42 else println()