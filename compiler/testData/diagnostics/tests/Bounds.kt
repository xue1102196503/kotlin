// !WITH_NEW_INFERENCE
// FILE: a.kt
package boundsWithSubstitutors
    open class A<T>
    class B<X : A<X>>()

    class C : A<C>()

    val a = B<C>()
    val a1 = B<<!NI;UPPER_BOUND_VIOLATED!><!NI;UPPER_BOUND_VIOLATED!><!NI;UPPER_BOUND_VIOLATED!><!NI;UPPER_BOUND_VIOLATED!><!NI;UPPER_BOUND_VIOLATED!><!UPPER_BOUND_VIOLATED!>Int<!><!><!><!><!><!>>()

    class X<A, B : A>()

    val b = X<Any, X<A<C>, C>>()
    val b0 = X<Any, <!NI;UPPER_BOUND_VIOLATED!><!NI;UPPER_BOUND_VIOLATED!><!UPPER_BOUND_VIOLATED!>Any?<!><!><!>>()
    val b1 = X<Any, X<A<C>, <!NI;UPPER_BOUND_VIOLATED!><!UPPER_BOUND_VIOLATED!>String<!><!>>>()

// FILE: b.kt
  open class A {}
  open class B<T : A>()

  class Pair<A, B>

  abstract class C<T : B<<!NI;UPPER_BOUND_VIOLATED!><!UPPER_BOUND_VIOLATED!>Int<!><!>>, X :  (B<<!NI;UPPER_BOUND_VIOLATED!><!UPPER_BOUND_VIOLATED!>Char<!><!>>) -> Pair<B<<!NI;UPPER_BOUND_VIOLATED!><!UPPER_BOUND_VIOLATED!>Any<!><!>>, B<A>>>() : B<<!NI;UPPER_BOUND_VIOLATED!><!UPPER_BOUND_VIOLATED!>Any<!><!>>() { // 2 errors
    val a = B<<!NI;UPPER_BOUND_VIOLATED!><!UPPER_BOUND_VIOLATED!>Char<!><!>>() // error

    abstract val x :  (B<<!NI;UPPER_BOUND_VIOLATED!><!UPPER_BOUND_VIOLATED!>Char<!><!>>) -> B<<!NI;UPPER_BOUND_VIOLATED!><!UPPER_BOUND_VIOLATED!>Any<!><!>>
  }


fun test() {
    foo<<!NI;UPPER_BOUND_VIOLATED!><!UPPER_BOUND_VIOLATED!>Int?<!><!>>()
    foo<Int>()
    bar<Int?>()
    bar<Int>()
    bar<<!NI;UPPER_BOUND_VIOLATED!><!UPPER_BOUND_VIOLATED!>Double?<!><!>>()
    bar<<!NI;UPPER_BOUND_VIOLATED!><!UPPER_BOUND_VIOLATED!>Double<!><!>>()
    1.buzz<<!NI;UPPER_BOUND_VIOLATED!><!UPPER_BOUND_VIOLATED!>Double<!><!>>()
}

fun <T : Any> foo() {}
fun <T : Int?> bar() {}
fun <T : <!NI;FINAL_UPPER_BOUND!><!FINAL_UPPER_BOUND!>Int<!><!>> Int.buzz() : Unit {}
