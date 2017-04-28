package ar.com.wolox.wolmo.core.service;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ar.com.wolox.wolmo.core.service.serializer.WoloxGsonBuilder;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Provides a repository of retrofit services for the application.
 * This stores a single instance of every service doing a lazy building when it's requested.
 * You should extend this class and return the API endpoint in {@link #getApiEndpoint()}, all the
 * services will be built using that endpoint.
 */
public abstract class WoloxRetrofitServices {

    private Retrofit mRetrofit;
    private Map<Class, Object> mServices;

    /**
     * Initializes the retrofits services.
     * This builds the {@link Retrofit} needed to create the services.
     */
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
    @NonNull
    public abstract String getApiEndpoint();

    /**
     * Returns an instance of {@link Gson} to use for conversion.
     * <p>
     * This method creates the GsonBuilder instance by calling to {@link
     * WoloxGsonBuilder#getBasicGsonBuilder()} and then calling {@link #initGson(GsonBuilder)} to
     * configure it before building Gson.
     * <p>
     * Override this method if you want to return a custom {@link Gson}. Please note that you don't
     * need to override this method to configure {@link Gson} or its builder, you can override
     * {@link #initGson(GsonBuilder)} to configure it.
     *
     * @return A configured Gson instance
     */
    @NonNull
    protected Gson getGson() {
        GsonBuilder builder = WoloxGsonBuilder.getBasicGsonBuilder();
        initGson(builder);
        return builder.create();
    }

    /**
     * Configures a {@link GsonBuilder}.
     * You must add serializers and/or deserializers inside this method.
     * By default this method does nothing.
     *
     * @param builder Builder to configure
     */
    protected void initGson(@NonNull GsonBuilder builder) {}

    /**
     * Returns an OkHttpClient.
     * This method calls {@link #initClient(OkHttpClient.Builder)} to configure the builder for OkHttpClient.
     *
     * @return A configured instance of OkHttpClient.
     */
    @NonNull
    protected OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        initClient(builder);
        return builder.build();
    }

    /**
     * Configures an {@link OkHttpClient.Builder}.
     * You must add interceptors and configure the builder inside this method.
     * <p>
     * By default this method adds a {@link HttpLoggingInterceptor} to the builder.
     */
    protected void initClient(@NonNull OkHttpClient.Builder builder) {
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

}