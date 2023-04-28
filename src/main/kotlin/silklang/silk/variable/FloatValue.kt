package silklang.silk.variable

import silklang.silk.utility.BooleanType
import java.util.*
import kotlin.math.pow
import kotlin.math.roundToInt

class FloatValue : AbstractValue {
    var value: Double

    constructor() {
        value = 0.0
    }

    constructor(value: Double) {
        this.value = value
    }

    constructor(value: String) {
        this.value = value.toDouble()
    }

    override val varType: VarType
        get() = VarType.Float

    override fun toString(): String = value.toString()

    override fun toInteger(): Int = value.roundToInt()

    override fun toFloat(): Double = value

    override fun isFloat(): Boolean = true

    override fun hashCode(): Int = Objects.hash(varType, value)

    override fun add(value: Variable): Variable = Variable(this.value + value.toFloat())
    override fun add(value: String): Variable = Variable(this.value + value.toFloat())
    override fun add(value: Int): Variable = Variable(this.value + value)
    override fun add(value: Double): Variable = Variable(this.value + value)

    override fun subtract(value: Variable): Variable = Variable(this.value - value.toFloat())
    override fun subtract(value: String): Variable = Variable(this.value - value.toFloat())
    override fun subtract(value: Int): Variable = Variable(this.value - value)
    override fun subtract(value: Double): Variable = Variable(this.value - value)

    override fun multiply(value: Variable): Variable = Variable(this.value * value.toFloat())
    override fun multiply(value: String): Variable = Variable(this.value * value.toFloat())
    override fun multiply(value: Int): Variable = Variable(this.value * value)
    override fun multiply(value: Double): Variable = Variable(this.value * value)

    override fun divide(value: Variable): Variable = Variable(div(this.value, value.toFloat()))
    override fun divide(value: String): Variable = Variable(div(this.value, value.toDouble()))
    override fun divide(value: Int): Variable = Variable(div(this.value, value.toDouble()))
    override fun divide(value: Double): Variable = Variable(div(this.value, value))

    override fun power(value: Variable): Variable = Variable(this.value.pow(value.toFloat()))
    override fun power(value: String): Variable = Variable(this.value.pow(value.toDouble()))
    override fun power(value: Int): Variable = Variable(this.value.pow(value))
    override fun power(value: Double): Variable = Variable(this.value.pow(value))

    override fun modulus(value: Variable): Variable = Variable(mod(this.value, value.toFloat()))
    override fun modulus(value: String): Variable = Variable(mod(this.value, value.toDouble()))
    override fun modulus(value: Int): Variable = Variable(mod(this.value, value.toDouble()))
    override fun modulus(value: Double): Variable = Variable(mod(this.value, value))

    override fun concat(value: Variable): Variable = Variable(toString() + value.toString())
    override fun concat(value: String): Variable = Variable(toString() + value)
    override fun concat(value: Int): Variable = Variable(toString() + value.toString())
    override fun concat(value: Double): Variable = Variable(toString() + value.toString())

    override fun negate(): Variable = Variable(-value)

    override fun compareTo(value: String): Int {
        return if (value.toDoubleOrNull() != null) {
            this.value.compareTo(value.toDouble())
        } else {
            this.value.toString().compareTo(value)
        }
    }

    override fun compareTo(value: Int): Int = this.value.compareTo(value)
    override fun compareTo(value: Double): Int = this.value.compareTo(value)

    override fun isTrue(): Boolean = BooleanType.isTrue(value.roundToInt())
    override fun isFalse(): Boolean = BooleanType.isFalse(value.roundToInt())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FloatValue

        return value == other.value
    }
}