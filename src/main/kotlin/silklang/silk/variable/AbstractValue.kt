package silklang.silk.variable

abstract class AbstractValue {
    abstract val varType: VarType
    open val listCount = 1
    open val list: ArrayList<Variable>
        get() = arrayListOf()

    abstract fun toInteger(): Int
    abstract fun toFloat(): Double

    open fun isFloat() = false

    abstract fun add(value: Variable): Variable
    abstract fun add(value: String): Variable
    abstract fun add(value: Int): Variable
    abstract fun add(value: Double): Variable

    abstract fun subtract(value: Variable): Variable
    abstract fun subtract(value: String): Variable
    abstract fun subtract(value: Int): Variable
    abstract fun subtract(value: Double): Variable

    abstract fun multiply(value: Variable): Variable
    abstract fun multiply(value: String): Variable
    abstract fun multiply(value: Int): Variable
    abstract fun multiply(value: Double): Variable

    abstract fun divide(value: Variable): Variable
    abstract fun divide(value: String): Variable
    abstract fun divide(value: Int): Variable
    abstract fun divide(value: Double): Variable

    abstract fun power(value: Variable): Variable
    abstract fun power(value: String): Variable
    abstract fun power(value: Int): Variable
    abstract fun power(value: Double): Variable

    abstract fun modulus(value: Variable): Variable
    abstract fun modulus(value: String): Variable
    abstract fun modulus(value: Int): Variable
    abstract fun modulus(value: Double): Variable

    abstract fun concat(value: Variable): Variable
    abstract fun concat(value: String): Variable
    abstract fun concat(value: Int): Variable
    abstract fun concat(value: Double): Variable

    abstract fun negate(): Variable

    fun isEqual(value: Variable) = compareTo(value) == 0
    fun isEqual(value: String) = compareTo(value) == 0
    fun isEqual(value: Int) = compareTo(value) == 0
    fun isEqual(value: Double) = compareTo(value) == 0

    fun isNotEqual(value: Variable) = compareTo(value) != 0
    fun isNotEqual(value: String) = compareTo(value) != 0
    fun isNotEqual(value: Int) = compareTo(value) != 0
    fun isNotEqual(value: Double) = compareTo(value) != 0

    fun isGreaterThan(value: Variable) = compareTo(value) > 0
    fun isGreaterThan(value: String) = compareTo(value) > 0
    fun isGreaterThan(value: Int) = compareTo(value) > 0
    fun isGreaterThan(value: Double) = compareTo(value) > 0

    fun isGreaterThanOrEqual(value: Variable) = compareTo(value) >= 0
    fun isGreaterThanOrEqual(value: String) = compareTo(value) >= 0
    fun isGreaterThanOrEqual(value: Int) = compareTo(value) >= 0
    fun isGreaterThanOrEqual(value: Double) = compareTo(value) >= 0

    fun isLessThan(value: Variable) = compareTo(value) < 0
    fun isLessThan(value: String) = compareTo(value) < 0
    fun isLessThan(value: Int) = compareTo(value) < 0
    fun isLessThan(value: Double) = compareTo(value) < 0

    fun isLessThanOrEqual(value: Variable) = compareTo(value) <= 0
    fun isLessThanOrEqual(value: String) = compareTo(value) <= 0
    fun isLessThanOrEqual(value: Int) = compareTo(value) <= 0
    fun isLessThanOrEqual(value: Double) = compareTo(value) <= 0

    abstract fun isTrue(): Boolean
    abstract fun isFalse(): Boolean

    open fun compareTo(value: Variable): Int {
        return when (value.type) {
            VarType.String -> compareTo(value.toString())
            VarType.Integer -> compareTo(value.toInteger())
            VarType.Float -> compareTo(value.toFloat())
            VarType.List -> compareTo((value.value as ListValue).getAt(0))
        }
    }

    abstract fun compareTo(value: String): Int
    abstract fun compareTo(value: Int): Int
    abstract fun compareTo(value: Double): Int
    open fun compareTo(value: ListValue) = compareTo(value.getAt(0))

    companion object {
        fun div(val1: Int, val2: Int) = if (val2 == 0) 0 else val1 / val2
        fun div(val1: Double, val2: Double) = if (val2 == 0.0) .0 else val1 / val2

        fun mod(val1: Int, val2: Int) = if (val2 != 0) val1 % val2 else 0
        fun mod(val1: Double, val2: Double) = if (val2 != 0.0) val1 % val2 else 0.0
    }
}