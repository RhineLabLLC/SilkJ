package silklang.silk.variable

import silklang.silk.utility.BooleanType
import silklang.silk.utility.Extensions.isFloatOrDouble
import silklang.silk.utility.Extensions.isNumeric
import java.util.*
import kotlin.math.pow

class StringValue: AbstractValue {
    var value: String

    constructor() {
        value = ""
    }

    constructor(value: String?) {
        this.value = value ?: ""
    }

    override val varType: VarType
        get() = VarType.String

    override fun toString(): String {
        return value
    }

    override fun toInteger(): Int {
        return value.toInt()
    }

    override fun toFloat(): Double {
        return value.toDouble()
    }

    override fun isFloat(): Boolean {
        return value.isFloatOrDouble()
    }

    override fun hashCode(): Int = Objects.hash(varType, value)

    override fun add(value: Variable): Variable =
        if (isFloat() || value.isFloat()) {
            Variable(toFloat() + value.toFloat())
        } else {
            Variable(toInteger() + value.toInteger())
        }
    override fun add(value: String): Variable =
        if (isFloat() || value.isFloatOrDouble()) {
            Variable(toFloat() + value.toDouble())
        } else {
            Variable(toInteger() + value.toInt())
        }
    override fun add(value: Int): Variable =
        if (isFloat()) {
            Variable(toFloat() + value)
        } else {
            Variable(toInteger() + value)
        }
    override fun add(value: Double): Variable = Variable(toFloat() + value)

    override fun subtract(value: Variable): Variable =
        if (isFloat() || value.isFloat()) {
            Variable(toFloat() - value.toFloat())
        } else {
            Variable(toInteger() - value.toInteger())
        }
    override fun subtract(value: String): Variable =
        if (isFloat() || value.isFloatOrDouble()) {
            Variable(toFloat() - value.toDouble())
        } else {
            Variable(toInteger() - value.toInt())
        }
    override fun subtract(value: Int): Variable =
        if (isFloat()) {
            Variable(toFloat() - value)
        } else {
            Variable(toInteger() - value)
        }
    override fun subtract(value: Double): Variable = Variable(toFloat() - value)

    override fun multiply(value: Variable): Variable =
        if (isFloat() || value.isFloat()) {
            Variable(toFloat() * value.toFloat())
        } else {
            Variable(toInteger() * value.toInteger())
        }
    override fun multiply(value: String): Variable =
        if (isFloat() || value.isFloatOrDouble()) {
            Variable(toFloat() * value.toDouble())
        } else {
            Variable(toInteger() * value.toInt())
        }
    override fun multiply(value: Int): Variable =
        if (isFloat()) {
            Variable(toFloat() * value)
        } else {
            Variable(toInteger() * value)
        }
    override fun multiply(value: Double): Variable = Variable(toFloat() * value)

    override fun divide(value: Variable): Variable =
        if (isFloat() || value.isFloat()) {
            Variable(div(toFloat(), value.toFloat()))
        } else {
            Variable(div(toInteger(), value.toInteger()))
        }
    override fun divide(value: String): Variable =
        if (isFloat() || value.isFloatOrDouble()) {
            Variable(div(toFloat(), value.toDouble()))
        } else {
            Variable(div(toInteger(), value.toInt()))
        }
    override fun divide(value: Int): Variable =
        if (isFloat()) {
            Variable(div(toFloat(), value.toDouble()))
        } else {
            Variable(div(toInteger(), value))
        }
    override fun divide(value: Double): Variable = Variable(div(toFloat(), value))

    override fun power(value: Variable): Variable = Variable(toFloat().pow(value.toFloat()))
    override fun power(value: String): Variable = Variable(toFloat().pow(value.toDouble()))
    override fun power(value: Int): Variable = Variable(toFloat().pow(value.toDouble()))
    override fun power(value: Double): Variable = Variable(toFloat().pow(value))

    override fun modulus(value: Variable): Variable =
        if (isFloat() || value.isFloat()) Variable(this.toFloat() % value.toFloat())
        else Variable(this.toInteger() % value.toInteger())
    override fun modulus(value: String): Variable =
        if (isFloat() || value.isFloatOrDouble()) Variable(this.toFloat() % value.toDouble())
        else Variable(this.toInteger() % value.toInt())
    override fun modulus(value: Int): Variable =
        if (isFloat()) Variable(this.toFloat() % value.toDouble())
        else Variable(this.toInteger() % value)
    override fun modulus(value: Double): Variable = Variable(this.toFloat() % value)

    override fun concat(value: Variable): Variable = Variable(this.value + value.toString())
    override fun concat(value: String): Variable = Variable(this.value + value)
    override fun concat(value: Int): Variable = Variable(this.value + value.toString())
    override fun concat(value: Double): Variable = Variable(this.value + value.toString())

    override fun negate(): Variable = if (isFloat()) Variable(-toFloat()) else Variable(-toInteger())

    override fun isTrue(): Boolean = BooleanType.isTrue(this.value)
    override fun isFalse(): Boolean = BooleanType.isFalse(this.value)

    override fun compareTo(value: String): Int = this.value.compareTo(value)
    override fun compareTo(value: Int): Int =
        if (this.value.isNumeric())
            if (this.value.isFloatOrDouble()) this.value.toDouble().compareTo(value)
            else this.value.toInt().compareTo(value)
        else this.value.compareTo(value.toString())

    override fun compareTo(value: Double): Int =
        if (this.value.isNumeric()) this.value.toDouble().compareTo(value)
        else this.value.compareTo(value.toString())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StringValue

        return value == other.value
    }
}