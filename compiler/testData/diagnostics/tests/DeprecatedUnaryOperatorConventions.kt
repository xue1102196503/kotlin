// !WITH_NEW_INFERENCE
// !DIAGNOSTICS: -UNUSED_PARAMETER

class Example {
    <!NI;INAPPLICABLE_OPERATOR_MODIFIER!><!INAPPLICABLE_OPERATOR_MODIFIER!>operator<!><!> fun plus(): String = ""
    operator fun unaryPlus(): Int = 0
}

class ExampleDeprecated {
    <!NI;INAPPLICABLE_OPERATOR_MODIFIER!><!INAPPLICABLE_OPERATOR_MODIFIER!>operator<!><!> fun plus(): String = ""
}

<!NI;INAPPLICABLE_OPERATOR_MODIFIER!><!INAPPLICABLE_OPERATOR_MODIFIER!>operator<!><!> fun String.plus(): String = this
operator fun String.unaryPlus(): Int = 0

fun test() {
    requireInt(+ "")
    requireInt(+ Example())
    requireString(<!NI;UNRESOLVED_REFERENCE_WRONG_RECEIVER!><!NI;DEBUG_INFO_UNRESOLVED_WITH_TARGET!><!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>+<!><!><!> ExampleDeprecated())
}

fun requireInt(n: Int) {}
fun requireString(s: String) {}

class Example2 {
    <!NI;INAPPLICABLE_OPERATOR_MODIFIER!><!INAPPLICABLE_OPERATOR_MODIFIER!>operator<!><!> fun plus() = this
    <!NI;INAPPLICABLE_OPERATOR_MODIFIER!><!INAPPLICABLE_OPERATOR_MODIFIER!>operator<!><!> fun minus() = this

    fun test() {
        <!NI;UNRESOLVED_REFERENCE_WRONG_RECEIVER!><!NI;DEBUG_INFO_UNRESOLVED_WITH_TARGET!><!UNRESOLVED_REFERENCE_WRONG_RECEIVER!>+<!><!><!>this
        <!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>-<!><!>this
    }
}
