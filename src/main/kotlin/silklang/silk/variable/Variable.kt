package silklang.silk.variable

class Variable {
    lateinit var value: AbstractValue

    val type: VarType
        get() = value.varType

    val isList: Boolean
        get() = value.varType == VarType.List

    val listCount: Int
        get() = value.listCount

    constructor()

    constructor(v: Int) {
        setValue(v)
    }

    constructor(v: Double) {
        setValue(v)
    }

    constructor(v: String) {
        setValue(v)
    }

    fun setValue(v: Int) {
        if (value is IntegerValue) (value as IntegerValue).value = v
        else value = IntegerValue(v)
    }

    fun setValue(v: Double) {
        if (value is FloatValue) (value as FloatValue).value = v
        else value = FloatValue(v)
    }

    fun setValue(v: String) {
        if (value is StringValue) (value as StringValue).value = v
        else value = StringValue(v)
    }

    fun getAt(index: Int): Variable =
        (value as? ListValue)?.getAt(index)
            ?: if (index == 0) {
                this
            } else {
                Variable()
            }

    fun getList(): List<Variable> =
        if (isList) {
            value.list
        } else {
            listOf(this)
        }

    override fun toString(): String = value.toString()

    fun toInteger(): Int = value.toInteger()

    fun toFloat(): Double = value.toFloat()

    fun isFloat(): Boolean = value.isFloat()

    fun add(v: Variable): Variable = value.add(v)
    fun add(v: String): Variable = value.add(v)
    fun add(v: Int): Variable = value.add(v)
    fun add(v: Double): Variable = value.add(v)

    fun subtract(v: Variable): Variable = value.subtract(v)
    fun subtract(v: String): Variable = value.subtract(v)
    fun subtract(v: Int): Variable = value.subtract(v)
    fun subtract(v: Double): Variable = value.subtract(v)

    fun multiply(v: Variable): Variable = value.multiply(v)
    fun multiply(v: String): Variable = value.multiply(v)
    fun multiply(v: Int): Variable = value.multiply(v)
    fun multiply(v: Double): Variable = value.multiply(v)

    fun divide(v: Variable): Variable = value.divide(v)
    fun divide(v: String): Variable = value.divide(v)
    fun divide(v: Int): Variable = value.divide(v)
    fun divide(v: Double): Variable = value.divide(v)

    fun power(v: Variable): Variable = value.power(v)
    fun power(v: String): Variable = value.power(v)
    fun power(v: Int): Variable = value.power(v)
    fun power(v: Double): Variable = value.power(v)

    fun modulus(v: Variable): Variable = value.modulus(v)
    fun modulus(v: String): Variable = value.modulus(v)
    fun modulus(v: Int): Variable = value.modulus(v)
    fun modulus(v: Double): Variable = value.modulus(v)

    fun concat(v: Variable): Variable = value.concat(v)
    fun concat(v: String): Variable = value.concat(v)
    fun concat(v: Int): Variable = value.concat(v)
    fun concat(v: Double): Variable = value.concat(v)

    fun negate(): Variable = value.negate()

    val int: Int
        get() = toInt()

    val double: Double
        get() = toDouble()

    val string: String
        get() = toString()

    fun Variable.toInt(): Int = toInteger()
    fun Variable.toDouble(): Double = toFloat()
    fun Variable.toString(): String = toString()

    fun compareTo(variable: Variable) = value.compareTo(variable)
    fun compareTo(stringVar: String) = value.compareTo(stringVar)
    fun compareTo(intVar: Int) = value.compareTo(intVar)
    fun compareTo(doubleVar: Double) = value.compareTo(doubleVar)

    fun isEqual(variable: Variable) = value.isEqual(variable)
    fun isEqual(stringVar: String) = value.isEqual(stringVar)
    fun isEqual(intVar: Int) = value.isEqual(intVar)
    fun isEqual(doubleVar: Double) = value.isEqual(doubleVar)

    fun isNotEqual(variable: Variable) = value.isNotEqual(variable)
    fun isNotEqual(stringVar: String) = value.isNotEqual(stringVar)
    fun isNotEqual(intVar: Int) = value.isNotEqual(intVar)
    fun isNotEqual(doubleVar: Double) = value.isNotEqual(doubleVar)

    fun isGreaterThan(variable: Variable) = value.isGreaterThan(variable)
    fun isGreaterThan(stringVar: String) = value.isGreaterThan(stringVar)
    fun isGreaterThan(intVar: Int) = value.isGreaterThan(intVar)
    fun isGreaterThan(doubleVar: Double) = value.isGreaterThan(doubleVar)

    fun isGreaterThanOrEqual(variable: Variable) = value.isGreaterThanOrEqual(variable)
    fun isGreaterThanOrEqual(stringVar: String) = value.isGreaterThanOrEqual(stringVar)
    fun isGreaterThanOrEqual(intVar: Int) = value.isGreaterThanOrEqual(intVar)
    fun isGreaterThanOrEqual(doubleVar: Double) = value.isGreaterThanOrEqual(doubleVar)

    fun isLessThan(variable: Variable) = value.isLessThan(variable)
    fun isLessThan(stringVar: String) = value.isLessThan(stringVar)
    fun isLessThan(intVar: Int) = value.isLessThan(intVar)
    fun isLessThan(doubleVar: Double) = value.isLessThan(doubleVar)

    fun isLessThanOrEqual(variable: Variable) = value.isLessThanOrEqual(variable)
    fun isLessThanOrEqual(stringVar: String) = value.isLessThanOrEqual(stringVar)
    fun isLessThanOrEqual(intVar: Int) = value.isLessThanOrEqual(intVar)
    fun isLessThanOrEqual(doubleVar: Double) = value.isLessThanOrEqual(doubleVar)

    fun isTrue() = value.isTrue()
    fun isFalse() = value.isFalse()

    fun isEqual(var1: Variable, var2: Variable): Boolean = var1.isEqual(var2)
    fun isEqual(var1: Variable, var2: String): Boolean = var1.isEqual(var2)
    fun isEqual(var1: Variable, var2: Int): Boolean = var1.isEqual(var2)
    fun isEqual(var1: Variable, var2: Double): Boolean = var1.isEqual(var2)

    fun isNotEqual(var1: Variable, var2: Variable): Boolean = var1.isNotEqual(var2)
    fun isNotEqual(var1: Variable, var2: String): Boolean = var1.isNotEqual(var2)
    fun isNotEqual(var1: Variable, var2: Int): Boolean = var1.isNotEqual(var2)
    fun isNotEqual(var1: Variable, var2: Double): Boolean = var1.isNotEqual(var2)

    fun isGreaterThan(var1: Variable, var2: Variable): Boolean = var1.isGreaterThan(var2)
    fun isGreaterThan(var1: Variable, var2: String): Boolean = var1.isGreaterThan(var2)
    fun isGreaterThan(var1: Variable, var2: Int): Boolean = var1.isGreaterThan(var2)
    fun isGreaterThan(var1: Variable, var2: Double): Boolean = var1.isGreaterThan(var2)

    fun isGreaterThanOrEqual(var1: Variable, var2: Variable): Boolean = var1.isGreaterThanOrEqual(var2)
    fun isGreaterThanOrEqual(var1: Variable, var2: String): Boolean = var1.isGreaterThanOrEqual(var2)
    fun isGreaterThanOrEqual(var1: Variable, var2: Int): Boolean = var1.isGreaterThanOrEqual(var2)
    fun isGreaterThanOrEqual(var1: Variable, var2: Double): Boolean = var1.isGreaterThanOrEqual(var2)

    fun isLessThan(var1: Variable, var2: Variable): Boolean = var1.isLessThan(var2)
    fun isLessThan(var1: Variable, var2: String): Boolean = var1.isLessThan(var2)
    fun isLessThan(var1: Variable, var2: Int): Boolean = var1.isLessThan(var2)
    fun isLessThan(var1: Variable, var2: Double): Boolean = var1.isLessThan(var2)

    fun isLessThanOrEqual(var1: Variable, var2: Variable): Boolean = var1.isLessThanOrEqual(var2)
    fun isLessThanOrEqual(var1: Variable, var2: String): Boolean = var1.isLessThanOrEqual(var2)
    fun isLessThanOrEqual(var1: Variable, var2: Int): Boolean = var1.isLessThanOrEqual(var2)
    fun isLessThanOrEqual(var1: Variable, var2: Double): Boolean = var1.isLessThanOrEqual(var2)

    fun add(var1: Variable, var2: Variable): Variable = var1.add(var2)
    fun add(var1: Variable, value: String): Variable = var1.add(value)
    fun add(var1: Variable, value: Int): Variable = var1.add(value)
    fun add(var1: Variable, value: Double): Variable = var1.add(value)

    fun subtract(var1: Variable, var2: Variable): Variable = var1.subtract(var2)
    fun subtract(var1: Variable, value: String): Variable = var1.subtract(value)
    fun subtract(var1: Variable, value: Int): Variable = var1.subtract(value)
    fun subtract(var1: Variable, value: Double): Variable = var1.subtract(value)

    fun multiply(var1: Variable, var2: Variable): Variable = var1.multiply(var2)
    fun multiply(var1: Variable, value: String): Variable = var1.multiply(value)
    fun multiply(var1: Variable, value: Int): Variable = var1.multiply(value)
    fun multiply(var1: Variable, value: Double): Variable = var1.multiply(value)

    fun divide(var1: Variable, var2: Variable): Variable = var1.divide(var2)
    fun divide(var1: Variable, value: String): Variable = var1.divide(value)
    fun divide(var1: Variable, value: Int): Variable = var1.divide(value)
    fun divide(var1: Variable, value: Double): Variable = var1.divide(value)

    fun modulus(var1: Variable, var2: Variable): Variable = var1.modulus(var2)
    fun modulus(var1: Variable, value: String): Variable = var1.modulus(value)
    fun modulus(var1: Variable, value: Int): Variable = var1.modulus(value)
    fun modulus(var1: Variable, value: Double): Variable = var1.modulus(value)

    fun negate(var1: Variable): Variable = var1.negate()

    operator fun Variable.get(index: Int): Variable = getAt(index)
}