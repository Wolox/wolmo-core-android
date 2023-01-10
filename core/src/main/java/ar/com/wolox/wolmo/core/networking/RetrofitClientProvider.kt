package ar.com.wolox.wolmo.core.networking

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClientProvider {

    /**
     * Given a baseUrl and a lambda that decorates as desired the [OkHttpClient.Builder]
     * this methods returns a proper [Retrofit] client with such characteristics.
     * @param baseUrl the Base Url where the calls will be produced.
     * @param decoration custom lambda that decorates the builder, for example, adding multiple interceptors.
     */
    fun provideRetrofitClient(
        baseUrl: String ,
        decoration: (OkHttpClient.Builder) -> OkHttpClient.Builder
    ) : Retrofit {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClientBuilder = OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor)
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(decoration(okHttpClientBuilder).build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}

fun OkHttpClient.Builder.addInterceptors(interceptors: List<Interceptor>): OkHttpClient.Builder {
    this.interceptors().addAll(interceptors)
    return this
}
