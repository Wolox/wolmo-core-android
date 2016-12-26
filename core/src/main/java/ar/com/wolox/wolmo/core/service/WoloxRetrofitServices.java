package ar.com.wolox.wolmo.core.service;

import com.google.gson.Gson;

import ar.com.wolox.wolmo.core.service.serializer.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class WoloxRetrofitServices {

    private Retrofit mRetrofit;
    private Map<Class, Object> mServices;

    public void init() {
        mServices = new HashMap<>();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(getApiEndpoint())
                .addConverterFactory(GsonConverterFactory.create(getGson()))
                .client(getOkHttpClient())
                .build();
    }

    /**
     * Returns the API endpoint.
     *
     * @return URL endpoint
     */
    public abstract String getApiEndpoint();

    /**
     * Returns an instance of Gson to use for conversion.
     * This method calls <i>initGson(builder)</i> to configure the Gson Builder.
     *
     * @return A configured Gson instance
     */
    protected Gson getGson() {
        com.google.gson.GsonBuilder builder = GsonBuilder.getBasicGsonBuilder();
        initGson(builder);
        return builder.create();
    }

    /**
     * Configure gson builder.
     * You must add serializers and/or deserializers inside this method.
     *
     * @param builder Builder to configure
     */
    protected void initGson(com.google.gson.GsonBuilder builder) {
    }

    /**
     * Returns an OkHttpClient.
     * This method calls <i>initClient(builder)</i> to configure the builder for OkHttpClient.
     *
     * @return A configured instance of OkHttpClient.
     */
    protected OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        initClient(builder);
        return builder.build();
    }

    /**
     * Configures an <i>OkHttpClient.Builder</i>.
     * You must add interceptors and configure the builder inside this method.
     */
    protected void initClient(OkHttpClient.Builder builder) {
        HttpLoggingInterceptor loggerInterceptor = new HttpLoggingInterceptor();
        loggerInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        builder.addInterceptor(loggerInterceptor);
    }

    /**
     * Builds and returns a Retrofit Service.
     * If the service wasn't accessed, it'll be created and cached internally.
     * On successive requests, the already created instance will be returned.
     *
     * @param clazz RetrofitService Class
     * @param <T> Service class
     * @return service
     */
    public <T> T getService(Class<T> clazz) {
        T service = (T) mServices.get(clazz);
        if (service != null) return service;
        service = mRetrofit.create(clazz);
        mServices.put(clazz, service);
        return service;
    }

    /* Override this class and define the services like this:
     *
     * public static UserService user() {
     *  return getService(UserService.class);
     * }
     *
     */
}