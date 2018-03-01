package com.p4.accountsaver.repository;

import android.util.Log;

import com.p4.accountsaver.model.Profile;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by averychoke on 27/2/18.
 */

public class BackendlessAPI {
    private static final BackendlessAPI sInstance = new BackendlessAPI();
    private final String BASE_URL = "https://api.backendless.com/FDB083B0-BAF9-5AA7-FF6B-507294178300/AD715598-37F8-FC4D-FFC8-56D94399D600/";
    private final Retrofit mRetrofit;
    private final API mApi;

    private BackendlessAPI() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mApi = mRetrofit.create(API.class);
    }

    public static BackendlessAPI getInstance() {
        return sInstance;
    }

    public void login(String username, String password, API.ApiListener<Profile> listener) {
        mApi.login(new API.LoginBody(username, password)).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(new ApiError(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                listener.onFailure(new ApiError(t));
            }
        });
    }
}
