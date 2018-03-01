package com.p4.accountsaver.repository;

import com.p4.accountsaver.model.Profile;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by averychoke on 27/2/18.
 */

public interface API {
    @POST("users/login")
    Call<Profile> login(@Body LoginBody loginBody);

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
