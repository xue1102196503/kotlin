// !WITH_NEW_INFERENCE
// !DIAGNOSTICS: -UNREACHABLE_CODE
package kt770_351_735


//KT-770 Reference is not resolved to anything, but is not marked unresolved
fun main(args : Array<String>) {
    var i = 0
    when (i) {
        1 -> i--
        2 -> i = 2  // i is surrounded by a black border
        else -> <!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>j<!><!> = 2
    }
    System.out.println(i)
}

//KT-351 Distinguish statement and expression positions
val w = <!NI;EXPRESSION_EXPECTED!><!EXPRESSION_EXPECTED!>while (true) {}<!><!>

fun foo() {
    var <!NI;ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE!><!ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE!>z<!><!> = 2
    val r = {  // type fun(): Any is inferred
        if (true) {
            2
        }
        else {
            z = 34
        }
    }
    val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>f<!><!>: ()-> Int = <!NI;TYPE_MISMATCH!><!TYPE_MISMATCH!>r<!><!>
    val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>g<!><!>: ()-> Any = r
}

//KT-735 Statements without braces are prohibited on the right side of when entries.
fun box() : Int {
    val d = 2
    var z = 0
    when(d) {
        5, 3 -> z++
        else -> z = -1000
    }
    return z
}

//More tests

fun test1() { while(true) {} }
fun test2(): Unit { while(true) {} }

fun testCoercionToUnit() {
    val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>simple<!><!>: ()-> Unit = {
        <!NI;UNUSED_EXPRESSION!><!UNUSED_EXPRESSION!>41<!><!>
    }
    val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>withIf<!><!>: ()-> Unit = {
        if (true) {
            <!NI;UNUSED_EXPRESSION!><!UNUSED_EXPRESSION!>3<!><!>
        } else {
            <!NI;UNUSED_EXPRESSION!><!UNUSED_EXPRESSION!>45<!><!>
        }
    }
    val i = 34
    val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>withWhen<!><!> : () -> Unit = {
        when(i) {
            1 -> {
                val d = 34
                <!NI;UNUSED_EXPRESSION!><!UNUSED_EXPRESSION!>"1"<!><!>
                doSmth(d)

            }
            2 -> <!NI;UNUSED_EXPRESSION!><!UNUSED_EXPRESSION!>'4'<!><!>
            else -> <!NI;UNUSED_EXPRESSION!><!UNUSED_EXPRESSION!>true<!><!>
        }
    }

    var <!NI;ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE!><!ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE!>x<!><!> = 43
    val checkType = {
        if (true) {
            x = 4
        } else {
            45
        }
    }
    val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>f<!><!> : () -> String = <!NI;TYPE_MISMATCH!><!TYPE_MISMATCH!>checkType<!><!>
}

fun doSmth(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>i<!><!>: Int) {}

fun testImplicitCoercion() {
    val d = 21
    var z = 0
    var <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>i<!><!> = when(d) {
        3 -> null
        4 -> { val <!NI;NAME_SHADOWING!><!NI;UNUSED_VARIABLE!><!NAME_SHADOWING!><!UNUSED_VARIABLE!>z<!><!><!><!> = 23 }
        else -> z = 20
    }

    var <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>u<!><!> = when(d) {
        3 -> {
        <!NI;IMPLICIT_CAST_TO_ANY!><!IMPLICIT_CAST_TO_ANY!><!NI;UNUSED_VALUE!><!UNUSED_VALUE!>z =<!><!> 34<!><!>
    }
        else -> <!NI;UNUSED_CHANGED_VALUE!><!NI;IMPLICIT_CAST_TO_ANY!><!UNUSED_CHANGED_VALUE!><!IMPLICIT_CAST_TO_ANY!>z--<!><!><!><!>
    }

    var <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>iff<!><!> = <!NI;INVALID_IF_AS_EXPRESSION!><!INVALID_IF_AS_EXPRESSION!>if<!><!> (true) {
        <!NI;UNUSED_VALUE!><!UNUSED_VALUE!>z =<!><!> 34
    }
    val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>g<!><!> = <!NI;INVALID_IF_AS_EXPRESSION!><!INVALID_IF_AS_EXPRESSION!>if<!><!> (true) 4
    val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>h<!><!> = if (false) <!NI;IMPLICIT_CAST_TO_ANY!><!IMPLICIT_CAST_TO_ANY!>4<!><!> else <!NI;IMPLICIT_CAST_TO_ANY!><!IMPLICIT_CAST_TO_ANY!>{}<!><!>

    bar(<!NI;TYPE_MISMATCH!>if (true) {
        <!NI;CONSTANT_EXPECTED_TYPE_MISMATCH!><!CONSTANT_EXPECTED_TYPE_MISMATCH!>4<!><!>
    }
        else {
        <!NI;UNUSED_VALUE!><!UNUSED_VALUE!>z =<!><!> 342
    }<!>)
}

fun fooWithAnyArg(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>arg<!><!>: Any) {}
fun fooWithAnyNullableArg(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>arg<!><!>: Any?) {}

fun testCoercionToAny() {
    val d = 21
    val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>x1<!><!>: Any = if (1>2) 1 else 2.0
    val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>x2<!><!>: Any? = if (1>2) 1 else 2.0
    val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>x3<!><!>: Any? = if (1>2) 1 else (if (1>2) null else 2.0)

    fooWithAnyArg(if (1>2) 1 else 2.0)
    fooWithAnyNullableArg(if (1>2) 1 else 2.0)
    fooWithAnyNullableArg(if (1>2) 1 else (if (1>2) null else 2.0))

    val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>y1<!><!>: Any = when(d) { 1 -> 1.0 else -> 2.0 }
    val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>y2<!><!>: Any? = when(d) { 1 -> 1.0 else -> 2.0 }
    val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>y3<!><!>: Any? = when(d) { 1 -> 1.0; 2 -> null; else -> 2.0 }

    fooWithAnyArg(when(d) { 1 -> 1.0 else -> 2.0 })
    fooWithAnyNullableArg(when(d) { 1 -> 1.0 else -> 2.0 })
    fooWithAnyNullableArg(when(d) { 1 -> 1.0; 2 -> null; else -> 2.0 })
}

fun fooWithAnuNullableResult(s: String?, name: String, optional: Boolean): Any? {
    return if (s == null) {
        if (!optional) {
            throw java.lang.IllegalArgumentException("Parameter '$name' was not found in the request")
        }
        null
    } else {
        name
    }
}

fun bar(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>a<!><!>: Unit) {}

fun testStatementInExpressionContext() {
    var <!NI;ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE!><!ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE!>z<!><!> = 34
    val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>a1<!><!>: Unit = <!NI;ASSIGNMENT_IN_EXPRESSION_CONTEXT!><!ASSIGNMENT_IN_EXPRESSION_CONTEXT!><!NI;UNUSED_VALUE!><!UNUSED_VALUE!>z =<!><!> 334<!><!>
    val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>f<!><!> = <!NI;EXPRESSION_EXPECTED!><!EXPRESSION_EXPECTED!>for (i in 1..10) {}<!><!>
    if (true) return <!NI;ASSIGNMENT_IN_EXPRESSION_CONTEXT!><!ASSIGNMENT_IN_EXPRESSION_CONTEXT!><!NI;UNUSED_VALUE!><!UNUSED_VALUE!>z =<!><!> 34<!><!>
    return <!NI;EXPRESSION_EXPECTED!><!EXPRESSION_EXPECTED!>while (true) {}<!><!>
}

fun testStatementInExpressionContext2() {
    val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>a2<!><!>: Unit = <!NI;EXPRESSION_EXPECTED!><!EXPRESSION_EXPECTED!>while(true) {}<!><!>
}