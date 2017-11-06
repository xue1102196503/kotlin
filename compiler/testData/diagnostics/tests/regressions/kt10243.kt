// !WITH_NEW_INFERENCE
val f: Boolean = true
private fun doUpdateRegularTasks() {
    try {
        while (f) {
            val xmlText = <!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>getText<!><!>()
            if (<!NI;DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!><!DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!>xmlText<!><!> <!NI;RESULT_TYPE_MISMATCH!><!NI;DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!><!DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!>==<!><!><!> null) {}
            else {
                <!NI;DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!><!DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!>xmlText<!><!>.<!NI;DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!><!DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!>value<!><!> = 0 // !!!
            }
        }

    }
    finally {
        fun execute() {}
    }
}
