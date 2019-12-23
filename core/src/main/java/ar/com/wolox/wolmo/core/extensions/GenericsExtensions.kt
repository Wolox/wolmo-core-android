package ar.com.wolox.wolmo.core.extensions

fun <T> T?.orElse(other: T) = this ?: other

val <T> T?.unit
    get() = Unit