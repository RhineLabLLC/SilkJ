package silklang.silk.variable

class ListValue : AbstractValue {
    lateinit var value: ArrayList<Variable>

    constructor() {
        value = ArrayList()
    }

    constructor(size: Int) {
        value = ArrayList(size)
        for (i in 0 until size) {
            value.add(Variable())
        }
    }

    constructor(values: Iterable<Variable>) {
        value = ArrayList(values.toList())
    }

    override val varType: VarType
        get() = VarType.List

    override val listCount: Int
        get() = value.size

    val size = listCount

    override val list = value

    override fun toString(): String = "{ ${value.joinToString(", ")} }"

    override fun toInteger(): Int = getAt(0).toInteger()

    override fun toFloat(): Double = getAt(0).toFloat()

    override fun hashCode(): Int {
        var hash = 17
        hash = hash * 31 + varType.hashCode()
        for (v in value) {
            hash = hash * 31 + v.hashCode()
        }
        return hash
    }

    fun isValidIndex(index: Int): Boolean = index >= 0 && index < value.size

    fun getAt(index: Int): Variable = if (isValidIndex(index)) value[index] else Variable()

    override fun add(value: Variable): Variable = getAt(0).add(value)
    override fun add(value: String): Variable = getAt(0).add(value)
    override fun add(value: Int): Variable = getAt(0).add(value)
    override fun add(value: Double): Variable = getAt(0).add(value)

    override fun subtract(value: Variable): Variable = getAt(0).subtract(value)
    override fun subtract(value: String): Variable = getAt(0).subtract(value)
    override fun subtract(value: Int): Variable = getAt(0).subtract(value)
    override fun subtract(value: Double): Variable = getAt(0).subtract(value)

    override fun multiply(value: Variable): Variable = getAt(0).multiply(value)
    override fun multiply(value: String): Variable = getAt(0).multiply(value)
    override fun multiply(value: Int): Variable = getAt(0).multiply(value)
    override fun multiply(value: Double): Variable = getAt(0).multiply(value)

    override fun divide(value: Variable): Variable = getAt(0).divide(value)
    override fun divide(value: String): Variable = getAt(0).divide(value)
    override fun divide(value: Int): Variable = getAt(0).divide(value)
    override fun divide(value: Double): Variable = getAt(0).divide(value)

    override fun power(value: Variable): Variable = getAt(0).power(value)
    override fun power(value: String): Variable = getAt(0).power(value)
    override fun power(value: Int): Variable = getAt(0).power(value)
    override fun power(value: Double): Variable = getAt(0).power(value)

    override fun modulus(value: Variable): Variable = getAt(0).modulus(value)
    override fun modulus(value: String): Variable = getAt(0).modulus(value)
    override fun modulus(value: Int): Variable = getAt(0).modulus(value)
    override fun modulus(value: Double): Variable = getAt(0).modulus(value)

    override fun concat(value: Variable): Variable = getAt(0).concat(value)
    override fun concat(value: String): Variable = getAt(0).concat(value)
    override fun concat(value: Int): Variable = getAt(0).concat(value)
    override fun concat(value: Double): Variable = getAt(0).concat(value)

    override fun negate(): Variable = getAt(0).negate()

    override fun compareTo(value: String): Int = getAt(0).compareTo(value)
    override fun compareTo(value: Int): Int = getAt(0).compareTo(value)
    override fun compareTo(value: Double): Int = getAt(0).compareTo(value)
    override fun compareTo(value: ListValue): Int {
        val list = value.value
        for (i in 0 until value.size.coerceAtMost(list.size)) {
            val compare = value[i].compareTo(list[i])
            if (compare != 0) {
                return compare
            }
        }
        return value.size - list.size
    }

    operator fun get(i: Int): Variable {
        return value[i]
    }

    override fun isTrue(): Boolean = getAt(0).isTrue()

    override fun isFalse(): Boolean = getAt(0).isFalse()
}