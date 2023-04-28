package silklang.silk.compiler

class LexicalHelper(private val lexer: LexicalAnalyzer, text: String? = null) {

    /**
     * Represents an invalid character. This character is returned when a valid character
     * is not available, such as when returning a character beyond the end of the text.
     * The character is represented as '\u0000'.
     */
    companion object {
        const val NullChar = '\u0000'
        private const val Escape: Char = '\\'
        private val EscapeCharacterLookup = mapOf(
            't' to '\t',
            'r' to '\r',
            'n' to '\n',
            '\'' to '\'',
            '"' to '"',
            '\\' to '\\'
        )
    }

    /**
     * Returns the current text being parsed.
     */
    var text: String = text ?: ""
        private set

    /**
     * Returns the current position within the text being parsed.
     */
    var index: Int = 0
        private set

    /**
     * Returns the 1-based line number for the current position.
     */
    var line: Int = 1
        private set

    /**
     * Indicates if the current position is at the end of the text being parsed.
     */
    val endOfText: Boolean
        get() = index >= text.length

    /**
     * Returns the number of characters not yet parsed. This is equal to the length of the
     * text being parsed minus the current position within that text.
     */
    val remaining: Int
        get() = text.length - index

    init {
        reset()
    }

    /**
     * Resets the current position to the start of the current text.
     */
    fun reset() {
        index = 0
        line = 1
    }

    /**
     * Sets the text to be parsed and resets the current position to the start of that text.
     * @param [text] The text to be parsed.
     */
    fun reset(text: String?) {
        this.text = text ?: ""
        reset()
    }

    /**
     * Returns the character at the current position, or NullChar if we're
     * at the end of the text being parsed.
     * @return The character at the current position.
     */
    fun peek(): Char = peek(0)

    /**
     * Returns the character at the specified number of characters beyond the current
     * position, or NullChar if the specified position is at the end of the
     * text being parsed.
     * @param [count] The number of characters beyond the current position.
     * @return The character at the specified position.
     */
    fun peek(count: Int): Char {
        val pos = index + count
        return if (pos < text.length) text[pos] else NullChar
    }

    /**
     * Moves the current position ahead one character. The position will not
     * be placed beyond the end of the text being parsed.
     */
    fun moveAhead() {
        if (index < text.length) {
            if (text[index++] == '\n')
                line++
        }
    }

    /**
     * Moves the current position ahead the specified number of characters. The position
     * will not be placed beyond the end of the text being parsed.
     * @param [count] The number of characters to move ahead.
     */
    fun moveAhead(count: Int) {
        val end = minOf(index + count, text.length)
        while (index < end) {
            if (text[index++] == '\n')
                line++
        }
    }

    /**
     * Moves to the next occurrence of the specified string within the text being parsed.
     * Returns true if a match was found. Otherwise, false is returned.
     * @param [s] String to find.
     * @return True if a match was found, false otherwise.
     */
    fun moveTo(s: String): Boolean {
        while (index <= text.length - s.length) {
            if (matchesCurrentPosition(s))
                return true
            if (text[index++] == '\n')
                line++
        }
        return false
    }

    /**
     * Moves to the next occurrence of any one of the specified characters.
     * Returns true if a match was found. Otherwise, false is returned.
     * @param [chars] Array of characters to search for.
     * @return True if a match was found, false otherwise.
     */
    fun moveTo(vararg chars: Char): Boolean {
        while (index < text.length) {
            if (chars.contains(text[index]))
                return true
            if (text[index++] == '\n')
                line++
        }
        return false
    }

    /**
     * Moves the current position forward to the next newline character.
     */
    fun moveToEndOfLine() = moveTo('\n')

    fun movePastWhitespace() {
        var c = peek()
        while (c.isWhitespace() && c != '\n') {
            moveAhead()
            c = peek()
        }
    }

    fun parseWhile(predicate: (c: Char) -> Boolean): String {
        val start = index
        while (predicate(peek())) {
            moveAhead()
        }
        return extract(start, index)
    }

    /**
     * Moves to the end of quoted text and returns the text within the quotes. Discards the
     * quote characters. Assumes the current position is at the starting quote character.
     */
    fun parseQuotedText(): String {
        val quote = text[index]
        index++
        val builder = StringBuilder()
        while (!endOfText) {
            val c = text[index]
            when {
                c == quote -> {
                    index++
                    break
                }
                c == '\r' || c == '\n' -> {
                    lexer.onError(ErrorCode.NEW_LINE_IN_STRING)
                    break
                }
                c == Escape -> {
                    index++
                    if (!endOfText) {
                        val r = text[index]
                        when {
                            EscapeCharacterLookup.containsKey(r) -> {
                                builder.append(EscapeCharacterLookup[r])
                            }
                            r == '\r' || r == '\n' -> {
                                builder.append(r)
                                if (r == '\r' && peek(1) == '\n') {
                                    index++
                                    builder.append(text[index])
                                }
                            }
                            else -> {
                                builder.append(Escape)
                                builder.append(r)
                            }
                        }
                    }
                }
                else -> builder.append(c)
            }
            index++
        }
        return builder.toString()
    }

    /**
     * Extracts a substring from the specified range of the text being parsed.
     *
     * @param start 0-based position of first character to extract.
     * @param end 0-based position of the character that follows the last character to extract.
     * @return Returns the extracted string
     */
    fun extract(start: Int, end: Int): String = text.substring(start, end)


    /**
     * Compares the given string to text at the current position.
     *
     * @param s String to compare.
     */
    private fun matchesCurrentPosition(s: String): Boolean {
        if (s.length > remaining) return false
        for (i in s.indices) {
            if (s[i] != text[index + i]) {
                return false
            }
        }
        return true
    }
}