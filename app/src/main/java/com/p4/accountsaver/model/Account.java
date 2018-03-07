package com.p4.accountsaver.model;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by averychoke on 3/3/18.
 */

public class Account {
    // compulsary
    private String objectId;
    @SerializedName("game_name")
    private String gameName;
    private String username;
    private String password;
    @SerializedName("is_locked")
    private boolean isLocked;
    private Date created;

    // optional
    private Date updated;
    @SerializedName("game_icon")
    private String gameIconUrl;
    private String password2;
    private String email;
    @SerializedName("phone_number")
    private String phoneNumber;
    private String description;
    @SerializedName("lock_password")
    private String lockPassword;
    private String securityQuestions;

    public Account() {

    }

    public Date getRecentDate() {
        return updated == null ? created : updated;
    }

    // region Get Set
    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getGameIconUrl() {
        return gameIconUrl;
    }

    public void setGameIconUrl(String gameIconUrl) {
        this.gameIconUrl = gameIconUrl;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLockPassword() {
        return lockPassword;
    }

    public void setLockPassword(String lockPassword) {
        this.lockPassword = lockPassword;
    }
    // endregion
}
