// !WITH_NEW_INFERENCE
enum class E : Cloneable {
    A;
    <!NI;OVERRIDING_FINAL_MEMBER!><!OVERRIDING_FINAL_MEMBER!>override<!><!> fun clone(): Any {
        return <!NI;TYPE_MISMATCH!><!NI;AMBIGUOUS_SUPER!><!AMBIGUOUS_SUPER!>super<!><!>.<!NI;DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!><!DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!>clone<!><!>()<!>
    }
}
