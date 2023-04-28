package silklang.silk.utility

object Extensions {
    fun makeQuoted(c: Char): String = "\"$c\""
    fun makeQuoted(s: String): String = "\"$s\""
    fun isHexDigit(c: Char): Boolean = c.isDigit() || (c in 'A'..'F') || (c in 'a'..'f')

    fun String.isNumeric(): Boolean = this
        .removePrefix("-")
        .removePrefix("+")
        .all { it in '0'..'9' || it == '.' }

    fun String.isFloatOrDouble(): Boolean = this
        .removeSuffix("D")
        .removeSuffix("F")
        .removeSuffix("d")
        .removeSuffix("f")
        .removePrefix("-")
        .removePrefix("+")
        .all { it in '0'..'9' || it == '.' }
        .and(this.contains("."))
}