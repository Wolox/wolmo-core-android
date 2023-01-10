package ar.com.wolox.wolmo.core.datalayer.cache

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlin.coroutines.coroutineContext

/**
 * [CachePolicy] that uses a [threshold] and a [refreshCriteria]
 * in order to check if an invalidation should occur.
 * @param threshold milliseconds wait for the next cache-miss.
 */
abstract class TimeAndRefreshPolicy<T>(
    private val threshold: Long
    ) : CachePolicy<T> {
    private var lastTime = System.currentTimeMillis()

    protected abstract fun refreshCriteria(value: T?): Boolean

    override fun shouldInvalidate(value: T?): Boolean {
        val currentTime = System.currentTimeMillis()
        return (currentTime - lastTime >= threshold) || refreshCriteria(value)
    }
}
