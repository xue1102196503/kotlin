// !WITH_NEW_INFERENCE

// KT-9078 (NPE in control flow analysis); EA-71535
abstract class KFunctionKt9005WorkAround<out R: Any?>(private val _functionInstance: Function<R>) {
    private val _reflectedFunction: kotlin.reflect.KFunction<R> = _functionInstance.<!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>reflect<!><!>() ?: throw IllegalStateException("")

    private val _parameters: List<kotlin.reflect.KParameter> = <!NI;TYPE_MISMATCH!>run {
        _functionInstance.javaClass.methods.first().<!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>parameters<!><!>.<!NI;DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!><!DEBUG_INFO_ELEMENT_WITH_ERROR_TYPE!>map<!><!> {
            <!NI;ABSTRACT_MEMBER_NOT_IMPLEMENTED!><!ABSTRACT_MEMBER_NOT_IMPLEMENTED!>object<!><!> : kotlin.reflect.KParameter {
                override val index: Int = 0
            }
        }
    }<!>
}
