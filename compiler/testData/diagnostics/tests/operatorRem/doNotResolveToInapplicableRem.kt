// !WITH_NEW_INFERENCE
// !DIAGNOSTICS: -UNUSED_PARAMETER

object OldMod {
    <!NI;DEPRECATED_BINARY_MOD!><!DEPRECATED_BINARY_MOD!>operator<!><!> fun mod(x: Int) {}
}

object RemExtension
operator fun RemExtension.rem(x: Int) {}

fun foo() {
    <!NI;TYPE_MISMATCH!>OldMod<!> <!DEPRECATED_BINARY_MOD_AS_REM!>%<!> 123
}