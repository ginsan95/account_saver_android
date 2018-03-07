package com.p4.accountsaver.manager;

import com.p4.accountsaver.model.Profile;

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

    // region get set
    public Profile getProfile() {
        return mProfile;
    }

    public void setProfile(Profile profile) {
        mProfile = profile;
    }
    // endregion
}
