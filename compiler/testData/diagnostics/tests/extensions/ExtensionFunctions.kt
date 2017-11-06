// !WITH_NEW_INFERENCE
// FILE: b.kt
package outer

fun Int?.optint() : Unit {}
val Int?.optval : Unit get() = Unit

fun <T: Any, E> T.foo(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>x<!><!> : E, y : A) : T   {
  y.plus(1)
  y plus 1
  y + 1.0

  this<!NI;UNNECESSARY_SAFE_CALL!><!UNNECESSARY_SAFE_CALL!>?.<!><!>minus<T>(this)

  return this
}

class A

infix operator fun A.plus(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>a<!><!> : Any) {

  1.foo()
  true.<!NI;UNRESOLVED_REFERENCE_WRONG_RECEIVER!><!NI;DEBUG_INFO_UNRESOLVED_WITH_TARGET!><!TYPE_INFERENCE_NO_INFORMATION_FOR_PARAMETER!>foo<!><!><!>(<!NO_VALUE_FOR_PARAMETER!><!NO_VALUE_FOR_PARAMETER!>)<!><!>

  <!NI;UNUSED_EXPRESSION!><!UNUSED_EXPRESSION!>1<!><!>
}

operator fun A.plus(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>a<!><!> : Int) {
  <!NI;UNUSED_EXPRESSION!><!UNUSED_EXPRESSION!>1<!><!>
}

operator fun <T> T.minus(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>t<!><!> : T) : Int = 1

fun test() {
  val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>y<!><!> = 1.abs
}
val Int.abs : Int
  get() = if (this > 0) this else -this;

<!NI;EXTENSION_PROPERTY_MUST_HAVE_ACCESSORS_OR_BE_ABSTRACT!><!EXTENSION_PROPERTY_MUST_HAVE_ACCESSORS_OR_BE_ABSTRACT!>val <T> T.foo : T<!><!>

fun Int.foo() = this

// FILE: b.kt
package null_safety

import outer.*

        fun parse(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>cmd<!><!>: String): Command? { return null  }
        class Command() {
        //  fun equals(other : Any?) : Boolean
          val foo : Int = 0
        }

        fun Any.<!NI;EXTENSION_SHADOWED_BY_MEMBER!><!EXTENSION_SHADOWED_BY_MEMBER!>equals<!><!>(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>other<!><!> : Any?) : Boolean = true
        fun Any?.equals1(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>other<!><!> : Any?) : Boolean = true
        fun Any.equals2(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>other<!><!> : Any?) : Boolean = true

        fun main(args: Array<String>) {

            System.out.print(1)

            val command = parse("")

            command.foo

            command<!NI;UNSAFE_CALL!><!UNSAFE_CALL!>.<!><!>equals(null)
            command?.equals(null)
            command.equals1(null)
            command?.equals1(null)

            val c = Command()
            c<!NI;UNNECESSARY_SAFE_CALL!><!UNNECESSARY_SAFE_CALL!>?.<!><!>equals2(null)

            if (command == null) <!NI;UNUSED_EXPRESSION!><!UNUSED_EXPRESSION!>1<!><!>
        }
