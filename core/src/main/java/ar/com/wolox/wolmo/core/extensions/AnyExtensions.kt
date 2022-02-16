package ar.com.wolox.wolmo.core.extensions

import java.lang.Exception

private const val EMPTY_INVOKE_STRING = "kotlin.Unit"
private const val NULL_STRING = "null"

fun (() -> Any?).toStringSafely(): String? = try {
    with(this.invoke().toString()) {
        return if (this == EMPTY_INVOKE_STRING) String() else this
    }
} catch (e: Exception) {
   NULL_STRING
}
