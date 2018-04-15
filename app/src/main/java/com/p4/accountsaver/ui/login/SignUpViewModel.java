package com.p4.accountsaver.ui.login;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.p4.accountsaver.R;
import com.p4.accountsaver.manager.ProfileManager;
import com.p4.accountsaver.model.Profile;
import com.p4.accountsaver.repository.API;
import com.p4.accountsaver.repository.ApiError;
import com.p4.accountsaver.repository.ApiEvent;
import com.p4.accountsaver.repository.BackendlessAPI;

public class SignUpViewModel extends AndroidViewModel {
    private final Context mContext;

    public final ObservableField<String> username = new ObservableField<>();
    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableField<String> password = new ObservableField<>();
    public final ObservableField<String> confirmPassword = new ObservableField<>();

    public final ObservableField<String> usernameError = new ObservableField<>();
    public final ObservableField<String> nameError = new ObservableField<>();
    public final ObservableField<String> passwordError = new ObservableField<>();
    public final ObservableField<String> confirmPasswordError = new ObservableField<>();

    private final MutableLiveData<ApiEvent> mSignUpEvent = new MutableLiveData<>();

    public SignUpViewModel(@NonNull Application application) {
        super(application);
        mContext = application.getApplicationContext();
    }

    public void signUp() {
        if (TextUtils.isEmpty(username.get())) {
            usernameError.set(null);
            usernameError.set(mContext.getString(R.string.username_empty_error));
        } else if (TextUtils.isEmpty(name.get())) {
            nameError.set(null);
            nameError.set(mContext.getString(R.string.name_empty_error));
        } else if (TextUtils.isEmpty(password.get())) {
            passwordError.set(null);
            passwordError.set(mContext.getString(R.string.password_empty_error));
        } else if (TextUtils.isEmpty(confirmPassword.get())) {
            confirmPasswordError.set(null);
            confirmPasswordError.set(mContext.getString(R.string.confirm_password_empty_error));
        } else if (!password.get().equals(confirmPassword.get())) {
            confirmPassword.set("");
            confirmPasswordError.set(null);
            confirmPasswordError.set(mContext.getString(R.string.different_password_error));
        } else {
            mSignUpEvent.setValue(new ApiEvent.Builder().inProgress(true).build());
            BackendlessAPI.getInstance().signUp(username.get(), name.get(), password.get(), new API.ApiListener<Boolean>() {
                @Override
                public void onSuccess(Boolean object) {
                    mSignUpEvent.setValue(new ApiEvent.Builder().success(true).build());
                }

                @Override
                public void onFailure(ApiError error) {
                    mSignUpEvent.setValue(new ApiEvent.Builder().setError(error).build());
                }
            });
        }
    }

    public LiveData<ApiEvent> getSignUpEvent() {
        return mSignUpEvent;
    }
}
