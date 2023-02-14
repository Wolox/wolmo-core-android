package ar.com.wolox.wolmo.core.datalayer.repository

import ar.com.wolox.wolmo.core.datalayer.cache.CachePolicy
import ar.com.wolox.wolmo.core.datalayer.cache.CachedValue
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

abstract class CachedRepository<T>(
    keys: List<String>,
) {

    private val entries : MutableMap<String, CachedValue<Pair<String, T>>> = mutableMapOf()

    init {
        keys.forEach { key ->
            entries[key] = CachedValue()
        }
    }

    abstract val policy: CachePolicy<Pair<String, T>>

    operator fun get(key: String) = entries[key]

    open suspend fun fetchWithPolicy(key: String): T? {
        return entries[key]?.fetch(policy)?.second
    }

}

class UserRepository : CachedRepository<Person>(
    listOf("friend","enemy"),
) {
    override val policy: CachePolicy<Pair<String, Person>> = object : CachePolicy<Pair<String, Person>> {

        var usages: MutableMap<String, Int> = mutableMapOf()

        val threshold = 2 // 0, 1, 2 (3 allowed)

        override suspend fun update(value: Pair<String, Person>?): Pair<String, Person> {
            return if (value?.first == "enemy") "enemy" to Enemy("Pepe", "")
            else "friend" to Friends("Epep")
        }

        override fun shouldInvalidate(value: Pair<String, Person>?): Boolean {
            val invalidate =
                value == null || (usages[value.first] != null && usages[value.first]!! > threshold)
            if (!invalidate) {
                usages[value!!.first] = (usages[value.first] ?: 1) + 1
            } else {
                println("Invalidation occur.")
            }
            return invalidate
        }
    }




    suspend fun fetchFriend(): Friends = fetchWithPolicy("friend") as Friends
    suspend fun fetchEnemy(): Enemy = fetchWithPolicy("enemy") as Enemy

}

interface Person {
    val name: String
}

data class Friends(
    override val name: String
) : Person

data class Enemy(
    override val name: String,
    val surname: String
) : Person

suspend fun main() {
    val r = UserRepository()
    coroutineScope {
        launch {
            r.fetchFriend()
            r.fetchFriend()
            r.fetchFriend()
            r.fetchFriend()
        }
    }

}
