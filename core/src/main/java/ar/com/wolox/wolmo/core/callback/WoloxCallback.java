package ar.com.wolox.wolmo.core.callback;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public abstract class WoloxCallback<T> implements Callback<T> {

    /**
     * WoloxCallback's implementation of Retrofit's onResponse() callback
     * Try using WoloxCallback's onResponseSuccessful() and onResponseFailed() in your project.
     */
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful())
            onResponseSuccessful(response.body());
        else
            onResponseFailed(response.errorBody(), response.code());
    }

    /**
     * WoloxCallback's implementation of Retrofit's onFailure() callback
     * Try using WoloxCallback's onCallFailure() in your project.
     */
    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onCallFailure(t);
    }

    /**
     * Successful HTTP response
     * @param response the API JSON response converted to a Java object.
     *                 The API response code is included in the response object.
     */
    public abstract void onResponseSuccessful(T response);

    /**
     * Successful HTTP response but has an error body
     * @param responseBody The error body
     * @param code The error code
     */
    public abstract void onResponseFailed(ResponseBody responseBody, int code);

    /**
     * Invoked when a network or unexpected exception occurred during the HTTP request, meaning
     * that the request couldn't be executed.
     * @param t A Throwable with the cause of the call failure
     */
    public abstract void onCallFailure(Throwable t);

}