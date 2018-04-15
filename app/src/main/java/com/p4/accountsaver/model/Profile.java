package com.p4.accountsaver.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by averychoke on 27/2/18.
 */

public class Profile {
    private String name;
    private String objectId;
    @SerializedName("user-token")
    private String userToken;

    // For Gson
    public Profile() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String ownerId) {
        this.objectId = ownerId;
    }

    public String getUserToken() {
        return userToken;
    }
}
