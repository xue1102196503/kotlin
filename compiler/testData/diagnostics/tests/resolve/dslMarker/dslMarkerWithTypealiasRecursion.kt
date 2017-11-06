// !WITH_NEW_INFERENCE
@DslMarker
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPEALIAS)
annotation class XMarker

@XMarker
class Foo

class Bar

typealias YBar = <!NI;RECURSIVE_TYPEALIAS_EXPANSION!><!RECURSIVE_TYPEALIAS_EXPANSION!>ZBar<!><!>
typealias ZBar = <!NI;RECURSIVE_TYPEALIAS_EXPANSION!><!RECURSIVE_TYPEALIAS_EXPANSION!>YBar<!><!>

fun Foo.foo(body: Foo.() -> Unit) = body()
fun Foo.zbar(<!UNUSED_PARAMETER!>body<!>: <!NI;RECURSIVE_TYPEALIAS_EXPANSION!><!RECURSIVE_TYPEALIAS_EXPANSION!>ZBar<!><!>.() -> Unit) = Bar().<!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>body<!>()

fun test() {
    Foo().foo {
        zbar {
            <!NI;DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!><!DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!>foo<!><!> {}
        }
    }
}