package ar.com.wolox.wolmo.core.networking.interceptors

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 * An implementation of OkHTTP's [Interceptor] that adds common headers to every API
 * request and provides helper methods to add custom ones.
 */
abstract class ApiRestInterceptor : Interceptor {
    /**
     * Intercepts the API call and adds custom headers to the request. By default, it will
     * add both "Content-Type" and "Accept" headers.
     * If you wish to add more custom headers you may prefer using the method addHeaders() instead
     * of overwriting this one.
     *
     * @param chain an object provided by OkHTTP with data of the request being made
     *
     * @return an instance of [Response] by OkHTTP
     */
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()
        val requestBuilder: Request.Builder =
            request
                .newBuilder()
                .addHeader(CONTENT_TYPE_HEADER, "application/json")
                .addHeader(ACCEPT_HEADER, "application/json")
        addHeaders(requestBuilder)
        request = requestBuilder.build()
        return chain.proceed(request)
    }

    /**
     * A helper method to add custom headers to the network request.
     *
     * @param requestBuilder an instance of [Request.Builder] that you can use to add custom
     * headers.
     */
    abstract fun addHeaders(requestBuilder: Request.Builder)

    companion object {
        protected const val CONTENT_TYPE_HEADER = "Content-Type"
        protected const val ACCEPT_HEADER = "Accept"
    }
}
