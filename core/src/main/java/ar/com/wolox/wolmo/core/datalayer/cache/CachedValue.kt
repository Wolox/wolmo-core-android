package ar.com.wolox.wolmo.core.datalayer.cache

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Represents a value that could be updated if certain criteria met,
 * or restore a previously cached value.
 * @param T type of the value to be cached.
 */
class CachedValue<T> {

    private var value: T? = null

    private val mutex: Mutex = Mutex()

    suspend fun fetch(policy: CachePolicy<T>): T? {
        mutex.withLock {
            if (policy.shouldInvalidate(value)) {
                value = policy.update(value)
            }
        }
        return mutex.withLock { value }
    }

}

internal class ExampleRepository {
    private val latestNews: CachedValue<List<String>> = CachedValue()
    private val retainPolicy = object : CachePolicy<List<String>> {
        var refresh = true
        var firstTime = true

        override suspend fun update(value: List<String>?): List<String> {
            delay(2000L)
            if (!firstTime) {
                refresh = false
            }
            val news = if (firstTime) listOf("News 1", "News 2") else listOf("News 3", "News 4")
            firstTime = false
            return news
        }

        override fun shouldInvalidate(value: List<String>?): Boolean {
            return value.isNullOrEmpty() || refresh
        }
    }

    suspend fun fetchNews(): List<String>? {
        return latestNews.fetch(retainPolicy)
    }
}

// Example Usage
suspend fun main() {
    val exampleRepository = ExampleRepository()
    coroutineScope {
        launch {
            println(exampleRepository.fetchNews()) // Not cached, 2" cost.
            println(exampleRepository.fetchNews()) // Not cached, 2" cost.
            println(exampleRepository.fetchNews()) // Cached, no cost.
        }
    }
}
