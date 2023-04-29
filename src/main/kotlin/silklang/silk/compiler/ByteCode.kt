package silklang.silk.compiler

enum class ByteCodeVariableType(val flag: Int) {
    None(0x00000000),
    Global(0x10000000),
    Local(0x20000000),
    Parameter(0x40000000),
    All(Global.flag or Local.flag or Parameter.flag)
}

/**
 * Byte code values.
 *
 * Note: New byte codes must be added at the end to avoid changing values of existing codes,
 * which would break running previously compiled programs.
 */
enum class ByteCode {
    Nop,
    ExecFunction,
    Return,
    Jump,
    Assign,
    // Retained for backwards compatibility
    // but now AssignListVariableMulti is used,
    // which supports multiple indexes.
    AssignListVariable,
    JumpIfFalse,
    EvalLiteral,
    EvalVariable,
    EvalCreateList,
    EvalInitializeList,
    // Retained for backwards compatibility
    // but now EvalListVariableMulti is used,
    // which supports multiple indexes.
    EvalListVariable,
    EvalFunction,
    EvalAdd,
    EvalSubtract,
    EvalMultiply,
    EvalDivide,
    EvalPower,
    EvalModulus,
    EvalConcat,
    EvalNegate,
    EvalAnd,
    EvalOr,
    EvalXor,
    EvalNot,
    EvalIsEqual,
    EvalIsNotEqual,
    EvalIsGreaterThan,
    EvalIsGreaterThanOrEqual,
    EvalIsLessThan,
    EvalIsLessThanOrEqual,
    AssignListVariableMulti,
    EvalListVariableMulti;

    companion object {
        fun getByteCodeByOrdinal(ordinal: Int) = values().firstOrNull { it.ordinal == ordinal } ?: Nop
    }
}

/**
 * Static helper methods
 */
object ByteCodes {
    const val InvalidIP = -1

    fun isGlobalVariable(value: Int): Boolean =
        (ByteCodeVariableType.values()[value].flag and ByteCodeVariableType.Global.flag) != 0

    fun isLocalVariable(value: Int): Boolean =
        (ByteCodeVariableType.values()[value].flag and ByteCodeVariableType.Local.flag) != 0

    fun isFunctionParameterVariable(value: Int): Boolean =
        (ByteCodeVariableType.values()[value].flag and ByteCodeVariableType.Parameter.flag) != 0

    fun getVariableIndex(value: Int): Int =
        value and ByteCodeVariableType.All.flag.inv()
}