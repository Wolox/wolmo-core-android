package ar.com.wolox.wolmo.core.datalayer.cache

class UsageThresholdPolicy<T>(
    private val updater: suspend () -> T,
    private val threshold: Int = 1
) : CachePolicy<T> {

    override suspend fun update(value: T?): T = updater()!!

    private var usages : Int = 0
        get () {
            field++
            val tmp = field
            if(field > threshold)
                field = 0
            return tmp
        }

    override fun shouldInvalidate(value: T?): Boolean = value == null || usages >= threshold
}
