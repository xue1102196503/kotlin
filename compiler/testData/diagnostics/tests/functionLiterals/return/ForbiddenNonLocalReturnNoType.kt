// !WITH_NEW_INFERENCE
fun test() {
    run {<!NI;RETURN_NOT_ALLOWED!><!RETURN_NOT_ALLOWED!>return<!><!>}
    <!NI;UNREACHABLE_CODE!>run {}<!>
}

fun test2() {
    run {<!NI;RETURN_NOT_ALLOWED!><!RETURN_NOT_ALLOWED!>return@test2<!><!>}
    <!NI;UNREACHABLE_CODE!>run {}<!>
}

fun test3() {
    fun test4() {
        run {<!NI;RETURN_NOT_ALLOWED!><!RETURN_NOT_ALLOWED!>return@test3<!><!>}
        <!NI;UNREACHABLE_CODE!>run {}<!>
    }
}

fun <T> run(f: () -> T): T { return f() }
