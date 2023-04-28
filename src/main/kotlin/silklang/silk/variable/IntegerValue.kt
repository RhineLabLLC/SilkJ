package silklang.silk.variable

import silklang.silk.utility.BooleanType
import silklang.silk.utility.Extensions.isFloatOrDouble
import silklang.silk.utility.Extensions.isNumeric
import kotlin.math.pow

class IntegerValue : AbstractValue {
    override val varType = VarType.Integer
    var value: Int = 0

    constructor() {
        value = 0
    }

    constructor(value: Int) {
        this.value = value
    }

    constructor(value: String) {
        this.value = if (value.length > 2 && value[0] == '0' && value[1] == 'x') {
            Integer.parseInt(value.substring(2), 16)
        } else {
            value.toInt()
        }
    }

    fun getVarType(): VarType = VarType.Integer

    override fun toString(): String = value.toString()
    override fun toInteger(): Int = value
    override fun toFloat(): Double = value.toDouble()

    override fun hashCode(): Int {
        return value.hashCode()
    }

    // Operations

    override fun add(value: Variable): Variable {
        return if (value.isFloat()) {
            Variable(this.value.toDouble() + value.toFloat())
        } else {
            Variable(this.value + value.toInteger())
        }
    }

    override fun add(value: String): Variable {
        return if (value.toDoubleOrNull() != null) {
            Variable(value.toDouble() + this.value.toDouble())
        } else {
            Variable(value.toInt() + toInteger())
        }
    }

    override fun add(value: Int): Variable = Variable(this.value + value)
    override fun add(value: Double): Variable = Variable(this.value + value)

    override fun subtract(value: Variable): Variable {
        return if (value.isFloat()) {
            Variable(toFloat() - value.toFloat())
        } else {
            Variable(toInteger() - value.toInteger())
        }
    }

    override fun subtract(value: String): Variable {
        return if (value.isFloatOrDouble()) {
            Variable(toFloat() - value.toFloat())
        } else {
            Variable(toInteger() - value.toInt())
        }
    }

    override fun subtract(value: Int): Variable = Variable(this.value - value)
    override fun subtract(value: Double): Variable = Variable(this.value - value)

    override fun multiply(value: Variable): Variable {
        return if (value.isFloat()) {
            Variable(toFloat() * value.toFloat())
        } else {
            Variable(toInteger() * value.toInteger())
        }
    }

    override fun multiply(value: String): Variable {
        return if (value.isFloatOrDouble()) {
            Variable(toFloat() * value.toFloat())
        } else {
            Variable(toInteger() * value.toInt())
        }
    }

    override fun multiply(value: Int): Variable = Variable(this.value * value)
    override fun multiply(value: Double): Variable = Variable(this.value * value)

    override fun divide(value: Variable): Variable {
        return if (value.isFloat()) {
            Variable(div(this.value.toDouble(), value.toFloat()))
        } else {
            Variable(div(this.value, value.toInteger()))
        }
    }

    override fun divide(value: String): Variable {
        return if (value.toDoubleOrNull() != null) {
            Variable(div(this.value.toDouble(), value.toDouble()))
        } else {
            Variable(div(this.value, value.toInt()))
        }
    }

    override fun divide(value: Int): Variable = Variable(div(this.value, value))
    override fun divide(value: Double): Variable = Variable(div(this.value.toDouble(), value))

    override fun power(value: Variable): Variable = Variable(toFloat().pow(value.toFloat()))
    override fun power(value: String): Variable = Variable(toFloat().pow(value.toDouble()))
    override fun power(value: Int): Variable = Variable(toFloat().pow(value.toDouble()))
    override fun power(value: Double): Variable = Variable(toFloat().pow(value))

    override fun modulus(value: Variable): Variable {
        return if (value.isFloat()) {
            Variable(mod(this.value.toDouble(), value.toFloat()))
        } else {
            Variable(mod(this.value, value.toInteger()))
        }
    }

    override fun modulus(value: String): Variable {
        return if (value.isFloatOrDouble()) {
            Variable(mod(this.value.toDouble(), value.toDouble()))
        } else {
            Variable(mod(this.value, value.toInt()))
        }
    }

    override fun modulus(value: Int): Variable = Variable(mod(this.value, value))
    override fun modulus(value: Double): Variable = Variable(mod(this.value.toDouble(), value))

    override fun concat(value: Variable): Variable = Variable(toString() + value.toString())
    override fun concat(value: String): Variable = Variable(toString() + value)
    override fun concat(value: Int): Variable = Variable(toString() + value.toString())
    override fun concat(value: Double): Variable = Variable(toString() + value.toString())

    override fun negate(): Variable = Variable(-value)

    override fun compareTo(value: String): Int {
        if (value.isNumeric()) {
            return if (value.toDoubleOrNull() != null) {
                this.value.toFloat().compareTo(value.toDouble())
            } else {
                this.value.compareTo(value.toInt())
            }
        }
        return this.value.toString().compareTo(value)
    }

    override fun compareTo(value: Int): Int = this.value.compareTo(value)
    override fun compareTo(value: Double): Int = this.value.toDouble().compareTo(value)

    override fun isTrue(): Boolean = BooleanType.isTrue(this.value)
    override fun isFalse(): Boolean = BooleanType.isFalse(this.value)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as IntegerValue

        if (varType != other.varType) return false
        return value == other.value
    }
}