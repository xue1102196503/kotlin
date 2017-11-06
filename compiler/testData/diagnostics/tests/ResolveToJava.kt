// !WITH_NEW_INFERENCE
// !CHECK_TYPE

// FILE: f.kt

import java.*
import java.util.*
import <!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>utils<!><!>.*

import java.io.PrintStream
import <!NI;PLATFORM_CLASS_MAPPED_TO_KOTLIN!><!PLATFORM_CLASS_MAPPED_TO_KOTLIN!>java.lang.Comparable<!><!> as Com

val l : MutableList<in Int> = ArrayList<Int>()

fun test(<!NI;UNUSED_PARAMETER!><!UNUSED_PARAMETER!>l<!><!> : <!NI;PLATFORM_CLASS_MAPPED_TO_KOTLIN!><!PLATFORM_CLASS_MAPPED_TO_KOTLIN!>java.util.List<Int><!><!>) {
  val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>x<!><!> : java.<!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>List<!><!>
  val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>y<!><!> : <!NI;PLATFORM_CLASS_MAPPED_TO_KOTLIN!><!PLATFORM_CLASS_MAPPED_TO_KOTLIN!>java.util.List<Int><!><!>
  val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>b<!><!> : <!NI;PLATFORM_CLASS_MAPPED_TO_KOTLIN!><!PLATFORM_CLASS_MAPPED_TO_KOTLIN!>java.lang.Object<!><!>
  val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>z<!><!> : java.<!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>utils<!><!>.<!NI;DEBUG_INFO_MISSING_UNRESOLVED!><!DEBUG_INFO_MISSING_UNRESOLVED!>List<!><!><Int>

  val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>f<!><!> : java.io.File? = null

  Collections.<!NI;FUNCTION_CALL_EXPECTED!><!TYPE_INFERENCE_NO_INFORMATION_FOR_PARAMETER!><!FUNCTION_CALL_EXPECTED!>emptyList<!><!><!>
  Collections.<!NI;FUNCTION_CALL_EXPECTED!><!FUNCTION_CALL_EXPECTED!>emptyList<Int><!><!>
  Collections.emptyList<Int>()
  Collections.<!TYPE_INFERENCE_NO_INFORMATION_FOR_PARAMETER!>emptyList<!>()

  checkSubtype<Set<Int>?>(Collections.singleton<Int>(1))
  Collections.singleton<Int>(<!NI;CONSTANT_EXPECTED_TYPE_MISMATCH!><!CONSTANT_EXPECTED_TYPE_MISMATCH!>1.0<!><!>)

  <!NI;RESOLUTION_TO_CLASSIFIER!><!RESOLUTION_TO_CLASSIFIER!>List<!><!><Int>


  val <!NI;UNUSED_VARIABLE!><!UNUSED_VARIABLE!>o<!><!> = "sdf" as <!NI;PLATFORM_CLASS_MAPPED_TO_KOTLIN!><!PLATFORM_CLASS_MAPPED_TO_KOTLIN!>Object<!><!>

  try {
    // ...
  }
  catch(e: Exception) {
    System.out.println(e.message)
  }

  PrintStream("sdf")

  val c : <!NI;PLATFORM_CLASS_MAPPED_TO_KOTLIN!><!PLATFORM_CLASS_MAPPED_TO_KOTLIN!>Com<Int><!><!>? = null

  checkSubtype<<!NI;PLATFORM_CLASS_MAPPED_TO_KOTLIN!><!PLATFORM_CLASS_MAPPED_TO_KOTLIN!>java.lang.Comparable<Int><!><!>?>(c)

//  Collections.sort<Integer>(ArrayList<Integer>())
  xxx.<!NI;UNRESOLVED_REFERENCE!><!UNRESOLVED_REFERENCE!>Class<!><!>()
}


// FILE: f.kt
package xxx
  import java.lang.Class;