package ar.com.wolox.wolmo.core.service.provider;

import java.util.List;

import retrofit.Callback;

public interface Provider<T> {

    void provide(int currentPage, int itemsPerPage, Callback<List<T>> callback);

}