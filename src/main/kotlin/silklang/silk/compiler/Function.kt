package silklang.silk.compiler

import silklang.silk.variable.Variable

// Base class for several function classes. Also defines some function-related constants.
// This class is made public so const values can be accessed externally.
abstract class Function(var name: String) {
    // Name of Main function. This function must be defined and is where execution
    // starts.
    companion object {
        const val Main = "Main"
        // Signifies no restriction on the number of parameters passed to intrinsic functions.
        const val NoParameterLimit = -1
    }

    open val isIntrinsic: Boolean
        get() = this is IntrinsicFunction
}

// Represents a user function.
open class UserFunction(name: String, var ip: Int) : Function(name) {
    var numVariables: Int = 0
    var numParameters: Int = 0
}

// Represents a user function during compilation.
class CompileTimeUserFunction(name: String, ip: Int) : UserFunction(name, ip) {
    var labels: LinkedHashMap<String, Label>? = null
    var variables: LinkedHashMap<String, Variable>? = null
    var parameters: LinkedHashMap<String, Variable>? = null
    var loopContexts = ArrayDeque<LoopContext>()

    fun getLoopContext(): LoopContext? {
        return if (loopContexts.isNotEmpty()) loopContexts.first() else null
    }

    // Returns the ID of the local variable or parameter with the given name, or
    // creates the variable is added if it was not found.
    fun getVariableId(name: String): Int {
        // First search parameters
        var index = parameters!!.keys.indexOf(name)
        if (index != -1)
            return index or ByteCodeVariableType.Parameter.flag
        // Next search local variables
        index = variables!!.keys.indexOf(name)
        if (index != -1)
            return index or ByteCodeVariableType.Local.flag
        // Variable not defined; create it
        variables!![name] = Variable()
        return variables!!.keys.indexOf(name) or ByteCodeVariableType.Local.flag
    }
}

// Represents an intrinsic function.
open class IntrinsicFunction(name: String, var minParameters: Int = NoParameterLimit, var maxParameters: Int = NoParameterLimit) : Function(name) {
    // Returns true if the given argument count is valid for this function's MinParameter
    // and MaxParameter values. If false is returned, message is set to
    // a description of how the count is invalid.
    internal fun isParameterCountValid(count: Int, message: String?): Boolean {
        var message = message
        if (minParameters == maxParameters) {
            if (minParameters != NoParameterLimit && minParameters != count) {
                message = "Function \"$name\" requires $minParameters argument(s)"
                return false
            }
        } else {
            if (minParameters != NoParameterLimit) {
                if (count < minParameters) {
                    message = "Function \"$name\" requires at least $minParameters argument(s)"
                    return false
                }
            }
            if (maxParameters != NoParameterLimit) {
                if (count > maxParameters) {
                    message = "Function \"$name\" doesn't allow more than $maxParameters argument(s)"
                    return false
                }
            }
        }
        return true
    }
}

// Represents one of the functions internal within this library.
class InternalFunction(name: String, var action: (Array<Variable>, Variable) -> Unit, minParameters: Int = NoParameterLimit, maxParameters: Int = NoParameterLimit) : IntrinsicFunction(name, minParameters, maxParameters)

// Represents a user function at run time.
class RuntimeFunction(function: UserFunction?) {
    var ip: Int = 0
    var parameters: ArrayList<Variable>
    var variables: ArrayList<Variable>
    var returnValue: Variable? = null

    init {
        if (function == null)
            throw IllegalArgumentException("Function argument cannot be null.")
        ip = function.ip
        parameters = ArrayList(function.numParameters)
        for (i in 0 until function.numParameters)
            parameters[i] = Variable()
        variables = ArrayList(function.numVariables)
        for (i in 0 until function.numVariables)
            variables[i] = Variable()
        returnValue = Variable()
    }
}