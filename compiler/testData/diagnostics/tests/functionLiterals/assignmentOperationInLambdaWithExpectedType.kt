// !WITH_NEW_INFERENCE
fun test(bal: Array<Int>) {
    var bar = 4

    val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>a<!><!>: () -> Unit = { bar += 4 }

    val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>b<!><!>: () -> Int = { <!NI;EXPECTED_TYPE_MISMATCH!><!EXPECTED_TYPE_MISMATCH!>bar = 4<!><!> }

    val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>c<!><!>: () -> <!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>UNRESOLVED<!><!> = { bal[2] = 3 }

    val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>d<!><!>: () -> Int = { <!NI;ASSIGNMENT_TYPE_MISMATCH!><!ASSIGNMENT_TYPE_MISMATCH(Int)!>bar += 4<!><!> }

    val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>e<!><!>: Unit = run { bar += 4 }

    val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>f<!><!>: Int = <!NI;TYPE_MISMATCH!>run { <!ASSIGNMENT_TYPE_MISMATCH!>bar += 4<!> }<!>
}
