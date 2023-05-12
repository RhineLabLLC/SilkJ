package silklang.silk.runtime

import silklang.silk.compiler.ErrorCode
import silklang.silk.variable.Variable

class BeginEventArgs {
    var userData: Any? = null
}

class EndEventArgs {
    var userData: Any? = null
}

class FunctionEventArgs(name: String, parameters: Array<Variable>, returnValue: Variable, userData: Any? = null) {
    var name = name
    var parameters = parameters
    var returnValue = returnValue
    var userData = userData
}

internal class ErrorEventArgs {
    lateinit var errorCode: ErrorCode
    var token: String? = null
}