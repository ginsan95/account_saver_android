package com.p4.accountsaver.repository;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.*;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.p4.accountsaver.manager.ProfileManager;
import com.p4.accountsaver.model.Account;
import com.p4.accountsaver.model.Profile;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
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
    public static final int PAGE_SIZE = 10;

    private Retrofit mRetrofit;
    private API mApi;

    private BackendlessAPI() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new MyDateTypeAdapter())
                .create();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        mApi = mRetrofit.create(API.class);
    }

    public static BackendlessAPI getInstance() {
        return sInstance;
    }

    private void setToken(String token) {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder().addHeader("user-token", token).build();
                    return chain.proceed(request);
                })
                .build();
        mRetrofit = mRetrofit.newBuilder()
                .client(httpClient)
                .build();
        mApi = mRetrofit.create(API.class);
    }

    public void login(String username, String password, API.ApiListener<Profile> listener) {
        mApi.login(new API.LoginBody(username, password)).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Profile profile = response.body();
                    setToken(profile.getUserToken());
                    listener.onSuccess(profile);
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

    public void fetchAccounts(int offset, String searchTerm, API.ApiListener<List<Account>> listener) {
        StringBuilder sb = new StringBuilder();
        if (ProfileManager.getInstance().getProfile() != null) {
            sb.append("ownerId=");
            sb.append(ProfileManager.getInstance().getProfile().getOwnerId());
        }
        if (!TextUtils.isEmpty(searchTerm)) {
            if (sb.length() > 0) {
                sb.append(" AND ");
            }
            sb.append("game_name LIKE '%");
            sb.append(searchTerm);
            sb.append("%'");
        }

        mApi.fetchAccounts(sb.toString(), offset, PAGE_SIZE).enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(new ApiError(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<List<Account>> call, Throwable t) {
                listener.onFailure(new ApiError(t));
            }
        });
    }
}
