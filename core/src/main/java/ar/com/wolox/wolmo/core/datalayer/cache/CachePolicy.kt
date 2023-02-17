package ar.com.wolox.wolmo.core.datalayer.cache

/**
 * Policy to determine behavior of the [CachedValue].
 */
interface CachePolicy< T > {
    suspend fun update(value: T?): T
    fun shouldInvalidate(value: T?): Boolean
}
