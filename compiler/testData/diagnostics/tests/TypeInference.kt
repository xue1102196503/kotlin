// !WITH_NEW_INFERENCE
class C<T>() {
  fun foo() : T {<!NI;NO_RETURN_IN_FUNCTION_WITH_BLOCK_BODY!><!NO_RETURN_IN_FUNCTION_WITH_BLOCK_BODY!>}<!><!>
}

fun foo(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>c<!><!>: C<Int>) {}
fun <T> bar() : C<T> {<!NI;NO_RETURN_IN_FUNCTION_WITH_BLOCK_BODY!><!NO_RETURN_IN_FUNCTION_WITH_BLOCK_BODY!>}<!><!>

fun main(args : Array<String>) {
  val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>a<!><!> : C<Int> = C();
  val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>x<!><!> : C<in String> = C()
  val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>y<!><!> : C<out String> = C()
  val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>z<!><!> : C<*> = <!TYPE_INFERENCE_NO_INFORMATION_FOR_PARAMETER!>C<!>()

  val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>ba<!><!> : C<Int> = bar();
  val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>bx<!><!> : C<in String> = bar()
  val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>by<!><!> : C<out String> = bar()
  val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>bz<!><!> : C<*> = <!TYPE_INFERENCE_NO_INFORMATION_FOR_PARAMETER!>bar<!>()
}