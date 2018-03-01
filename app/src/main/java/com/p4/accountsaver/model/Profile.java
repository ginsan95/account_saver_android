package com.p4.accountsaver.model;

/**
 * Created by averychoke on 27/2/18.
 */

public class Profile {
    private String mName;
    private String mOwnerId;

    // For Gson
    public Profile() {

    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getOwnerId() {
        return mOwnerId;
    }

    public void setOwnerId(String ownerId) {
        mOwnerId = ownerId;
    }
}
