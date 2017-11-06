// !WITH_NEW_INFERENCE
// KT-2286 Improve error message for nullability check failure for extension methods

package n

abstract class Buggy {

    abstract val coll : Collection<Int>

    fun getThree(): Int? {
        return coll.find{ it > 3 }  // works fine
    }

    val anotherThree : Int
        get() = <!NI;TYPE_MISMATCH!>coll.<!TYPE_INFERENCE_EXPECTED_TYPE_MISMATCH!>find{ it > 3 }<!><!> // does not work here

    val yetAnotherThree : Int
        get() = <!NI;TYPE_MISMATCH!>coll.<!TYPE_INFERENCE_EXPECTED_TYPE_MISMATCH!>find({ v:Int -> v > 3 })<!><!> // neither here

    val extendedGetter : Int
        get() {
            return <!NI;TYPE_MISMATCH!>coll.<!TYPE_INFERENCE_EXPECTED_TYPE_MISMATCH!>find{ it > 3 }<!><!>  // not even here!
        }

}

//from library
fun <T: Any> Iterable<T>.find(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>predicate<!><!>: (T) -> Boolean) : T? {<!NI;NO_RETURN_IN_FUNCTION_WITH_BLOCK_BODY!><!NO_RETURN_IN_FUNCTION_WITH_BLOCK_BODY!>}<!><!>