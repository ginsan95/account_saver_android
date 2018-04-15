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
import com.p4.accountsaver.model.SecurityQuestions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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
                .registerTypeAdapter(SecurityQuestions.class, new SecurityQuestionsAdapter())
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

    // region Profile
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

    public void signUp(String username, String name, String password, API.ApiListener<Boolean> listener) {
        mApi.signUp(new API.SignUpBody(username, name, password)).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getObjectId() != null) {
                    listener.onSuccess(true);
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

    public void logout(API.ApiListener<Boolean> listener) {
        mApi.logout().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    listener.onSuccess(true);
                } else {
                    listener.onFailure(new ApiError(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onFailure(new ApiError(t));
            }
        });
    }
    // endregion

    // region Account
    public void fetchAccounts(int offset, String searchTerm, API.ApiListener<List<Account>> listener) {
        StringBuilder sb = new StringBuilder();
        if (ProfileManager.getInstance().getProfile() != null) {
            sb.append("ownerId='");
            sb.append(ProfileManager.getInstance().getProfile().getObjectId());
            sb.append("'");
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

    public void saveAccount(Account account, API.ApiListener<Account> listener) {
        mApi.saveAccount(account).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(new ApiError(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                listener.onFailure(new ApiError(t));
            }
        });
    }

    public void updateAccount(Account account, API.ApiListener<Account> listener) {
        mApi.updateAccount(account.getObjectId(), account).enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(new ApiError(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                listener.onFailure(new ApiError(t));
            }
        });
    }

    public void deleteAccount(String id, API.ApiListener<Boolean> listener) {
        mApi.deleteAccount(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        listener.onSuccess(jsonObject.has("deletionTime"));
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        listener.onSuccess(false);
                    }
                } else {
                    listener.onFailure(new ApiError(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onFailure(new ApiError(t));
            }
        });
    }
    // endregion

    // region Game Icons
    public void fetchGameIcons(API.ApiListener<List<String>> listener) {
        mApi.fetchGameIcons(ProfileManager.getInstance().getProfile().getObjectId()).enqueue(new Callback<List<API.FileBody>>() {
            @Override
            public void onResponse(Call<List<API.FileBody>> call, Response<List<API.FileBody>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<String> gameIcons = new ArrayList<>();
                    if (response.isSuccessful() && response.body() != null) {
                        for (API.FileBody fileBody : response.body()) {
                            if (fileBody.publicUrl != null) {
                                gameIcons.add(fileBody.publicUrl);
                            }
                        }
                    }
                    listener.onSuccess(gameIcons);
                } else {
                    listener.onFailure(new ApiError(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<List<API.FileBody>> call, Throwable t) {
                listener.onFailure(new ApiError(t));
            }
        });
    }

    public void uploadGameIcon(byte[] iconData, API.ApiListener<String> listener) {
        Profile profile = ProfileManager.getInstance().getProfile();
        if (profile != null) {
            String name = "game-icon-" + (new Date().getTime()) + ".jpeg";
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", name,
                    RequestBody.create(MediaType.parse("image/*"), iconData));
            String path = profile.getObjectId() + "/" + name;

            mApi.uploadGameIcon(path, filePart).enqueue(new Callback<API.FileBody>() {
                @Override
                public void onResponse(Call<API.FileBody> call, Response<API.FileBody> response) {
                    if (response.isSuccessful() && response.body() != null && response.body().fileURL != null) {
                        listener.onSuccess(response.body().fileURL);
                    } else {
                        listener.onFailure(new ApiError(response.errorBody()));
                    }
                }

                @Override
                public void onFailure(Call<API.FileBody> call, Throwable t) {
                    listener.onFailure(new ApiError(t));
                }
            });
        } else {
            listener.onFailure(null);
        }
    }

    public void deleteGameIcons(String url) {
        String subUrl = url.substring(BASE_URL.length());
        mApi.deleteGameIcon(subUrl).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    // endregion
}
