package ar.com.wolox.wolmo.core.service.interceptor;

import com.squareup.okhttp.Request;

public class SecuredRequestInterceptor extends ApiRestInterceptor {

    public static final String SESSION_TOKEN_HEADER = "Authorization";

    public void addHeaders(Request.Builder requestBuilder) {
        String token = "holis"; // AccessUtils.getSessionToken()
        if (token == null) return;
        requestBuilder.addHeader(SESSION_TOKEN_HEADER, token);
    }
}