package silklang.silk.utility

object BooleanType {
    const val True = -1
    const val False = 0

    const val TrueString = "True"
    const val FalseString = "False"

    fun isTrue(value: Int): Boolean = value != False
    fun isFalse(value: Int): Boolean = value == False

    fun isTrue(value: String): Boolean {
        if ((value.toIntOrNull() ?: 0) != 0) return true
        return value.equals(TrueString, true) || value.equals("Yes", true)
    }

    fun isFalse(value: String): Boolean = !isTrue(value)
}