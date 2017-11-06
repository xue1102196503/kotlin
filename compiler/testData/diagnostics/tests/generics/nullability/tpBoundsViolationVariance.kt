// !WITH_NEW_INFERENCE
// !CHECK_TYPE
// !DIAGNOSTICS: -UNUSED_PARAMETER,-UNUSED_VARIABLE

class A<F> {
    class Inv<Q>
    fun <E : Inv<F>> fooInv1(x: E) = x
    fun <E : Inv<F?>> fooInv2(x: E) = x

    class In<in Q>
    fun <E : In<F>> fooIn1(x: E) = x
    fun <E : In<F?>> fooIn2(x: E) = x

    class Out<out Q>
    fun <E : Out<F>> fooOut1(x: E) = x
    fun <E : Out<F?>> fooOut2(x: E) = x

    fun <Z : F, W : Z?> bar() {
        // F
        fooInv1<Inv<F>>(Inv<F>())
        fooInv2<<!NI;UPPER_BOUND_VIOLATED!><!UPPER_BOUND_VIOLATED!>Inv<F><!><!>>(<!NI;TYPE_MISMATCH!><!NI;TYPE_MISMATCH!><!NI;TYPE_MISMATCH!><!NI;TYPE_MISMATCH!>Inv<F>()<!><!><!><!>)
        fooInv1(Inv<F>())
        <!TYPE_INFERENCE_UPPER_BOUND_VIOLATED!>fooInv2<!>(<!NI;TYPE_MISMATCH!><!NI;TYPE_MISMATCH!><!NI;TYPE_MISMATCH!><!NI;TYPE_MISMATCH!>Inv<F>()<!><!><!><!>)

        fooIn1<In<F?>>(In<F?>())
        fooIn2<In<F?>>(In<F?>())
        fooIn1(In<F?>())
        fooIn2(In<F?>())

        fooOut1<Out<F>>(Out<F>())
        fooOut2<Out<F>>(Out<F>())
        fooOut1(Out<F>())
        fooOut2(Out<F>())

        // Z
        fooInv1<<!NI;UPPER_BOUND_VIOLATED!><!UPPER_BOUND_VIOLATED!>Inv<Z><!><!>>(<!NI;TYPE_MISMATCH!><!NI;TYPE_MISMATCH!><!NI;TYPE_MISMATCH!><!NI;TYPE_MISMATCH!>Inv<Z>()<!><!><!><!>)
        fooInv2<<!NI;UPPER_BOUND_VIOLATED!><!UPPER_BOUND_VIOLATED!>Inv<Z><!><!>>(<!NI;TYPE_MISMATCH!><!NI;TYPE_MISMATCH!><!NI;TYPE_MISMATCH!><!NI;TYPE_MISMATCH!>Inv<Z>()<!><!><!><!>)
        <!TYPE_INFERENCE_UPPER_BOUND_VIOLATED!>fooInv1<!>(<!NI;TYPE_MISMATCH!><!NI;TYPE_MISMATCH!><!NI;TYPE_MISMATCH!><!NI;TYPE_MISMATCH!>Inv<Z>()<!><!><!><!>)
        <!TYPE_INFERENCE_UPPER_BOUND_VIOLATED!>fooInv2<!>(<!NI;TYPE_MISMATCH!><!NI;TYPE_MISMATCH!><!NI;TYPE_MISMATCH!><!NI;TYPE_MISMATCH!>Inv<Z>()<!><!><!><!>)

        fooIn1<<!NI;UPPER_BOUND_VIOLATED!><!UPPER_BOUND_VIOLATED!>In<Z?><!><!>>(<!NI;TYPE_MISMATCH!>In<Z?>()<!>)
        fooIn2<<!NI;UPPER_BOUND_VIOLATED!><!UPPER_BOUND_VIOLATED!>In<Z?><!><!>>(<!NI;TYPE_MISMATCH!>In<Z?>()<!>)
        <!TYPE_INFERENCE_UPPER_BOUND_VIOLATED!>fooIn1<!>(<!NI;TYPE_MISMATCH!><!NI;TYPE_MISMATCH!>In<Z?>()<!><!>)
        <!TYPE_INFERENCE_UPPER_BOUND_VIOLATED!>fooIn2<!>(<!NI;TYPE_MISMATCH!><!NI;TYPE_MISMATCH!>In<Z?>()<!><!>)

        fooOut1<Out<Z>>(Out<Z>())
        fooOut2<Out<Z>>(Out<Z>())
        fooOut1(Out<Z>())
        fooOut2(Out<Z>())

        // W
        fooInv1<<!NI;UPPER_BOUND_VIOLATED!><!UPPER_BOUND_VIOLATED!>Inv<W><!><!>>(<!NI;TYPE_MISMATCH!><!NI;TYPE_MISMATCH!><!NI;TYPE_MISMATCH!><!NI;TYPE_MISMATCH!><!NI;TYPE_MISMATCH!>Inv<W>()<!><!><!><!><!>)
        fooInv2<<!NI;UPPER_BOUND_VIOLATED!><!UPPER_BOUND_VIOLATED!>Inv<W><!><!>>(<!NI;TYPE_MISMATCH!><!NI;TYPE_MISMATCH!><!NI;TYPE_MISMATCH!><!NI;TYPE_MISMATCH!>Inv<W>()<!><!><!><!>)
        <!TYPE_INFERENCE_UPPER_BOUND_VIOLATED!>fooInv1<!>(<!NI;TYPE_MISMATCH!><!NI;TYPE_MISMATCH!><!NI;TYPE_MISMATCH!><!NI;TYPE_MISMATCH!><!NI;TYPE_MISMATCH!>Inv<W>()<!><!><!><!><!>)
        <!TYPE_INFERENCE_UPPER_BOUND_VIOLATED!>fooInv2<!>(<!NI;TYPE_MISMATCH!><!NI;TYPE_MISMATCH!><!NI;TYPE_MISMATCH!><!NI;TYPE_MISMATCH!>Inv<W>()<!><!><!><!>)

        fooIn1<<!NI;UPPER_BOUND_VIOLATED!><!UPPER_BOUND_VIOLATED!>In<W?><!><!>>(<!NI;TYPE_MISMATCH!>In<W?>()<!>)
        fooIn2<<!NI;UPPER_BOUND_VIOLATED!><!UPPER_BOUND_VIOLATED!>In<W?><!><!>>(<!NI;TYPE_MISMATCH!>In<W?>()<!>)
        <!TYPE_INFERENCE_UPPER_BOUND_VIOLATED!>fooIn1<!>(<!NI;TYPE_MISMATCH!><!NI;TYPE_MISMATCH!>In<W?>()<!><!>)
        <!TYPE_INFERENCE_UPPER_BOUND_VIOLATED!>fooIn2<!>(<!NI;TYPE_MISMATCH!><!NI;TYPE_MISMATCH!>In<W?>()<!><!>)

        fooOut1<<!NI;UPPER_BOUND_VIOLATED!><!UPPER_BOUND_VIOLATED!>Out<W><!><!>>(<!NI;TYPE_MISMATCH!>Out<W>()<!>)
        fooOut2<Out<W>>(Out<W>())
        <!TYPE_INFERENCE_UPPER_BOUND_VIOLATED!>fooOut1<!>(<!NI;TYPE_MISMATCH!><!NI;TYPE_MISMATCH!>Out<W>()<!><!>)
        fooOut2(Out<W>())
    }
}
