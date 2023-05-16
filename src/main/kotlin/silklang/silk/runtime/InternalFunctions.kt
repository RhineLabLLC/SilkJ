package silklang.silk.runtime

import silklang.silk.compiler.Func
import silklang.silk.compiler.InternalFunc
import silklang.silk.utility.BooleanType
import silklang.silk.variable.Variable

class InternalFunctionInfo(
    action: (Array<Variable>, Variable) -> Unit,
    var minParameters: Int = Func.NoParameterLimit,
    var maxParameters: Int = Func.NoParameterLimit
) {
    var action: ((Array<Variable>, Variable) -> Unit)? = action
        private set

}

object InternalFunctions {
    private val internalFunctionLookup: Map<String, InternalFunctionInfo> = mapOf()

    private val internalVariableLookup: Map<String, Variable> = mapOf(
        "True" to Variable(BooleanType.True),
        "False" to Variable(BooleanType.False),
    )

    /**
     * Adds internal functions and variables to the given collections. Does not overwrite any existing
     * items with the same name.
     */
    fun addInternalFunctionsAndVariables(
        functions: LinkedHashMap<String, Func>,
        variables: LinkedHashMap<String, Variable>
    ) {
        // Add internal functions (don't override host app's functions)
        internalFunctionLookup.keys.forEach { key ->
            if (!functions.containsKey(key)) {
                val info = internalFunctionLookup[key]!!
                functions[key] = InternalFunc(key, info.action!!, info.minParameters, info.maxParameters)
            }
        }
        // Add internal variables (don't override host app's variables)
        internalVariableLookup.keys.forEach { key ->
            if (!variables.containsKey(key)) {
                val v = internalVariableLookup[key]!!
                variables[key] = v
            }
        }
    }

}