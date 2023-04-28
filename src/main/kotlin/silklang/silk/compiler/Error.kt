package silklang.silk.compiler

enum class ErrorLevel(val description: String) {
    ERROR("ERROR"),
    FATAL_ERROR("FATAL ERROR");

    override fun toString(): String = description
}

enum class ErrorCode(val description: String) {
    NO_ERROR("Operation completed successfully"),
    TOO_MANY_ERRORS("Too many errors encountered"),
    INTERNAL_ERROR("Internal error"),

    CODE_OUTSIDE_FUNCTION("Code is not allowed outside of functions"),
    DUPLICATE_FUNCTION_NAME("More than one function defined with the same name"),
    DUPLICATE_LABEL("Label defined more than once"),
    FUNCTION_NOT_DEFINED("Function was not defined"),
    ILLEGAL_VAR("The VAR keyword can only appear within a function, or before the first function"),
    INVALID_STEP_VALUE("STEP value must be a non-zero numeric literal"),
    LABEL_NOT_DEFINED("Label was referenced but never defined"),
    MAIN_NOT_DEFINED("Function main() was not defined : You must define main() as the program's starting point"),
    NEW_LINE_IN_STRING("New line in string literal"),
    VARIABLE_ALREADY_DEFINED("Variable has already been defined"),
    ASSIGN_TO_READ_ONLY_VARIABLE("Cannot change read-only variable"),
    VARIABLE_NOT_DEFINED("Use of undefined variable"),
    WRONG_NUMBER_OF_ARGUMENTS("Wrong number of arguments"),

    EXPECTED_EQUALS("Expected equal sign \"=\""),
    EXPECTED_EXPRESSION("An expression was expected"),
    EXPECTED_LEFT_BRACE("Expected opening curly brace \"{\""),
    EXPECTED_LEFT_PAREN("Opening parenthesis expected \"(\""),
    EXPECTED_LITERAL("Expected literal value"),
    EXPECTED_OPERAND("Operand expected"),
    EXPECTED_RIGHT_BRACE("Closing curly brace \"}\" expected"),
    EXPECTED_RIGHT_PAREN("Closing parenthesis \")\" expected"),
    EXPECTED_RIGHT_BRACKET("Closing square bracket \"]\" expected"),
    EXPECTED_SYMBOL("Identifier name expected"),
    EXPECTED_TO("Expected TO keyword"),

    BREAK_WITHOUT_LOOP("Break statement outside of loop"),
    CONTINUE_WITHOUT_LOOP("Continue statement outside of loop"),

    UNEXPECTED_CHARACTER("Unexpected character encountered"),
    UNEXPECTED_KEYWORD("Keyword is unexpected here"),
    UNEXPECTED_TOKEN("Unexpected token encountered");

    override fun toString(): String = description
}

data class Error(
    val level: ErrorLevel,
    val code: ErrorCode,
    val description: String,
    val line: Int
) {

    constructor(level: ErrorLevel, code: ErrorCode, line: Int) :
            this(level, code, code.toString(), line)

    constructor(level: ErrorLevel, code: ErrorCode, token: Token) :
            this(level, code, "$code : \"${token.value}\"", token.line)

    constructor(level: ErrorLevel, code: ErrorCode, description: String, token: Token) :
            this(level, code, "$code : $description : \"${token.value}\"", token.line)

    override fun toString(): String =
        "${level.description} E${code.ordinal.toString().padStart(3, '0')} : $description (Line $line)"
}
