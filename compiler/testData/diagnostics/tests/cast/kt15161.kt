// !WITH_NEW_INFERENCE
class Array<E>(e: E) {
    val k = Array(1) {
        1 <!NI;USELESS_CAST!><!USELESS_CAST!>as Any<!><!>
        e <!NI;USELESS_CAST!>as Any?<!>
    }
}