package silklang.silk.compiler

import silklang.silk.utility.Extensions.isHexDigit
import silklang.silk.utility.Extensions.makeQuoted

class LexicalAnalyzer(private var source: String? = null) {
    // symbol characters
    companion object {
        val symbolFirstChars = "_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toHashSet()
        val symbolChars = "_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toHashSet()

        // Operator lookup table
        val operatorLookup = hashMapOf(
            '+' to Operator('+', TokenType.Plus),
            '-' to Operator('-', TokenType.Minus),
            '*' to Operator('*', TokenType.Multiply),
            '/' to Operator('/', TokenType.Divide,
                Operator('/', TokenType.LineComment),
                Operator('*', TokenType.CommentStart)),
            '^' to Operator('^', TokenType.Power),
            '%' to Operator('%', TokenType.Modulus),
            '&' to Operator('&', TokenType.Concat),
            '=' to Operator('=', TokenType.Equal),
            '<' to Operator('<', TokenType.LessThan,
                Operator('>', TokenType.NotEqual),
                Operator('=', TokenType.LessThanOrEqual)),
            '>' to Operator('>', TokenType.GreaterThan,
                Operator('=', TokenType.GreaterThanOrEqual)),
            '(' to Operator('(', TokenType.LeftParen),
            ')' to Operator(')', TokenType.RightParen),
            '{' to Operator('{', TokenType.LeftBrace),
            '}' to Operator('}', TokenType.RightBrace),
            '[' to Operator('[', TokenType.LeftBracket),
            ']' to Operator(']', TokenType.RightBracket),
            ',' to Operator(',', TokenType.Comma),
            ':' to Operator(':', TokenType.Colon)
        )

        // symbol operator lookup
        val symboloperatorLookup = mapOf(
            "and" to TokenType.And,
            "or" to TokenType.Or,
            "xor" to TokenType.Xor,
            "not" to TokenType.Not
        )
    }

    // Multiline comment terminator
    private val commentEnd = "*/"

    private var savedUngetToken: Token? = null

    var lastTokenLine: Int = 0
        private set

    val currentLine: Int
        get() = lexHelper.line

    val text: String
        get() = lexHelper.text

    private val lexHelper = LexicalHelper(this)

    fun reset(source: String? = null) {
        this.source = source
        lexHelper.reset(source)
        savedUngetToken = null
    }

    fun peekNext(): Token {
        savedUngetToken = getNext()
        return savedUngetToken!!
    }

    fun getNextSkipNewLines(): Token {
        var token = getNext()
        while (token.type == TokenType.EndOfLine) {
            token = getNext()
        }
        return token
    }

    // Methods
    fun getNext(): Token {
        var tokenType: TokenType
        var tokenValue: String
        var tokenStart: Int
        var tokenLine: Int

        // Return saved "unget" token, if any
        if (savedUngetToken != null) {
            val token = savedUngetToken
            savedUngetToken = null
            return token!!
        }

        parseNextToken@ while (true) {
            // Skip any whitespace (does not skip newlines)
            lexHelper.movePastWhitespace()

            // Save current line number
            val lastTokenLine = currentLine

            // Test for end of input text
            if (lexHelper.endOfText) {
                return Token(TokenType.EndOfFile, "<EndOfFile>", lexHelper.line)
            }

            // Peek next character
            val c = lexHelper.peek()
            tokenStart = lexHelper.index
            tokenLine = lexHelper.line

            // symbol (keyword, symbol, function or character-based operator)
            if (symbolFirstChars.contains(c)) {
                tokenValue = lexHelper.parseWhile { symbolChars.contains(it) }
                return if (symboloperatorLookup.containsKey(tokenValue)) {
                    tokenType = symboloperatorLookup[tokenValue]!!
                    Token(tokenType, tokenValue, tokenLine)
                } else if (Keywords.isKeyword(tokenValue)) {
                    Token(TokenType.Keyword, tokenValue, tokenLine).keyword(Keywords.getKeyword(tokenValue))
                } else {
                    Token(TokenType.Symbol, tokenValue, tokenLine)
                }
            }

            // Number
            if (c.isDigit() || c == '.') {
                var count: Int
                var gotDigit = false
                tokenType = TokenType.Integer

                if (c == '0' && lexHelper.peek(1) == 'x') {
                    // Hexadecimal literal
                    count = 2
                    while (lexHelper.peek(count).isHexDigit()) {
                        count++
                    }
                    if (count > 2) {
                        gotDigit = true
                        tokenType = TokenType.Integer
                    }
                } else {
                    // Decimal literal
                    count = 1
                    var gotDecimal = (c == '.')
                    gotDigit = c.isDigit()

                    while (true) {
                        val ch = lexHelper.peek(count)
                        if (ch == '.') {
                            if (gotDecimal) {
                                break
                            }
                            gotDecimal = true
                        } else if (ch.isDigit()) {
                            gotDigit = true
                        } else {
                            break
                        }
                        count++
                    }
                    if (gotDigit) {
                        tokenType = if (gotDecimal) TokenType.Float else TokenType.Integer
                    }
                }
                if (gotDigit) {
                    lexHelper.moveAhead(count)
                    return Token(tokenType, lexHelper.extract(tokenStart, lexHelper.index), tokenLine)
                }
            }

            // String
            if (c == '"' || c == '\'') {
                return Token(TokenType.String, lexHelper.parseQuotedText(), tokenLine)
            }

            // Operator
            if (operatorLookup.containsKey(c)) {
                val info = operatorLookup[c]
                lexHelper.moveAhead()
                val ch = lexHelper.peek()
                for (secondaryInfo in info!!.secondaryChars) {
                    if (ch == secondaryInfo.char) {
                        lexHelper.moveAhead()
                        // If comments, just consume them
                        if (consumeComment(secondaryInfo.type)) {
                            continue@parseNextToken
                        }
                        return Token(
                            secondaryInfo.type,
                            lexHelper.extract(tokenStart, lexHelper.index),
                            tokenLine
                        )
                    }
                }
                return Token(info.type, lexHelper.extract(tokenStart, lexHelper.index), tokenLine)
            }

            if (c == '\n') {
                lexHelper
                return Token(TokenType.EndOfLine, "<EndOfLine>", tokenLine)
            }

            // We don't know what to do with this character
            lexHelper.moveAhead()
            // onError(ErrorCode.UnexpectedCharacter, makeQuoted(c))
        }
    }

    private fun consumeComment(type: TokenType): Boolean {
        // Consume comments
        return when (type) {
            TokenType.CommentStart -> {
                lexHelper.moveTo(commentEnd)
                lexHelper.moveAhead(commentEnd.length)
                true
            }

            TokenType.LineComment -> {
                lexHelper.moveToEndOfLine()
                true
            }

            else -> {
                false
            }
        }
    }

    fun ungetToken(token: Token) {
        savedUngetToken = token
    }
}