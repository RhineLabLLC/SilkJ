package silklang.silk.compiler

class ByteCodeEntry(var value: Int, var isBytecode: Boolean, var line: Int) {

    constructor(bytecode: ByteCode, line: Int) : this(bytecode.ordinal, true, line)

    override fun toString(): String {
        return if (isBytecode) {
            "ByteCode.${ByteCode.values()[value]} ($value)"
        } else {
            var s = "$value (0x${Integer.toHexString(value)})"

            val flag = ByteCodeVariableType.All.ordinal and value
            if (flag != ByteCodeVariableType.None.ordinal) {
                s += " : BYTECODE${flag}[${value and ByteCodeVariableType.All.ordinal}]"
            }

            s
        }
    }
}

class ByteCodeWriter(private val lexer: LexicalAnalyzer) {
    private val byteCodeEntries = mutableListOf<ByteCodeEntry>()
    private val counters = mutableListOf<Int>()

    val IP get() = byteCodeEntries.size

    init {
        // Bytecode size must not change (won't be able to read saved compiled data)
        // assert(sizeOf<ByteCode>() == sizeOf<Int>())
        reset()
    }

    fun write(bytecode: ByteCode, incrementCounter: Boolean = true): Int {
        val ip = IP
        byteCodeEntries.add(ByteCodeEntry(bytecode, lexer.lastTokenLine))
        if (incrementCounter) incrementCount()
        return ip
    }

    fun write(value: Int, incrementCounter: Boolean = true): Int {
        val ip = IP
        byteCodeEntries.add(ByteCodeEntry(ByteCode.getByteCodeByOrdinal(value), lexer.lastTokenLine))
        if (incrementCounter) incrementCount()
        return ip
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun write(bytecode: ByteCode, value: Int, incrementCounter: Boolean = true): Int {
        val ip = IP
        byteCodeEntries.add(ByteCodeEntry(bytecode, lexer.lastTokenLine))
        byteCodeEntries.add(ByteCodeEntry(ByteCode.getByteCodeByOrdinal(value), lexer.lastTokenLine))
        if (incrementCounter) incrementCount()
        return ip
    }

    fun writeAt(ip: Int, bytecode: ByteCode) {
        assert(ip >= 0 && ip < IP)
        assert(byteCodeEntries[ip].isBytecode)
        byteCodeEntries[ip].value = bytecode.ordinal
    }

    fun writeAt(ip: Int, value: Int) {
        assert(ip >= 0 && ip < IP)
        assert(!byteCodeEntries[ip].isBytecode)
        byteCodeEntries[ip].value = value
    }

    fun undoLastWrite() {
        assert(byteCodeEntries.isNotEmpty())
        if (byteCodeEntries.isNotEmpty()) byteCodeEntries.removeAt(byteCodeEntries.size - 1)
    }

    fun resetCounter() {
        assert(counters.isNotEmpty())
        counters[counters.size - 1] = 0
    }

    private fun incrementCount() {
        assert(counters.isNotEmpty())
        counters[counters.size - 1]++
    }

    val counter: Int
        get() {
            assert(counters.isNotEmpty())
            return counters[counters.size - 1]
        }

    fun pushCounter() {
        counters.add(0)
    }

    fun popCounter() {
        assert(counters.size >= 2)
        if (counters.size >= 2) counters.removeAt(counters.size - 1)
    }

    fun getBytecodes(): IntArray {
        assert(byteCodeEntries.isNotEmpty())
        return byteCodeEntries.map { it.value }.toIntArray()
    }

    fun getLineNumbers(): IntArray {
        assert(byteCodeEntries.isNotEmpty())
        return byteCodeEntries.map { it.line }.toIntArray()
    }

    fun reset() {
        byteCodeEntries.clear()
        counters.clear()
        counters.add(0)
    }

    companion object {
        private fun parseLines(source: String?): List<String> {
            val lines = mutableListOf<String>()

            if (source != null) {
                var pos = 0
                while (pos < source.length) {
                    var nextPos: Int
                    var eol = source.indexOfAny(charArrayOf('\r', '\n'), pos)
                    if (eol >= 0) {
                        nextPos = eol + 1
                        if (nextPos < source.length && source[eol] == '\r' && source[nextPos] == '\n') {
                            nextPos++
                        }
                    } else {
                        eol = source.length
                        nextPos = eol
                    }
                    lines.add(source.substring(pos, eol))
                    pos = nextPos
                }
            }
            return lines
        }
    }
}
