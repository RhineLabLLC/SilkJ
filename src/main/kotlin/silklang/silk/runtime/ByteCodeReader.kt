package silklang.silk.runtime

import silklang.silk.compiler.ByteCode

/**
 * Helper class for reading bytecode data.
 */
class ByteCodeReader(bytecodes: IntArray) {
    private val byteCodes: IntArray = bytecodes.clone()
    private val ipStack: ArrayDeque<Int> = ArrayDeque()

    /**
     * Instruction pointer (IP). Returns the current read position.
     */
    var ip: Int = 0
        private set

    val endOfFile: Boolean
        get() = ip >= byteCodes.size

    fun getNext(): ByteCode {
        if (endOfFile)
            throw Exception("Attempted to read beyond the last bytecode")
        return byteCodes[ip++].toByteCode()
    }

    fun getNextValue(): Int {
        if (endOfFile)
            throw Exception("Attempted to read beyond the last bytecode")
        return byteCodes[ip++]
    }

    /**
     * Jumps to the specified read position.
     * @param ip the read position to jump to.
     */
    fun goTo(ip: Int) {
        require(ip >= 0 && ip < byteCodes.size) { "Invalid instruction pointer" }
        this.ip = ip
    }

    /**
     * Saves the current read position.
     */
    fun push() {
        ipStack.addFirst(ip)
    }

    /**
     * Restores the read position previously saved with [push].
     */
    fun pop() {
        require(ipStack.isNotEmpty()) { "Instruction pointer stack is empty" }
        goTo(ipStack.removeFirst())
    }

    private fun Int.toByteCode(): ByteCode {
        require(isValidByteCodeValue()) { "Attempted to read non-bytecode value $this as bytecode" }
        return ByteCode.values()[this]
    }

    private fun Int.isValidByteCodeValue(): Boolean {
        return this in ByteCode.values().map { it.ordinal }
    }
}