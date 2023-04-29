package silklang.silk.utility

object Extensions {
    fun makeQuoted(c: Char): String = "\"$c\""
    fun makeQuoted(s: String): String = "\"$s\""

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

    fun Char.isHexDigit(): Boolean = this in '0'..'9' && (this in 'a'..'f' || this in 'A'..'F')
}