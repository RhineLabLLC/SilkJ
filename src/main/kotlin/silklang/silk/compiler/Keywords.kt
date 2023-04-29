package silklang.silk.compiler

import java.util.TreeMap

/**
 * Enum of supported keywords.
 */
enum class Keyword {
    NullKeyword,    // Not a keyword
    Var,
    GoTo,
    Return,
    If,
    Else,
    While,
    For,
    Break,
    Continue,
    // Keywords part of other statements/expressions
    And,
    Or,
    Xor,
    Not,
    To,
    Step,
}

/**
 * Static class for tracking and looking up keywords.
 */
object Keywords {
    private val keywordLookup = TreeMap<String, Keyword>(String.CASE_INSENSITIVE_ORDER).apply {
        put("var", Keyword.Var)
        put("goto", Keyword.GoTo)
        put("return", Keyword.Return)
        put("if", Keyword.If)
        put("else", Keyword.Else)
        put("while", Keyword.While)
        put("for", Keyword.For)
        put("break", Keyword.Break)
        put("continue", Keyword.Continue)
        put("to", Keyword.To)
        put("step", Keyword.Step)
    }

    /**
     * Returns true if the given string is a keyword.
     */
    fun isKeyword(name: String): Boolean {
        assert(isValidSymbolName(name))
        return keywordLookup.containsKey(name)
    }

    /**
     * If [name] is a keyword, this method returns true and sets [keyword]
     * to the corresponding [Keyword] value. Otherwise, this method returns false.
     */
    fun getKeyword(name: String): Keyword {
        assert(isValidSymbolName(name))
        return keywordLookup[name]!!
    }

    /**
     * Returns the keyword ID associated with the given keyword.
     * Throws an exception if the string is not a keyword.
     */
    fun lookupKeyword(name: String): Keyword {
        assert(isValidSymbolName(name))
        return keywordLookup[name]
            ?: throw IllegalArgumentException("'$name' is not a valid keyword.")
    }

    /**
     * Tests if all characters in the given name are valid identifier characters.
     */
    private fun isValidSymbolName(name: String): Boolean = name.matches(Regex("[_a-zA-Z][_a-zA-Z0-9]*"))

    /**
     * Tests if a name string is a keyword or equal to "main".
     */
    fun isReservedSymbol(name: String): Boolean {
        assert(isValidSymbolName(name))
        return isKeyword(name) || name.equals("Main", ignoreCase = true)
    }
}
