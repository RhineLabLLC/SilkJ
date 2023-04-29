package silklang.silk.compiler

class Label(val name: String, val ip: Int?) {
    val fixUpIps = ArrayList<Int>()
}