package ar.com.wolox.wolmo.core.networking

import retrofit2.Response

/**
 * Method Object that safely abstracts the typical service calls.
 */
object NetworkRequestHandler {

    /**
     * Static method that allows to execute requests from a [suspend] function of [Response] type
     * and returns a [NetworkResponse] object depending on HTTP response.
     */
    suspend fun <T: Response<*>> safeServiceCall(block: suspend () -> T): NetworkResponse<T> {
        return try {
            val response: T = block.invoke()
            if (response.isSuccessful) NetworkResponse.Success(response)
            else NetworkResponse.Error(response)
        } catch (t: Throwable) {
            NetworkResponse.Failure(t)
        }
    }

    /**
     * Static method that allows to execute requests from a [suspend] function of [Response] type
     * and returns a [NetworkResponse] object depending on HTTP response.
     * Also, this variant allows us to catch the throwable, giving us the option to
     * map it to another response.
     */
    suspend fun <T: Response<*>> safeServiceCall(
        block: suspend () -> T,
        catching: suspend (Throwable) -> T
    ): NetworkResponse<T> {
        return try {
            val response: T = block.invoke()
            if (response.isSuccessful) NetworkResponse.Success(response)
            else NetworkResponse.Error(response)
        } catch (t: Throwable) {
            NetworkResponse.CaughtFailure(catching(t))
        }
    }

}

/**
 * State Hierarchy that represents our main concerns from Backend Results.
 */
sealed class NetworkResponse<T> {

    /**
     * Successful HTTP response from the server.
     * The server received the request, answered it and the response is not of an error type.
     * It will return a [T] object, the API JSON response converted to a Java/Kotlin object,
     * which includes the API response code.
     */
    data class Success<T>(val response: T) : NetworkResponse<T>()

    /**
     * Successful HTTP response from the server, but has an error body.
     * The server received the request, answered it and reported an error.
     * It will return a [T], the API JSON response converted to a Java/Kotlin object,
     * which includes the API response code.
     */
    data class Error<T>(val response: T) : NetworkResponse<T>()

    /**
     * The HTTP request to the server failed on the local device, no data was transmitted.
     * Invoked when a network or unexpected exception occurred during the HTTP request, meaning
     * that the request couldn't be executed. The cause of the failure will be given through a
     * [Throwable] object
     */
    data class Failure<T>(val t: Throwable) : NetworkResponse<T>()

    data class CaughtFailure<T>(val failedValue: T) : NetworkResponse<T>()
}
