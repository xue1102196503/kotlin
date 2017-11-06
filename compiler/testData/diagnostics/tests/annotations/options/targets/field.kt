// !WITH_NEW_INFERENCE
@Target(AnnotationTarget.FIELD) 
annotation class Field

<!NI;WRONG_ANNOTATION_TARGET!><!WRONG_ANNOTATION_TARGET!>@Field<!><!>
annotation class Another

@Field
val x: Int = 42

<!NI;WRONG_ANNOTATION_TARGET!><!WRONG_ANNOTATION_TARGET!>@Field<!><!>
val y: Int
    get() = 13

<!NI;WRONG_ANNOTATION_TARGET!><!WRONG_ANNOTATION_TARGET!>@Field<!><!>
abstract class My(<!NI;WRONG_ANNOTATION_TARGET!><!WRONG_ANNOTATION_TARGET!>@Field<!><!> arg: Int, @Field val w: Int) {
    @Field
    val x: Int = arg

    <!NI;WRONG_ANNOTATION_TARGET!><!WRONG_ANNOTATION_TARGET!>@Field<!><!>
    val y: Int
        get() = 0

    <!NI;WRONG_ANNOTATION_TARGET!><!WRONG_ANNOTATION_TARGET!>@Field<!><!>
    abstract val z: Int

    <!NI;WRONG_ANNOTATION_TARGET!><!WRONG_ANNOTATION_TARGET!>@Field<!><!>
    fun foo() {}

    <!NI;WRONG_ANNOTATION_TARGET!><!WRONG_ANNOTATION_TARGET!>@Field<!><!>
    val v: Int by <!NI;DELEGATE_SPECIAL_FUNCTION_MISSING!><!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>Delegates<!><!>.<!NI;DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!><!DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!>lazy<!><!> { 42 }<!>
}

enum class Your {
    @Field FIRST
}

interface His {
    <!NI;WRONG_ANNOTATION_TARGET!><!WRONG_ANNOTATION_TARGET!>@Field<!><!>
    val x: Int

    <!NI;WRONG_ANNOTATION_TARGET!><!WRONG_ANNOTATION_TARGET!>@Field<!><!>
    val y: Int
        get() = 42
}
