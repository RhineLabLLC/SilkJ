package silklang.silk.compiler

import silklang.silk.utility.Flags.hasFlag

enum class TokenTypeFlag(val flag: Int) {
    Literal(0x10000000),
    Operator(0x20000000);
}

enum class TokenType(val flag: Int) {
    EndOfFile(0),
    EndOfLine(1),

    Keyword(10),
    Symbol(11),
    String(12 or TokenTypeFlag.Literal.flag),
    Integer(13 or TokenTypeFlag.Literal.flag),
    Float(14 or TokenTypeFlag.Literal.flag),

    Plus(20 or TokenTypeFlag.Operator.flag),
    Minus(21 or TokenTypeFlag.Operator.flag),
    Multiply(22 or TokenTypeFlag.Operator.flag),
    Divide(23 or TokenTypeFlag.Operator.flag),
    Power(24 or TokenTypeFlag.Operator.flag),
    Modulus(25 or TokenTypeFlag.Operator.flag),
    Concat(26 or TokenTypeFlag.Operator.flag),
    UnaryMinus(27 or TokenTypeFlag.Operator.flag),

    And(30 or TokenTypeFlag.Operator.flag),
    Or(31 or TokenTypeFlag.Operator.flag),
    Xor(32 or TokenTypeFlag.Operator.flag),
    Not(33 or TokenTypeFlag.Operator.flag),

    Equal(40 or TokenTypeFlag.Operator.flag),
    NotEqual(41 or TokenTypeFlag.Operator.flag),
    GreaterThan(42 or TokenTypeFlag.Operator.flag),
    GreaterThanOrEqual(43 or TokenTypeFlag.Operator.flag),
    LessThan(44 or TokenTypeFlag.Operator.flag),
    LessThanOrEqual(45 or TokenTypeFlag.Operator.flag),

    LeftParen(60),
    RightParen(61),
    LeftBrace(62),
    RightBrace(63),
    LeftBracket(64),
    RightBracket(65),
    Comma(66),
    Colon(67),

    CommentStart(90),
    CommentEnd(91),
    LineComment(92);
}


/**
 * Class representing a single token in the input string.
 */
internal class Token(
    val type: TokenType,
    val value: String,
    val keyword: Keyword?,
    val line: Int
) {
    init {
        assert(if (type == TokenType.Keyword) keyword != null else keyword == null)
    }

    /**
     * Returns true if this token is a literal.
     */
    val isLiteral get() = type.flag.hasFlag(TokenTypeFlag.Literal.flag)

    /**
     * Returns true if this token is an operator.
     */
    val isOperator get() = type.flag.hasFlag(TokenTypeFlag.Operator.flag)

    /**
     * TokenType to ByteCode lookup. Should include all token types that are operators.
     */
    private companion object {
        val byteCodeLookup = mapOf(
            TokenType.Plus to ByteCode.EvalAdd,
            TokenType.Minus to ByteCode.EvalSubtract,
            TokenType.Multiply to ByteCode.EvalMultiply,
            TokenType.Divide to ByteCode.EvalDivide,
            TokenType.Power to ByteCode.EvalPower,
            TokenType.Modulus to ByteCode.EvalModulus,
            TokenType.Concat to ByteCode.EvalConcat,
            TokenType.UnaryMinus to ByteCode.EvalNegate,
            TokenType.And to ByteCode.EvalAnd,
            TokenType.Or to ByteCode.EvalOr,
            TokenType.Xor to ByteCode.EvalXor,
            TokenType.Not to ByteCode.EvalNot,
            TokenType.Equal to ByteCode.EvalIsEqual,
            TokenType.NotEqual to ByteCode.EvalIsNotEqual,
            TokenType.GreaterThan to ByteCode.EvalIsGreaterThan,
            TokenType.GreaterThanOrEqual to ByteCode.EvalIsGreaterThanOrEqual,
            TokenType.LessThan to ByteCode.EvalIsLessThan,
            TokenType.LessThanOrEqual to ByteCode.EvalIsLessThanOrEqual,
        )
    }

    /**
     * Gets the corresponding bytecode for this operator token.
     * Throws an exception if this token is not an operator.
     */
    fun getOperatorByteCode(): ByteCode {
        require(isOperator) { "GetOperatorByteCode() called on non-operator ($type) token." }
        return byteCodeLookup[type]
            ?: throw Exception("GetOperatorByteCode(): No bytecode for operator $type")
    }

    /**
     * Primarily used for debugging purposes.
     */
    override fun toString() = "\"$value\" ($type on line $line)"
}