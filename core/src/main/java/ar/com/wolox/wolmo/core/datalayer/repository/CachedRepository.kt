package ar.com.wolox.wolmo.core.datalayer.repository

import ar.com.wolox.wolmo.core.datalayer.cache.CachePolicy
import ar.com.wolox.wolmo.core.datalayer.cache.CachedValue
import ar.com.wolox.wolmo.core.datalayer.cache.UsageThresholdPolicy
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import retrofit2.Response

@Suppress("UNCHECKED_CAST")
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

    open suspend fun fetchWithPolicy(key: String): T? {
        // TODO: We should check if we could return subtypes of T (E : T ?).
        return entries[key]?.fetch(policy)?.second
    }

}

// TODO: This Repository is for experimental causes.

class UserRepository(private val userAPI: UserAPI) {

    private val userInformation : CachedValue<UserInformation> = CachedValue()
    private val userUpdatePolicy = UsageThresholdPolicy(
        threshold = 5,
        updater = { fetchUserInformationUsingAPI() }
    )

    suspend fun fetchUserInformation(): UserInformation? {
        return userInformation.fetch(userUpdatePolicy)
    }

    private suspend fun fetchUserInformationUsingAPI() : UserInformation {
        val response = userAPI.fetchUserInformation()
        return response.body()!!
    }


}

interface UserAPI {
    suspend fun fetchUserInformation() : Response<UserInformation>
}

data class UserInformation(
    val username: String,
    val userTier: String,
    val userCountry: String
)

suspend fun main() {
    val r = UserRepository(
        object : UserAPI {
            override suspend fun fetchUserInformation(): Response<UserInformation> {
                return Response.success(UserInformation("Lisandro", "Normal", "ARG"))
            }
        }
    )
    coroutineScope {
        launch {
            val info = r.fetchUserInformation()
            println(info)
            repeat(10) {
                r.fetchUserInformation()
            }
        }
    }

}
