package ar.com.wolox.wolmo.core.extensions

/**
 * This method works to avoid the usage of the elvis operator in cases where
 * it messes up the code. We strongly recommend avoiding abuse in its use.
 *
 * Recommended cases:
 * - (onValueNullable ?: defaultValue).doSomething()                 ✘
 *   oneValueNullable.orElse(defaultValue).doSomething()             ✓
 *
 * - doSomething((oneValueNullable ?: defaultValue).anyAttribute)    ✘
 * - doSomething(oneValueNullable.orElse(defaultValue).anyAttribute) ✓
 *
 * NOT recommended cases:
 * - doSomething(onValueNullable ?: defaultValue)                    ✓
 * - doSomething(onValueNullable.orElse(defaultValue))               ✘
 *
 * - val value = onValueNullable ?: defaultValue                     ✓
 * - val value = onValueNullable.orElse(defaultValue)                ✘
 */
fun <T> T?.orElse(other: T) = this ?: other
