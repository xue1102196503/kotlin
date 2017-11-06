// !WITH_NEW_INFERENCE
// !API_VERSION: 1.0
// !DIAGNOSTICS: -UNUSED_PARAMETER, -UNUSED_VARIABLE, -EXTENSION_SHADOWED_BY_MEMBER

class Foo {
    operator fun rem(x: Int): Foo = Foo()
}

class Bar {
    operator fun remAssign(x: Int) {}
}

class Baz {
    companion object {
        operator fun rem(x: Int) {}
        operator fun Int.rem(x: Int) {}
    }
}

operator fun Baz.rem(x: Int) {}

fun local() {
    operator fun Int.rem(x: Int) {}
    operator fun String.remAssign(x: Int) {}
}

class WithMod {
    <!NI;DEPRECATED_BINARY_MOD!><!DEPRECATED_BINARY_MOD!>operator<!><!> fun mod(other: WithMod) = this

    fun test() {
        val a = <!NI;TYPE_MISMATCH!>this<!> <!DEPRECATED_BINARY_MOD_AS_REM!>%<!> <!NI;TYPE_MISMATCH!>this<!>
        var b = this.mod(this)
        <!NI;TYPE_MISMATCH!>b<!> <!DEPRECATED_BINARY_MOD_AS_REM!>%=<!> <!NI;TYPE_MISMATCH!>this<!>
    }
}

fun noOverflow() {
    (-1).mod(5)
}

fun builtIns(b: Byte, s: Short) {
    var a = <!NI;CONSTANT_EXPECTED_TYPE_MISMATCH!>1<!> % 2
    <!NI;DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!>a<!> <!NI;ASSIGN_OPERATOR_AMBIGUITY!>%=<!> 3
    1.mod(2)
    <!NI;TYPE_MISMATCH!>b<!> % <!NI;TYPE_MISMATCH!>s<!>
    <!NI;CONSTANT_EXPECTED_TYPE_MISMATCH!>1.0<!> % <!NI;CONSTANT_EXPECTED_TYPE_MISMATCH!>2.0<!>
}