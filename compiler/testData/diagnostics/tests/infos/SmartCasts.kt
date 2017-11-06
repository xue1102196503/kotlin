// !WITH_NEW_INFERENCE
open class A() {
  fun foo() {}
}

class B() : A() {
  fun bar() {}
}

fun f9(init : A?) {
  val a : A? = init
  a?.foo()
  a?.<!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>bar<!><!>()
  if (a is B) {
    <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>a<!><!>.bar()
    <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>a<!><!>.foo()
  }
  a?.foo()
  a?.<!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>bar<!><!>()
  if (!(a is B)) {
    a?.<!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>bar<!><!>()
    a?.foo()
  }
  if (!(a is B) || <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>a<!><!>.bar() == Unit) {
      a?.<!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>bar<!><!>()
  }
  if (!(a is B)) {
    return;
  }
  <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>a<!><!>.bar()
  <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>a<!><!>.foo()
}

fun f10(init : A?) {
  val a : A? = init
  if (!(a is B)) {
    return;
  }
  if (!(<!NI;USELESS_IS_CHECK!><!USELESS_IS_CHECK!>a is B<!><!>)) {
    return;
  }
}

class C() : A() {
  fun bar() {

  }
}

fun f101(a : A?) {
    if (a is C) {
      <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>a<!><!>.bar();
    }
}

fun f11(a : A?) {
  when (a) {
    is B -> <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>a<!><!>.bar()
    is A -> <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>a<!><!>.foo()
    is Any -> <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>a<!><!>.foo()
    <!NI;USELESS_IS_CHECK!><!USELESS_IS_CHECK!>is Any?<!><!> -> a.<!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>bar<!><!>()
    else -> a?.foo()
  }
}

fun f12(a : A?) {
  when (a) {
    is B -> <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>a<!><!>.bar()
    is A -> <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>a<!><!>.foo()
    is Any -> <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>a<!><!>.foo();
    <!NI;USELESS_IS_CHECK!><!USELESS_IS_CHECK!>is Any?<!><!> -> a.<!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>bar<!><!>()
    is C -> <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>a<!><!>.bar()
    else -> a?.foo()
  }

  if (<!NI;USELESS_IS_CHECK!><!USELESS_IS_CHECK!>a is Any?<!><!>) {
    a?.<!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>bar<!><!>()
  }
  if (a is B) {
    <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>a<!><!>.foo()
    <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>a<!><!>.bar()
  }
}

fun f13(a : A?) {
  if (a is B) {
    <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>a<!><!>.foo()
    <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>a<!><!>.bar()
  }
  else {
    a?.foo()
    <!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>c<!><!>.<!NI;DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!><!DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!>bar<!><!>()
  }

  a?.foo()
  if (!(a is B)) {
    a?.foo()
    <!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>c<!><!>.<!NI;DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!><!DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!>bar<!><!>()
  }
  else {
    <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>a<!><!>.foo()
    <!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>c<!><!>.<!NI;DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!><!DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!>bar<!><!>()
  }

  a?.foo()
  if (a is B && <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>a<!><!>.foo() == Unit) {
    <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>a<!><!>.foo()
    <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>a<!><!>.bar()
  }
  else {
    a?.foo()
    <!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>c<!><!>.<!NI;DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!><!DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!>bar<!><!>()
  }

  if (!(a is B) || !(a is C)) {
  }
  else {
  }

  if (!(a is B) || !(a is C)) {
  }

  if (!(a is B)) return
  <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>a<!><!>.bar()
}

fun f14(a : A?) {
  while (!(a is B)) {
  }
  <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>a<!><!>.bar()
}
fun f15(a : A?) {
  do {
  } while (!(a is B))
  <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>a<!><!>.bar()
}

fun getStringLength(obj : Any) : Char? {
  if (obj !is String)
    return null
  return <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>obj<!><!>.get(0) // no cast to String is needed
}

fun toInt(i: Int?): Int = if (i != null) <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>i<!><!> else 0
fun illegalWhenBody(a: Any): Int = <!NI;NO_ELSE_IN_WHEN!><!NO_ELSE_IN_WHEN!>when<!><!>(a) {
    is Int -> <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>a<!><!>
    is String -> <!NI;TYPE_MISMATCH!><!TYPE_MISMATCH!>a<!><!>
}
fun illegalWhenBlock(a: Any): Int {
    when(a) {
        is Int -> return <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>a<!><!>
        is String -> return <!NI;TYPE_MISMATCH!><!TYPE_MISMATCH!>a<!><!>
    }
<!NI;NO_RETURN_IN_FUNCTION_WITH_BLOCK_BODY!><!NO_RETURN_IN_FUNCTION_WITH_BLOCK_BODY!>}<!><!>
fun declarations(a: Any?) {
    if (a is String) {
       val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>p4<!><!>: String = <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>a<!><!>
    }
    if (a is String?) {
        if (a != null) {
            val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>s<!><!>: String = <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>a<!><!>
        }
    }
    if (a != null) {
        if (a is String?) {
            val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>s<!><!>: String = <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>a<!><!>
        }
    }
}
fun vars(a: Any?) {
    var <!NI;ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE!><!ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE!>b<!><!>: Int = 0
    if (a is Int) {
        <!NI;UNUSED_VALUE!><!UNUSED_VALUE!>b =<!><!> <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>a<!><!>
    }
}
fun returnFunctionLiteralBlock(a: Any?): Function0<Int> {
    if (a is Int) return { <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>a<!><!> }
    else return { 1 }
}
fun returnFunctionLiteral(a: Any?): Function0<Int> {
    if (a is Int) return { -> <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>a<!><!> }
    else return { -> 1 }
}

fun returnFunctionLiteralExpressionBody(a: Any?): Function0<Int> =
        if (a is Int) { -> <!NI;TYPE_MISMATCH!><!NI;TYPE_MISMATCH!><!DEBUG_INFO_SMARTCAST!>a<!><!><!> }
        else { -> 1 }


fun mergeSmartCasts(a: Any?) {
  if (a is String || a is Int) {
    a.<!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>compareTo<!><!>("")
    <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>a<!><!>.toString()
  }
  if (a is Int || a is String) {
    a.<!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>compareTo<!><!>("")
  }
  when (a) {
    is String, is Any -> a.<!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>compareTo<!><!>("")
  }
  if (a is String && <!NI;USELESS_IS_CHECK!><!USELESS_IS_CHECK!>a is Any<!><!>) {
    val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>i<!><!>: Int = <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>a<!><!>.compareTo("")
  }
  if (a is String && <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>a<!><!>.compareTo("") == 0) {}
  if (a is String || a.<!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>compareTo<!><!>("") <!NI;RESULT_TYPE_MISMATCH!><!NI;DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!><!DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!>==<!><!><!> 0) {}
}

//mutability
fun f(): String {
    var a: Any = 11
    if (a is String) {
        // a is a string, despite of being a variable
        val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>i<!><!>: String = <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>a<!><!>
        <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>a<!><!>.compareTo("f")
        // Beginning from here a is captured in a closure but nobody modifies it
        val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>f<!><!>: Function0<String> = { <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>a<!><!> }
        return <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>a<!><!>
    }
    return ""
}

fun foo(aa: Any?): Int {
    var a = aa
    if (a is Int?) {
        return <!NI;TYPE_MISMATCH!><!TYPE_MISMATCH!>a<!><!>
    }
    return 1
}
