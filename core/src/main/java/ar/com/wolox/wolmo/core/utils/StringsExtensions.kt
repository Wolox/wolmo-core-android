package ar.com.wolox.wolmo.core.utils

fun String.replaceWith(replacementSeparator: Pair<String,String>, replacement: String): String {
    val start = indexOf(replacementSeparator.first)
    val end = indexOf(replacementSeparator.second)

    return subSequence(0, start).toString() + replacement + subSequence(end + 1, length).toString()
}

// Example Use
fun main() {

    val example1 = "Hello {username}!"
    val example2 = "{username}"
    val example3 = "{username} username"

    println(example1.replaceWith("{" to "}", "Lionel"))
    println(example2.replaceWith("{" to "}", "Lionel"))
    println(example3.replaceWith("{" to "}", "Lionel"))

}
