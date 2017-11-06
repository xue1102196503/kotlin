// !WITH_NEW_INFERENCE
fun bar(x: Int) = x + 1

fun f1(x: Int?) {
    bar(<!NI;TYPE_MISMATCH!><!TYPE_MISMATCH!>x<!><!>)
    if (x != null) bar(x<!NI;UNNECESSARY_NOT_NULL_ASSERTION!><!UNNECESSARY_NOT_NULL_ASSERTION!>!!<!><!>)
    if (x == null) <!UNREACHABLE_CODE!>bar(<!><!NI;ALWAYS_NULL!><!ALWAYS_NULL!>x<!><!>!!<!UNREACHABLE_CODE!>)<!>
}

fun f2(x: Int?) {    
    if (x != null) else <!NI;ALWAYS_NULL!><!ALWAYS_NULL!>x<!><!>!!
}

fun f3(x: Int?) {    
    if (x != null) bar(x<!NI;UNNECESSARY_NOT_NULL_ASSERTION!><!UNNECESSARY_NOT_NULL_ASSERTION!>!!<!><!>) else <!NI;ALWAYS_NULL!><!ALWAYS_NULL!>x<!><!>!!
}
    
fun f4(x: Int?) {    
    if (x == null) <!NI;ALWAYS_NULL!><!ALWAYS_NULL!>x<!><!>!! else bar(x<!NI;UNNECESSARY_NOT_NULL_ASSERTION!><!UNNECESSARY_NOT_NULL_ASSERTION!>!!<!><!>)
}

fun f5(x: Int?) {    
    if (x == null) else bar(x<!NI;UNNECESSARY_NOT_NULL_ASSERTION!><!UNNECESSARY_NOT_NULL_ASSERTION!>!!<!><!>)
}
