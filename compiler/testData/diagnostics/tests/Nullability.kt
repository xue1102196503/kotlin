// !WITH_NEW_INFERENCE
fun test() {
  val a : Int? = 0
  if (a != null) {
    <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>a<!><!>.plus(1)
  }
  else {
    <!NI;DEBUG_INFO_CONSTANT!><!DEBUG_INFO_CONSTANT!>a<!><!>?.plus(1)
  }

  val out : java.io.PrintStream? = null
  val ins : java.io.InputStream? = null

  out?.println()
  ins?.read()

  if (ins != null) {
    <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>ins<!><!>.read()
    out?.println()
    if (out != null) {
      <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>ins<!><!>.read();
      <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>out<!><!>.println();
    }
  }

  if (out != null && ins != null) {
    <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>ins<!><!>.read();
    <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>out<!><!>.println();
  }

  if (out == null) {
    <!NI;DEBUG_INFO_CONSTANT!><!DEBUG_INFO_CONSTANT!>out<!><!>?.println()
  } else {
    <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>out<!><!>.println()
  }

  if (out != null && ins != null || out != null) {
    ins?.read();
    ins<!NI;UNSAFE_CALL!><!UNSAFE_CALL!>.<!><!>read();
    <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>out<!><!>.println();
  }

  if (out == null || <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>out<!><!>.println(0) == Unit) {
    out?.println(1)
    out<!NI;UNSAFE_CALL!><!UNSAFE_CALL!>.<!><!>println(1)
  }
  else {
    <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>out<!><!>.println(2)
  }

  if (out != null && <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>out<!><!>.println() == Unit) {
    <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>out<!><!>.println();
  }
  else {
    out?.println();
  }

  if (out == null || <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>out<!><!>.println() == Unit) {
    out?.println();
  }
  else {
    <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>out<!><!>.println();
  }

  if (1 == 2 || out != null && <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>out<!><!>.println(1) == Unit) {
    out?.println(2);
    out<!NI;UNSAFE_CALL!><!UNSAFE_CALL!>.<!><!>println(2);
  }
  else {
    out?.println(3)
    out<!NI;UNSAFE_CALL!><!UNSAFE_CALL!>.<!><!>println(3)
  }

  out?.println()
  ins?.read()

  if (ins != null) {
    <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>ins<!><!>.read()
    out?.println()
    if (out != null) {
      <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>ins<!><!>.read();
      <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>out<!><!>.println();
    }
  }

  if (out != null && ins != null) {
    <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>ins<!><!>.read();
    <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>out<!><!>.println();
  }

  if (out == null) {
    <!NI;DEBUG_INFO_CONSTANT!><!DEBUG_INFO_CONSTANT!>out<!><!>?.println()
  } else {
    <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>out<!><!>.println()
  }

  if (out != null && ins != null || out != null) {
    ins?.read();
    <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>out<!><!>.println();
  }

  if (out == null || <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>out<!><!>.println(0) == Unit) {
    out?.println(1)
    out<!NI;UNSAFE_CALL!><!UNSAFE_CALL!>.<!><!>println(1)
  }
  else {
    <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>out<!><!>.println(2)
  }

  if (out != null && <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>out<!><!>.println() == Unit) {
    <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>out<!><!>.println();
  }
  else {
    out?.println();
    out<!NI;UNSAFE_CALL!><!UNSAFE_CALL!>.<!><!>println();
  }

  if (out == null || <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>out<!><!>.println() == Unit) {
    out?.println();
    out<!NI;UNSAFE_CALL!><!UNSAFE_CALL!>.<!><!>println();
  }
  else {
    <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>out<!><!>.println();
  }

  if (1 == 2 || out != null && <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>out<!><!>.println(1) == Unit) {
    out?.println(2);
    out<!NI;UNSAFE_CALL!><!UNSAFE_CALL!>.<!><!>println(2);
  }
  else {
    out?.println(3)
    out<!NI;UNSAFE_CALL!><!UNSAFE_CALL!>.<!><!>println(3)
  }

  if (1 > 2) {
    if (out == null) return;
    <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>out<!><!>.println();
  }
  out?.println();

  while (out != null) {
    <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>out<!><!>.println();
  }
  <!NI;DEBUG_INFO_CONSTANT!><!DEBUG_INFO_CONSTANT!>out<!><!>?.println();

  val out2 : java.io.PrintStream? = null
  
  while (out2 == null) {
    <!NI;DEBUG_INFO_CONSTANT!><!DEBUG_INFO_CONSTANT!>out2<!><!>?.println();
    <!DEBUG_INFO_CONSTANT!>out2<!><!NI;UNSAFE_CALL!><!UNSAFE_CALL!>.<!><!>println();
  }
  <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>out2<!><!>.println()

}


fun f(out : String?) {
  out?.get(0)
  out<!NI;UNSAFE_CALL!><!UNSAFE_CALL!>.<!><!>get(0)
  if (out != null) else return;
  <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>out<!><!>.get(0)
}

fun f1(out : String?) {
  out?.get(0)
  if (out != null) else {
    1 + 2
    return;
  }
  <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>out<!><!>.get(0)
}

fun f2(out : String?) {
  out?.get(0)
  if (out == null) {
    1 + 2
    return;
  }
  <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>out<!><!>.get(0)
}

fun f3(out : String?) {
  out?.get(0)
  if (out == null) {
    1 + 2
    return;
  }
  else {
    1 + 2
  }
  <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>out<!><!>.get(0)
}

fun f4(s : String?) {
  s?.get(0)
  while (1 < 2 && s != null) {
    <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>s<!><!>.get(0)
  }
  s?.get(0)
  while (s == null || 1 < 2) {
     s?.get(0)
  }
  <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>s<!><!>.get(0)
}

fun f5(s : String?) {
  s?.get(0)
  while (1 < 2 && s != null) {
    <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>s<!><!>.get(0)
  }
  s?.get(0)
  while (s == null || 1 < 2) {
    if (1 > 2) break
     s?.get(0)
  }
  s?.get(0);
}

fun f6(s : String?) {
  s?.get(0)
  do {
    s?.get(0)
    if (1 < 2) break;
  } while (s == null)
  s?.get(0)
  do {
    s?.get(0)
  } while (s == null)
  <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>s<!><!>.get(0)
}

fun f7(s : String?, t : String?) {
  s?.get(0)
  if (!(s == null)) {
    <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>s<!><!>.get(0)
  }
  s?.get(0)
  if (!(s != null)) {
    <!NI;DEBUG_INFO_CONSTANT!><!DEBUG_INFO_CONSTANT!>s<!><!>?.get(0)
  }
  else {
    <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>s<!><!>.get(0)
  }
  s?.get(0)
  if (!!(s != null)) {
    <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>s<!><!>.get(0)
  }
  else {
    <!NI;DEBUG_INFO_CONSTANT!><!DEBUG_INFO_CONSTANT!>s<!><!>?.get(0)
  }
  s?.get(0)
  t?.get(0)
  if (!(s == null || t == null)) {
    <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>s<!><!>.get(0)
    <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>t<!><!>.get(0)
  }
  else {
    s?.get(0)
    t?.get(0)
  }
  s?.get(0)
  t?.get(0)
  if (!(s == null)) {
    <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>s<!><!>.get(0)
    t?.get(0)
  }
  else {
    <!NI;DEBUG_INFO_CONSTANT!><!DEBUG_INFO_CONSTANT!>s<!><!>?.get(0)
    t?.get(0)
  }
}

fun f8(b : String?, a : String) {
  b?.get(0)
  if (b == a) {
    <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>b<!><!>.get(0);
  }
  b?.get(0)
  if (a == b) {
    <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>b<!><!>.get(0)
  }
  if (a != b) {
    b?.get(0)
  }
  else {
    <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>b<!><!>.get(0)
  }
}

fun f9(a : Int?) : Int {
  if (a != null)
    return <!NI;DEBUG_INFO_SMARTCAST!><!DEBUG_INFO_SMARTCAST!>a<!><!>
  return 1
}
