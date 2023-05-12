package silklang.silk.compiler

import silklang.silk.variable.Variable

class Compiler {
    /**
     * Gets or sets the maximum number of compile errors allowed before compiling aborts.
     */
    var maxErrors = 45

    /**
     * Whether the compiler generates a log file of all bytecode generated.
     */
    var createLogFile = false

    /**
     * Whether line number is included. This is especially useful for debugging.
     */
    var enableLineNumbers = true

    /**
     * Whether enable internal functions and variables.
     */
    var enableInternalFunctions = true

    /**
     * Log file name.
     */
    var logFile = ""

    /**
     * Compilation errors.
     */
    var errors = arrayListOf<Error>()

    private val intrinsicFunctions = LinkedHashMap<String, IntrinsicFunc>()
    private val intrinsicVariables = LinkedHashMap<String, Variable>()
    private val functions = LinkedHashMap<String, Func>()
    private val variables = LinkedHashMap<String, Variable>()
    private val literals = arrayListOf<Variable>()
    private val lexer = LexicalAnalyzer()
    private val writer = ByteCodeWriter(lexer)
    private var currentFunction: CompileTimeUserFunc? = null
    private var inHeader = false
    private var sourceFile: String? = null
}