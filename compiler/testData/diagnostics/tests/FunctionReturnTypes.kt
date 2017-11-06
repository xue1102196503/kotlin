// !WITH_NEW_INFERENCE
// !DIAGNOSTICS: -UNREACHABLE_CODE

fun none() {}

fun unitEmptyInfer() {}
fun unitEmpty() : Unit {}
fun unitEmptyReturn() : Unit {return}
fun unitIntReturn() : Unit {return <!NI;CONSTANT_EXPECTED_TYPE_MISMATCH!><!CONSTANT_EXPECTED_TYPE_MISMATCH!>1<!><!>}
fun unitUnitReturn() : Unit {return Unit}
fun test1() : Any = {<!NI;RETURN_NOT_ALLOWED!><!NI;RETURN_TYPE_MISMATCH!><!RETURN_NOT_ALLOWED!><!RETURN_TYPE_MISMATCH!>return<!><!><!><!>}
fun test2() : Any = a@ {return@a 1}
fun test3() : Any { <!NI;RETURN_TYPE_MISMATCH!><!RETURN_TYPE_MISMATCH!>return<!><!> }
fun test4(): ()-> Unit = { <!NI;RETURN_NOT_ALLOWED!><!NI;RETURN_TYPE_MISMATCH!><!RETURN_NOT_ALLOWED!><!RETURN_TYPE_MISMATCH!>return@test4<!><!><!><!> }
fun test5(): Any = l@{ return@l }
fun test6(): Any = {<!NI;RETURN_NOT_ALLOWED!><!RETURN_NOT_ALLOWED!>return<!><!> 1}

fun bbb() {
    return <!NI;CONSTANT_EXPECTED_TYPE_MISMATCH!><!CONSTANT_EXPECTED_TYPE_MISMATCH!>1<!><!>
}

fun foo(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>expr<!><!>: StringBuilder): Int {
    val c = 'a'
    when(c) {
        0.toChar() -> throw Exception("zero")
        else -> throw Exception("nonzero" + c)
    }
}


fun unitShort() : Unit = Unit
fun unitShortConv() : Unit = <!NI;CONSTANT_EXPECTED_TYPE_MISMATCH!><!CONSTANT_EXPECTED_TYPE_MISMATCH!>1<!><!>
fun unitShortNull() : Unit = <!NI;NULL_FOR_NONNULL_TYPE!><!NULL_FOR_NONNULL_TYPE!>null<!><!>

fun intEmpty() : Int {<!NI;NO_RETURN_IN_FUNCTION_WITH_BLOCK_BODY!><!NO_RETURN_IN_FUNCTION_WITH_BLOCK_BODY!>}<!><!>
fun intShortInfer() = 1
fun intShort() : Int = 1
//fun intBlockInfer()  {1}
fun intBlock() : Int {return 1}
fun intBlock1() : Int {<!NI;UNUSED_EXPRESSION!><!UNUSED_EXPRESSION!>1<!><!><!NI;NO_RETURN_IN_FUNCTION_WITH_BLOCK_BODY!><!NO_RETURN_IN_FUNCTION_WITH_BLOCK_BODY!>}<!><!>

fun intString(): Int = <!NI;TYPE_MISMATCH!><!TYPE_MISMATCH!>"s"<!><!>
fun intFunctionLiteral(): Int = <!NI;TYPE_MISMATCH_DUE_TO_EQUALS_LAMBDA_IN_FUN!><!TYPE_MISMATCH_DUE_TO_EQUALS_LAMBDA_IN_FUN!>{ 10 }<!><!>

fun blockReturnUnitMismatch() : Int {<!NI;RETURN_TYPE_MISMATCH!><!RETURN_TYPE_MISMATCH!>return<!><!>}
fun blockReturnValueTypeMismatch() : Int {return <!NI;CONSTANT_EXPECTED_TYPE_MISMATCH!><!CONSTANT_EXPECTED_TYPE_MISMATCH!>3.4<!><!>}
fun blockReturnValueTypeMatch() : Int {return 1}
fun blockReturnValueTypeMismatchUnit() : Int {return <!NI;TYPE_MISMATCH!><!TYPE_MISMATCH!>Unit<!><!>}

fun blockAndAndMismatch() : Int {
    <!NI;UNUSED_EXPRESSION!><!UNUSED_EXPRESSION!>true && false<!><!>
    <!NI;NO_RETURN_IN_FUNCTION_WITH_BLOCK_BODY!><!NO_RETURN_IN_FUNCTION_WITH_BLOCK_BODY!>}<!><!>
fun blockAndAndMismatch1() : Int {
    return <!NI;TYPE_MISMATCH!><!TYPE_MISMATCH!>true && false<!><!>
}
fun blockAndAndMismatch2() : Int {
    (return <!NI;CONSTANT_EXPECTED_TYPE_MISMATCH!><!CONSTANT_EXPECTED_TYPE_MISMATCH!>true<!><!>) && (return <!NI;CONSTANT_EXPECTED_TYPE_MISMATCH!><!CONSTANT_EXPECTED_TYPE_MISMATCH!>false<!><!>)
}

fun blockAndAndMismatch3() : Int {
    <!NI;UNUSED_EXPRESSION!><!UNUSED_EXPRESSION!>true || false<!><!>
    <!NI;NO_RETURN_IN_FUNCTION_WITH_BLOCK_BODY!><!NO_RETURN_IN_FUNCTION_WITH_BLOCK_BODY!>}<!><!>
fun blockAndAndMismatch4() : Int {
    return <!NI;TYPE_MISMATCH!><!TYPE_MISMATCH!>true || false<!><!>
}
fun blockAndAndMismatch5() : Int {
    (return <!NI;CONSTANT_EXPECTED_TYPE_MISMATCH!><!CONSTANT_EXPECTED_TYPE_MISMATCH!>true<!><!>) || (return <!NI;CONSTANT_EXPECTED_TYPE_MISMATCH!><!CONSTANT_EXPECTED_TYPE_MISMATCH!>false<!><!>)
}
fun blockReturnValueTypeMatch1() : Int {
    return if (1 > 2) <!NI;CONSTANT_EXPECTED_TYPE_MISMATCH!><!CONSTANT_EXPECTED_TYPE_MISMATCH!>1.0<!><!> else <!CONSTANT_EXPECTED_TYPE_MISMATCH!>2.0<!>
}
fun blockReturnValueTypeMatch2() : Int {
    return <!NI;TYPE_MISMATCH!><!TYPE_MISMATCH!><!NI;INVALID_IF_AS_EXPRESSION!><!INVALID_IF_AS_EXPRESSION!>if<!><!> (1 > 2) 1<!><!>
}
fun blockReturnValueTypeMatch3() : Int {
    return <!NI;TYPE_MISMATCH!><!TYPE_MISMATCH!><!NI;INVALID_IF_AS_EXPRESSION!><!INVALID_IF_AS_EXPRESSION!>if<!><!> (1 > 2) else 1<!><!>
}
fun blockReturnValueTypeMatch4() : Int {
    if (1 > 2)
        return <!NI;CONSTANT_EXPECTED_TYPE_MISMATCH!><!CONSTANT_EXPECTED_TYPE_MISMATCH!>1.0<!><!>
    else return <!NI;CONSTANT_EXPECTED_TYPE_MISMATCH!><!CONSTANT_EXPECTED_TYPE_MISMATCH!>2.0<!><!>
}
fun blockReturnValueTypeMatch5() : Int {
    if (1 > 2)
        return <!NI;CONSTANT_EXPECTED_TYPE_MISMATCH!><!CONSTANT_EXPECTED_TYPE_MISMATCH!>1.0<!><!>
    return <!NI;CONSTANT_EXPECTED_TYPE_MISMATCH!><!CONSTANT_EXPECTED_TYPE_MISMATCH!>2.0<!><!>
}
fun blockReturnValueTypeMatch6() : Int {
    if (1 > 2)
    else return <!NI;CONSTANT_EXPECTED_TYPE_MISMATCH!><!CONSTANT_EXPECTED_TYPE_MISMATCH!>1.0<!><!>
    return <!NI;CONSTANT_EXPECTED_TYPE_MISMATCH!><!CONSTANT_EXPECTED_TYPE_MISMATCH!>2.0<!><!>
}
fun blockReturnValueTypeMatch7() : Int {
    if (1 > 2)
    <!NI;UNUSED_EXPRESSION!><!UNUSED_EXPRESSION!>1.0<!><!>
    else <!NI;UNUSED_EXPRESSION!><!UNUSED_EXPRESSION!>2.0<!><!>
    <!NI;NO_RETURN_IN_FUNCTION_WITH_BLOCK_BODY!><!NO_RETURN_IN_FUNCTION_WITH_BLOCK_BODY!>}<!><!>
fun blockReturnValueTypeMatch8() : Int {
    if (1 > 2)
    <!NI;UNUSED_EXPRESSION!><!UNUSED_EXPRESSION!>1.0<!><!>
    else <!NI;UNUSED_EXPRESSION!><!UNUSED_EXPRESSION!>2.0<!><!>
    return 1
}
fun blockReturnValueTypeMatch9() : Int {
    if (1 > 2)
    <!NI;UNUSED_EXPRESSION!><!UNUSED_EXPRESSION!>1.0<!><!>
    <!NI;NO_RETURN_IN_FUNCTION_WITH_BLOCK_BODY!><!NO_RETURN_IN_FUNCTION_WITH_BLOCK_BODY!>}<!><!>
fun blockReturnValueTypeMatch10() : Int {
    return <!NI;TYPE_MISMATCH!><!TYPE_MISMATCH!><!NI;INVALID_IF_AS_EXPRESSION!><!INVALID_IF_AS_EXPRESSION!>if<!><!> (1 > 2)
    1<!><!>
}
fun blockReturnValueTypeMatch11() : Int {
    if (1 > 2)
    else <!NI;UNUSED_EXPRESSION!><!UNUSED_EXPRESSION!>1.0<!><!>
    <!NI;NO_RETURN_IN_FUNCTION_WITH_BLOCK_BODY!><!NO_RETURN_IN_FUNCTION_WITH_BLOCK_BODY!>}<!><!>
fun blockReturnValueTypeMatch12() : Int {
    if (1 > 2)
        return 1
    else return <!NI;CONSTANT_EXPECTED_TYPE_MISMATCH!><!CONSTANT_EXPECTED_TYPE_MISMATCH!>1.0<!><!>
}
fun blockNoReturnIfValDeclaration(): Int {
    val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>x<!><!> = 1
    <!NI;NO_RETURN_IN_FUNCTION_WITH_BLOCK_BODY!><!NO_RETURN_IN_FUNCTION_WITH_BLOCK_BODY!>}<!><!>
fun blockNoReturnIfEmptyIf(): Int {
    if (1 < 2) {} else {}
    <!NI;NO_RETURN_IN_FUNCTION_WITH_BLOCK_BODY!><!NO_RETURN_IN_FUNCTION_WITH_BLOCK_BODY!>}<!><!>
fun blockNoReturnIfUnitInOneBranch(): Int {
    if (1 < 2) {
        return 1
    } else {
        if (3 < 4) {
        } else {
            return 2
        }
    }
    <!NI;NO_RETURN_IN_FUNCTION_WITH_BLOCK_BODY!><!NO_RETURN_IN_FUNCTION_WITH_BLOCK_BODY!>}<!><!>
fun nonBlockReturnIfEmptyIf(): Int = if (1 < 2) <!NI;TYPE_MISMATCH!><!TYPE_MISMATCH!>{}<!><!> else <!TYPE_MISMATCH!>{}<!>
fun nonBlockNoReturnIfUnitInOneBranch(): Int = if (1 < 2) <!NI;TYPE_MISMATCH!><!TYPE_MISMATCH!>{}<!><!> else 2

val a = <!NI;RETURN_NOT_ALLOWED!><!RETURN_NOT_ALLOWED!>return<!><!> 1

class A() {
}
fun illegalConstantBody(): Int = <!NI;TYPE_MISMATCH!><!TYPE_MISMATCH!>"s"<!><!>
fun illegalConstantBlock(): String {
    return <!NI;CONSTANT_EXPECTED_TYPE_MISMATCH!><!CONSTANT_EXPECTED_TYPE_MISMATCH!>1<!><!>
}
fun illegalIfBody(): Int =
        if (1 < 2) <!NI;CONSTANT_EXPECTED_TYPE_MISMATCH!><!CONSTANT_EXPECTED_TYPE_MISMATCH!>'a'<!><!> else <!NI;TYPE_MISMATCH!>{ <!CONSTANT_EXPECTED_TYPE_MISMATCH!>1.0<!> }<!>
fun illegalIfBlock(): Boolean {
    if (1 < 2)
        return false
    else { return <!NI;CONSTANT_EXPECTED_TYPE_MISMATCH!><!CONSTANT_EXPECTED_TYPE_MISMATCH!>1<!><!> }
}
fun illegalReturnIf(): Char {
    return if (1 < 2) 'a' else <!NI;TYPE_MISMATCH!>{ <!CONSTANT_EXPECTED_TYPE_MISMATCH!>1<!> }<!>
}

fun returnNothing(): Nothing {
    throw <!NI;CONSTANT_EXPECTED_TYPE_MISMATCH!><!CONSTANT_EXPECTED_TYPE_MISMATCH!>1<!><!>
}
fun f(): Int {
    if (1 < 2) { return 1 } else returnNothing()
}

fun f1(): Int = if (1 < 2) 1 else returnNothing()

public fun f2() = 1
class B() {
    protected fun f() = "ss"
}

fun testFunctionLiterals() {
    val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>endsWithVarDeclaration<!><!> : () -> Boolean = {
        <!NI;EXPECTED_TYPE_MISMATCH!><!EXPECTED_TYPE_MISMATCH!>val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>x<!><!> = 2<!><!>
    }

    val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>endsWithAssignment<!><!>: () -> Int = {
        var <!NI;ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE!><!ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE!>x<!><!> = 1
        <!NI;EXPECTED_TYPE_MISMATCH!><!EXPECTED_TYPE_MISMATCH!><!NI;UNUSED_VALUE!><!UNUSED_VALUE!>x =<!><!> 333<!><!>
    }

    val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>endsWithReAssignment<!><!>: () -> Int = {
        var x = 1
        <!NI;ASSIGNMENT_TYPE_MISMATCH!><!ASSIGNMENT_TYPE_MISMATCH!>x += 333<!><!>
    }

    val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>endsWithFunDeclaration<!><!> : () -> String = {
        var <!NI;ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE!><!ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE!>x<!><!> = 1
        <!NI;UNUSED_VALUE!><!UNUSED_VALUE!>x =<!><!> 333
        <!NI;EXPECTED_TYPE_MISMATCH!><!EXPECTED_TYPE_MISMATCH!>fun meow() : Unit {}<!><!>
    }

    val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>endsWithObjectDeclaration<!><!> : () -> Int = {
        var <!NI;ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE!><!ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE!>x<!><!> = 1
        <!NI;UNUSED_VALUE!><!UNUSED_VALUE!>x =<!><!> 333
        <!NI;LOCAL_OBJECT_NOT_ALLOWED!><!NI;EXPECTED_TYPE_MISMATCH!><!LOCAL_OBJECT_NOT_ALLOWED!><!EXPECTED_TYPE_MISMATCH!>object A<!><!><!><!> {}
    }

    val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>expectedUnitReturnType1<!><!>: () -> Unit = {
        val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>x<!><!> = 1
    }

    val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>expectedUnitReturnType2<!><!>: () -> Unit = {
        fun meow() : Unit {}
        <!NI;LOCAL_OBJECT_NOT_ALLOWED!><!LOCAL_OBJECT_NOT_ALLOWED!>object A<!><!> {}
    }

}
