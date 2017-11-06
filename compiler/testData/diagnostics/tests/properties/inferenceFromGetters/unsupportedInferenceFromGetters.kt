// !WITH_NEW_INFERENCE
// !LANGUAGE: -ShortSyntaxForPropertyGetters

// blockBodyGetter.kt
<!NI;UNSUPPORTED_FEATURE!><!UNSUPPORTED_FEATURE!>val x get() {
    return 1
}<!><!>

// cantBeInferred.kt
<!NI;UNSUPPORTED_FEATURE!><!UNSUPPORTED_FEATURE!>val <!NI;IMPLICIT_NOTHING_PROPERTY_TYPE!>x1<!> get() = <!TYPE_INFERENCE_NO_INFORMATION_FOR_PARAMETER!>foo<!>()<!><!>
<!NI;UNSUPPORTED_FEATURE!><!UNSUPPORTED_FEATURE!>val y1 get() = <!TYPE_INFERENCE_NO_INFORMATION_FOR_PARAMETER!>bar<!>()<!><!>

fun <E> foo(): E = null!!
fun <E> bar(): List<E> = null!!

// explicitGetterType.kt
<!NI;UNSUPPORTED_FEATURE!><!UNSUPPORTED_FEATURE!>val x2 get(): String = foo()<!><!>
<!NI;UNSUPPORTED_FEATURE!><!UNSUPPORTED_FEATURE!>val y2 get(): List<Int> = bar()<!><!>
<!NI;UNSUPPORTED_FEATURE!><!UNSUPPORTED_FEATURE!>val z2 get(): List<Int> {
    return bar()
}<!><!>

<!NI;MUST_BE_INITIALIZED!><!MUST_BE_INITIALIZED!>val u<!><!> get(): String = field

// members.kt
class A {
    <!NI;UNSUPPORTED_FEATURE!><!UNSUPPORTED_FEATURE!>val x get() = 1<!><!>
    <!NI;UNSUPPORTED_FEATURE!><!UNSUPPORTED_FEATURE!>val y get() = id(1)<!><!>
    <!NI;UNSUPPORTED_FEATURE!><!UNSUPPORTED_FEATURE!>val y2 get() = id(id(2))<!><!>
    <!NI;UNSUPPORTED_FEATURE!><!UNSUPPORTED_FEATURE!>val z get() = l("")<!><!>
    <!NI;UNSUPPORTED_FEATURE!><!UNSUPPORTED_FEATURE!>val z2 get() = l(id(l("")))<!><!>

    <!NI;UNSUPPORTED_FEATURE!><!UNSUPPORTED_FEATURE!>val <T> T.u get() = id(this)<!><!>
}
fun <E> id(x: E) = x
fun <E> l(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>x<!><!>: E): List<E> = null!!

// vars
<!NI;UNSUPPORTED_FEATURE!><!UNSUPPORTED_FEATURE!>var x3
    get() = 1
    set(q) {
    }<!><!>

// recursive
<!NI;UNSUPPORTED_FEATURE!><!UNSUPPORTED_FEATURE!>val x4 get() = <!NI;TYPECHECKER_HAS_RUN_INTO_RECURSIVE_PROBLEM!><!NI;DEBUG_INFO_MISSING_UNRESOLVED!><!TYPECHECKER_HAS_RUN_INTO_RECURSIVE_PROBLEM!>x4<!><!><!><!><!>

// null as nothing
<!NI;UNSUPPORTED_FEATURE!><!UNSUPPORTED_FEATURE!>val x5 get() = null<!><!>
<!NI;UNSUPPORTED_FEATURE!><!UNSUPPORTED_FEATURE!>val <!NI;IMPLICIT_NOTHING_PROPERTY_TYPE!><!IMPLICIT_NOTHING_PROPERTY_TYPE!>y5<!><!> get() = null!!<!><!>

// objectExpression.kt
object Outer {
    <!NI;UNSUPPORTED_FEATURE!><!UNSUPPORTED_FEATURE!>private var x
        get() = object : CharSequence {
            override val length: Int
                get() = 0

            override fun get(index: Int): Char {
                return ' '
            }

            override fun subSequence(startIndex: Int, endIndex: Int) = ""

            fun bar() {
            }
        }
        set(q) {
            x = q
        }<!><!>
}