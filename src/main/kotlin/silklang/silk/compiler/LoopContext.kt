package silklang.silk.compiler

class LoopContext(private val function: CompileTimeUserFunction, private val writer: ByteCodeWriter) {
    private var continueIP: Int = 0

    var startIP: Int = 0
    val breakFixups: MutableList<Int> = mutableListOf()
    val continueFixups: MutableList<Int> = mutableListOf()

    init {
        continueIP = writer.IP
        startIP = writer.IP
        function.loopContexts.addFirst(this)
    }

    fun setContinueIP() {
        continueIP = writer.IP
    }

    fun dispose() {
        if (!isDisposed) {
            // Fixup break targets references
            val loopEndIp = writer.IP
            for (ip in breakFixups) {
                writer.writeAt(ip, loopEndIp)
            }

            // Fixup continue targets
            for (ip in continueFixups) {
                writer.writeAt(ip, continueIP)
            }

            // Remove this context from loop stack
            require(function.loopContexts.isNotEmpty())
            require(function.loopContexts.first() == this)
            function.loopContexts.removeFirst()

            isDisposed = true
        }
    }

    private var isDisposed = false
}

