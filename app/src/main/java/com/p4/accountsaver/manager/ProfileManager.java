package com.p4.accountsaver.manager;

import com.p4.accountsaver.model.Profile;
import com.p4.accountsaver.repository.API;
import com.p4.accountsaver.repository.ApiError;
import com.p4.accountsaver.repository.BackendlessAPI;

/**
 * Created by averychoke on 4/3/18.
 */

public class ProfileManager {
    private static final ProfileManager sInstance = new ProfileManager();
    private Profile mProfile;

    private ProfileManager() {}

    public static ProfileManager getInstance() {
        return sInstance;
    }

    public void login(String username, String password, API.ApiListener<Profile> listener) {
        BackendlessAPI.getInstance().login(username, password, new API.ApiListener<Profile>() {
            @Override
            public void onSuccess(Profile profile) {
                if (profile != null) {
                    mProfile = profile;
                    listener.onSuccess(profile);
                } else {
                    listener.onFailure(null);
                }
            }

            @Override
            public void onFailure(ApiError error) {
                listener.onFailure(error);
            }
        });
    }

    // region get set
    public Profile getProfile() {
        return mProfile;
    }

    public void setProfile(Profile profile) {
        mProfile = profile;
    }
    // endregion
}
