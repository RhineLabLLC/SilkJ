package silklang.silk.utility

object Flags {
    fun Int.hasFlag(flag: Int) = this and flag != 0
    fun Int.applyFlag(flag: Int) = this or flag
}