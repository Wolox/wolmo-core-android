package ar.com.wolox.wolmo.core.service.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public abstract class ApiRestInterceptor extends OkHttpClient {

    private static final String USER_AGENT_HEADER = "User-Agent";
    private static final String ACCEPT_HEADER = "Accept";

    public ApiRestInterceptor() {
        interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request.Builder requestBuilder = request.newBuilder()
                        .addHeader(USER_AGENT_HEADER, "Android")
                        .addHeader(ACCEPT_HEADER, "application/json");
                addHeaders(requestBuilder);
                request = requestBuilder.build();
                Response response = chain.proceed(request);
                return response;
            }
        });
    }

    public abstract void addHeaders(Request.Builder requestBuilder);
}
