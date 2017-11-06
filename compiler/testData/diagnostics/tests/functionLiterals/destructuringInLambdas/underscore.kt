// !WITH_NEW_INFERENCE
// !CHECK_TYPE
// !DIAGNOSTICS: -UNUSED_PARAMETER
data class A(val x: Int, val y: String)
data class B(val u: Double, val w: Short)

fun foo(block: (A) -> Unit) { }

fun bar() {
    foo { (_, b) ->
        <!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>_<!><!>.<!NI;DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!><!DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!>hashCode<!><!>()
        b checkType { _<String>() }
    }

    foo { (a, _) ->
        a checkType { _<Int>() }
        <!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>_<!><!>.<!NI;DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!><!DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!>hashCode<!><!>()
    }

    foo { (_, _) ->
        <!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>_<!><!>.<!NI;DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!><!DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!>hashCode<!><!>()
    }

    foo { (_: Int, b: String) ->
        <!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>_<!><!>.<!NI;DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!><!DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!>hashCode<!><!>()
        b checkType { _<String>() }
    }

    foo { (a: Int, _: String) ->
        a checkType { _<Int>() }
        <!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>_<!><!>.<!NI;DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!><!DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!>hashCode<!><!>()
    }

    foo { (_: Int, _: String) ->
        <!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>_<!><!>.<!NI;DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!><!DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!>hashCode<!><!>()
    }

    foo { (_, _): A ->
        <!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>_<!><!>.<!NI;DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!><!DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!>hashCode<!><!>()
    }

    foo { (`_`, _) ->
        _ checkType { _<Int>() }
    }

    foo { (_, `_`) ->
        _ checkType { _<String>() }
    }

    foo { (<!NI;REDECLARATION!><!NI;UNUSED_DESTRUCTURED_PARAMETER_ENTRY!><!REDECLARATION!><!UNUSED_DESTRUCTURED_PARAMETER_ENTRY!>`_`<!><!><!><!>, <!NI;REDECLARATION!><!REDECLARATION!>`_`<!><!>) ->
        _ checkType { _<String>() }
    }

    foo { (<!NI;COMPONENT_FUNCTION_RETURN_TYPE_MISMATCH!><!COMPONENT_FUNCTION_RETURN_TYPE_MISMATCH!>_: String<!><!>, b) ->
        <!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>_<!><!>.<!NI;DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!><!DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!>hashCode<!><!>()
        b checkType { _<String>() }
    }

    foo <!NI;TYPE_MISMATCH!>{ <!EXPECTED_PARAMETER_TYPE_MISMATCH!>(_, b): B<!> ->
        <!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>_<!><!>.<!NI;DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!><!DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!>hashCode<!><!>()
        b checkType { _<Short>() }
    }<!>
}
