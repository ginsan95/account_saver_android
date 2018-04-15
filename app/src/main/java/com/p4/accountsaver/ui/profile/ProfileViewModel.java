package com.p4.accountsaver.ui.profile;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableField;

import com.p4.accountsaver.manager.ProfileManager;
import com.p4.accountsaver.model.Profile;
import com.p4.accountsaver.repository.API;
import com.p4.accountsaver.repository.ApiError;
import com.p4.accountsaver.repository.ApiEvent;

public class ProfileViewModel extends ViewModel {
    public ObservableField<String> name = new ObservableField<>();

    public MutableLiveData<ApiEvent<Void>> mLogoutEvent = new MutableLiveData<>();

    public void start() {
        Profile profile = ProfileManager.getInstance().getProfile();
        if (profile != null) {
            name.set(profile.getName());
        }
    }

    public void logout() {
        mLogoutEvent.setValue(new ApiEvent.Builder().inProgress(true).build());
        ProfileManager.getInstance().logout(new API.ApiListener<Boolean>() {
            @Override
            public void onSuccess(Boolean success) {
                mLogoutEvent.setValue(new ApiEvent.Builder().success(true).build());
            }

            @Override
            public void onFailure(ApiError error) {
                mLogoutEvent.setValue(new ApiEvent.Builder().setError(error).build());
            }
        });
    }

    // region Get Set
    public LiveData<ApiEvent<Void>> getLogoutEvent() {
        return mLogoutEvent;
    }
    // endregion
}
