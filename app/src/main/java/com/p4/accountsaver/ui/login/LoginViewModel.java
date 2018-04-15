package com.p4.accountsaver.ui.login;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.databinding.ObservableField;
import android.text.TextUtils;
import android.util.Log;

import com.p4.accountsaver.R;
import com.p4.accountsaver.manager.ProfileManager;
import com.p4.accountsaver.model.Profile;
import com.p4.accountsaver.repository.API;
import com.p4.accountsaver.repository.ApiError;
import com.p4.accountsaver.repository.ApiEvent;
import com.p4.accountsaver.repository.BackendlessAPI;
import com.p4.accountsaver.ui.base.BaseViewModel;

/**
 * Created by averychoke on 25/2/18.
 */

public class LoginViewModel extends AndroidViewModel {
    private final Context mContext;

    public final ObservableField<String> username = new ObservableField<>();
    public final ObservableField<String> password = new ObservableField<>();
    public final ObservableField<String> usernameError = new ObservableField<>();
    public final ObservableField<String> passwordError = new ObservableField<>();

    private final MutableLiveData<ApiEvent> mLoginEvent = new MutableLiveData<>();
    private final MutableLiveData<Object> mSignUpEvent = new MutableLiveData<>();

    public LoginViewModel(Application context) {
        super(context);
        mContext = context.getApplicationContext();
    }

    public void login() {
        if (TextUtils.isEmpty(username.get())) {
            usernameError.set(null);
            usernameError.set(mContext.getString(R.string.username_empty_error));
        } else if (TextUtils.isEmpty(password.get())) {
            passwordError.set(null);
            passwordError.set(mContext.getString(R.string.password_empty_error));
        } else {
            mLoginEvent.setValue(new ApiEvent.Builder().inProgress(true).build());
            ProfileManager.getInstance().login(username.get(), password.get(), new API.ApiListener<Profile>() {
                @Override
                public void onSuccess(Profile object) {
                    mLoginEvent.setValue(new ApiEvent.Builder().success(true).build());
                }

                @Override
                public void onFailure(ApiError error) {
                    mLoginEvent.setValue(new ApiEvent.Builder().setError(error).build());
                }
            });
        }
    }

    public void signUp() {
        mSignUpEvent.setValue(new Object());
    }

    public LiveData<ApiEvent> getLoginEvent() {
        return mLoginEvent;
    }

    public LiveData<Object> getSignUpEvent() {
        return mSignUpEvent;
    }
}
