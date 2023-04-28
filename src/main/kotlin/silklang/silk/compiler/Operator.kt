package silklang.silk.compiler

class Operator(val char: Char, val type: TokenType, vararg secondaries: Operator) {
    val secondaryChars = mutableListOf(*secondaries)

    init {
        secondaryChars.addAll(secondaries)
    }
}