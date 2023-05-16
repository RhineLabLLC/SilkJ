package silklang.silk.runtime

import silklang.silk.compiler.Func
import silklang.silk.variable.Variable

class CompiledProgram {

    private val magic = 0x4B4C4953 // SILK

    enum class FileFlag(val flag: Int) {
        None(0x0),
        HasLineNumbers(0x1),
    }

    enum class FunctionType() {
        User,
        Intrinsic,
        Internal,
    }

    var byteCodes: ArrayList<Int> = ArrayList()
    var functions: ArrayList<Func> = ArrayList()
    var variables: ArrayList<Variable> = ArrayList()
    var literals: ArrayList<Variable> = ArrayList()
    var lineNumbers: ArrayList<Int>? = null

}