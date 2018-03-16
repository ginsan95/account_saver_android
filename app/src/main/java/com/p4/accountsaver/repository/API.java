package com.p4.accountsaver.repository;

import com.p4.accountsaver.model.Account;
import com.p4.accountsaver.model.Profile;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by averychoke on 27/2/18.
 */

public interface API {
    // region APIs
    @POST("users/login")
    Call<Profile> login(@Body LoginBody loginBody);

    @GET("data/Account?sortBy=game_name")
    Call<List<Account>> fetchAccounts(@Query("where") String where, @Query("offset") int offset, @Query("pageSize") int pageSize);

    @POST("data/Account")
    Call<Account> saveAccount(@Body Account account);

    @PUT("data/Account/{id}")
    Call<Account> updateAccount(@Path("id") String id, @Body Account account);
    // endregion


    // region Body classes
    class LoginBody {
        private String login;
        private String password;

        public LoginBody(String login, String password) {
            this.login = login;
            this.password = password;
        }
    }
    // endregion

    // region Listener
    public interface ApiListener<T> {
        void onSuccess(T object);

        void onFailure(ApiError error);
    }
    // endregion
}
