// !WITH_NEW_INFERENCE
val test1: (String) -> Boolean =
        when {
            true -> {{ true }}
            else -> {{ false }}
        }

val test2: (String) -> Boolean =
        when {
            true -> {{ true }}
            else -> null!!
        }

val test3: (String) -> Boolean =
        when {
            true -> { <!NI;UNUSED_ANONYMOUS_PARAMETER!><!UNUSED_ANONYMOUS_PARAMETER!>s<!><!> -> true }
            else -> null!!
        }

val test4: (String) -> Boolean =
        when {
            true -> <!NI;TYPE_MISMATCH!>{ <!EXPECTED_PARAMETERS_NUMBER_MISMATCH!><!NI;UNUSED_ANONYMOUS_PARAMETER!><!UNUSED_ANONYMOUS_PARAMETER!>s1<!><!>, <!NI;UNUSED_ANONYMOUS_PARAMETER!><!CANNOT_INFER_PARAMETER_TYPE!><!UNUSED_ANONYMOUS_PARAMETER!>s2<!><!><!><!> -> true }<!>
            else -> null!!
        }

