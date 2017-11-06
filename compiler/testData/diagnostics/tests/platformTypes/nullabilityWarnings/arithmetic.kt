// !WITH_NEW_INFERENCE
// FILE: J.java

import org.jetbrains.annotations.*;

public class J {
    @NotNull
    public static Integer staticNN;
    @Nullable
    public static Integer staticN;
    public static Integer staticJ;
}

// FILE: k.kt

fun test() {
    // @NotNull platform type
    var platformNN = J.staticNN
    // @Nullable platform type
    var platformN = J.staticN
    // platform type with no annotation
    var platformJ = J.staticJ

    +platformNN
    <!NI;UNSAFE_CALL!><!UNSAFE_CALL!>+<!><!>platformN
    +platformJ

    ++platformNN
    <!NI;UNSAFE_CALL!><!UNSAFE_CALL!>++<!><!>platformN
    ++platformJ

    platformNN++
    platformN<!NI;UNSAFE_CALL!><!UNSAFE_CALL!>++<!><!>
    platformJ++

    1 + platformNN
    1 <!NI;NONE_APPLICABLE!>+<!> <!TYPE_MISMATCH!>platformN<!>
    1 + platformJ

    platformNN + 1
    platformN <!NI;UNRESOLVED_REFERENCE_WRONG_RECEIVER!><!NI;DEBUG_INFO_UNRESOLVED_WITH_TARGET!><!UNSAFE_OPERATOR_CALL!>+<!><!><!> 1
    platformJ + 1

    1 <!NI;INFIX_MODIFIER_REQUIRED!><!INFIX_MODIFIER_REQUIRED!>plus<!><!> platformNN
    1 <!NI;NONE_APPLICABLE!><!INFIX_MODIFIER_REQUIRED!>plus<!><!> <!TYPE_MISMATCH!>platformN<!>
    1 <!NI;INFIX_MODIFIER_REQUIRED!><!INFIX_MODIFIER_REQUIRED!>plus<!><!> platformJ

    platformNN <!NI;INFIX_MODIFIER_REQUIRED!><!INFIX_MODIFIER_REQUIRED!>plus<!><!> 1
    platformN <!NI;UNSAFE_INFIX_CALL!><!NI;INFIX_MODIFIER_REQUIRED!><!UNSAFE_INFIX_CALL!><!INFIX_MODIFIER_REQUIRED!>plus<!><!><!><!> 1
    platformJ <!NI;INFIX_MODIFIER_REQUIRED!><!INFIX_MODIFIER_REQUIRED!>plus<!><!> 1

    platformNN += 1
    platformN <!NI;UNRESOLVED_REFERENCE_WRONG_RECEIVER!><!NI;DEBUG_INFO_UNRESOLVED_WITH_TARGET!><!UNSAFE_OPERATOR_CALL!>+=<!><!><!> 1
    platformJ += 1
}