var <caret>someProperty: Int
    get() = someProperty * 2
    public set

fun foo() {
    println(someProperty)
    someProperty = 10
}