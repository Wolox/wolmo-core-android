package ar.com.wolox.wolmo.core.callback;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * This class is a implementation of Retrofit's {@link Callback}.
 * It helps to differentiate and handle call errors, request errors and handle authentication errors.
 * Only one method will be invoked in response to a given request.
 * <p>
 * All the callback methods are executed using an executor.
 *
 * @param <T> Successful response body type
 */
public abstract class WoloxCallback<T> implements Callback<T> {

    /**
     * WoloxCallback's implementation of Retrofit's {@link Callback#onResponse(Call, Response)}
     * callback. Try using WoloxCallback's {@link #onResponseSuccessful(Object)} and {@link
     * #onResponseFailed(ResponseBody, int)} in your project.
     */
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (isAuthError(response)) {
            handleAuthError(response);
        } else if (response.isSuccessful()) {
            onResponseSuccessful(response.body());
        } else {
            onResponseFailed(response.errorBody(), response.code());
        }
    }

    /**
     * WoloxCallback's implementation of Retrofit's {@link Callback#onFailure(Call, Throwable)} callback.
     * Try using WoloxCallback's {@link #onCallFailure(Throwable)} in your project.
     */
    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onCallFailure(t);
    }

    /**
     * Checks whether the response is an auth error or not.
     * <p>
     * You should override this method and check if the response is an auth error, then return
     * <b>true</b> if it is. By default, this method returns <b>false</b>.
     *
     * @param response Retrofit response
     * @return <b>true</b> if the response is an auth error, <b>false</b> otherwise
     */
    protected boolean isAuthError(Response<T> response) {
        return false;
    }

    /**
     * Handles the auth error response.
     * This method is only called when there is an auth error. (If {@link #isAuthError(Response)}
     * returns true).
     * <p>
     * You should remove tokens and do the corresponding cleaning inside this method.
     * By default, this method does nothing.
     *
     * @param response Retrofit response
     */
    protected void handleAuthError(Response<T> response) {}

    /**
     * Successful HTTP response.
     *
     * @param response the API JSON response converted to a Java object.
     *                 The API response code is included in the response object.
     */
    public abstract void onResponseSuccessful(T response);

    /**
     * Successful HTTP response but has an error body.
     *
     * @param responseBody The error body
     * @param code The error code
     */
    public abstract void onResponseFailed(ResponseBody responseBody, int code);

    /**
     * Invoked when a network or unexpected exception occurred during the HTTP request, meaning
     * that the request couldn't be executed.
     *
     * @param t A Throwable with the cause of the call failure
     */
    public abstract void onCallFailure(Throwable t);

}