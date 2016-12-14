package ar.com.wolox.wolmo.core.service.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public abstract class ApiRestInterceptor implements Interceptor {

    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String ACCEPT_HEADER = "Accept";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder requestBuilder = request.newBuilder()
                .addHeader(CONTENT_TYPE_HEADER, "application/json")
                .addHeader(ACCEPT_HEADER, "application/json");
        addHeaders(requestBuilder);
        request = requestBuilder.build();
        return chain.proceed(request);
    }

    public abstract void addHeaders(Request.Builder requestBuilder);
}
