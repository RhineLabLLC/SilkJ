package silklang.silk.runtime

import silklang.silk.compiler.Func
import silklang.silk.compiler.InternalFunc
import silklang.silk.compiler.IntrinsicFunc
import silklang.silk.compiler.UserFunc
import silklang.silk.utility.Flags.hasFlag
import silklang.silk.variable.Variable
import java.io.ByteArrayInputStream
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.File
import kotlin.io.path.Path

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

    fun reset() {
        byteCodes = arrayListOf()
        functions = arrayListOf()
        variables = arrayListOf()
        literals = arrayListOf()
        lineNumbers = null
    }

    var isEmpty = byteCodes.isEmpty()

    fun getVariablesCopy() = arrayListOf<Variable>().apply { addAll(variables) }

    fun save(path: String) {
        check(!isEmpty) { "Cannot save empty program." }
        val file = Path(path).toFile()
        if (!file.exists()) if (!file.createNewFile()) throw IllegalStateException("Cannot create file!")
        DataOutputStream(file.outputStream()).use {
            it.writeInt(magic)
            it.writeInt(0)
            it.writeInt(if (lineNumbers != null) FileFlag.HasLineNumbers.flag else FileFlag.None.flag)
            it.writeInt(byteCodes.size)
            byteCodes.forEach { i -> it.writeInt(i) }
            it.writeInt(functions.size)
            functions.forEach { f ->
                it.writeUTF(f.name)
                when {
                    (f is InternalFunc) -> it.writeInt(FunctionType.Internal.ordinal)
                    (f is IntrinsicFunc) -> it.writeInt(FunctionType.Intrinsic.ordinal)
                    (f is UserFunc) -> {
                        it.writeInt(FunctionType.User.ordinal)
                        it.writeInt(f.ip)
                        it.writeInt(f.numVariables)
                        it.writeInt(f.numParameters)
                    }
                }
            }
            it.writeInt(variables.size)
            variables.forEach { } // Write variable
            it.writeInt(literals.size)
            for (i in 0..literals.size) { } // Write literal
            if (lineNumbers != null) {
                if (lineNumbers!!.size != byteCodes.size) return
                it.writeInt(lineNumbers!!.size)
                lineNumbers!!.forEach { i -> it.writeInt(i) }
            }
        }

        fun load(path: String) {
            reset()
            var i: Int
            var count: Int
            val file = Path(path).toFile()
            if (!file.exists()) throw IllegalStateException("File not exists: $path")
            DataInputStream(file.inputStream()).use { it ->
                i = it.readInt()
                check(i == magic) { "Invalid file format!" }
                i = it.readInt()
                check(i == 0) { "Invalid file format!" }
                var flags = it.readInt()
                count = it.readInt()
                for (j in 0 until count) {
                    byteCodes[j] = it.readInt()
                }
                count = it.readInt()
                for (j in 0 until count) {
                    val name = it.readUTF()
                    when (it.readInt()) {
                        FunctionType.Internal.ordinal -> {
                            val func = InternalFunc(name, { _, _ -> return@InternalFunc })
                            functions.add(func)
                        }
                        FunctionType.Intrinsic.ordinal -> {
                            val func = IntrinsicFunc(name)
                            functions.add(func)
                        }
                        else -> {
                            val func = UserFunc(name, 0).apply {
                                ip = it.readInt()
                                numVariables = it.readInt()
                                numParameters = it.readInt()
                            }
                            functions.add(func)
                        }
                    }
                }
                count = it.readInt()
                for (j in 0 until count) { } // Read variable
                count = it.readInt()
                for (j in 0 until count) { } // Read literal
                if (flags.hasFlag(FileFlag.HasLineNumbers.flag)) {
                    count = it.readInt()
                    lineNumbers = ArrayList()
                    for (j in 0 until count) lineNumbers!![j] = it.readInt()
                }
                functions.stream().filter { f -> f is InternalFunc }.forEach { f ->
                    if (InternalFunctions.internalFunctionLookup.containsKey(f.name)) {
                        val info = InternalFunctions.internalFunctionLookup[f.name]!!
                        f as InternalFunc
                        f.action = info.action!!
                        f.minParameters = info.minParameters
                        f.maxParameters = info.maxParameters
                    }
                }
            }
        }
    }
}