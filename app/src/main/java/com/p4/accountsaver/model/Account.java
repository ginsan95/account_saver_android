package com.p4.accountsaver.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by averychoke on 3/3/18.
 */

public class Account implements Parcelable {
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
    @SerializedName("security_questions")
    private SecurityQuestions securityQuestions;

    public Account() {

    }

    public Account getClone() {
        Account account = new Account();
        account.objectId = objectId;
        account.gameName = gameName;
        account.username = username;
        account.password = password;
        account.isLocked = isLocked;
        account.created = created;
        account.updated = updated;
        account.gameIconUrl = gameIconUrl;
        account.password2 = password2;
        account.email = email;
        account.phoneNumber = phoneNumber;
        account.description = description;
        account.lockPassword = lockPassword;
        account.securityQuestions = securityQuestions;
        return account;
    }

    public String getRecentDateString() {
        Date recentDate = updated == null ? created : updated;
        if (recentDate != null) {
            SimpleDateFormat df = new SimpleDateFormat("d/m/yyyy hh:mm a");
            return df.format(recentDate);
        }
        return null;
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

    public SecurityQuestions getSecurityQuestions() {
        return securityQuestions;
    }

    public void setSecurityQuestions(SecurityQuestions securityQuestions) {
        this.securityQuestions = securityQuestions;
    }
    // endregion

    // region Parcelable
    protected Account(Parcel in) {
        objectId = in.readString();
        gameName = in.readString();
        username = in.readString();
        password = in.readString();
        isLocked = in.readByte() != 0;
        created = new Date(in.readLong());
        if (in.readByte() != 0) {
            updated = new Date(in.readLong());
        }
        gameIconUrl = in.readString();
        password2 = in.readString();
        email = in.readString();
        phoneNumber = in.readString();
        description = in.readString();
        lockPassword = in.readString();
        securityQuestions = in.readParcelable(SecurityQuestions.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(objectId);
        dest.writeString(gameName);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeByte((byte) (isLocked ? 1 : 0));
        dest.writeLong(created.getTime());
        dest.writeByte((byte) (updated != null ? 1 : 0));
        if (updated != null) {
            dest.writeLong(updated.getTime());
        }
        dest.writeString(gameIconUrl);
        dest.writeString(password2);
        dest.writeString(email);
        dest.writeString(phoneNumber);
        dest.writeString(description);
        dest.writeString(lockPassword);
        dest.writeParcelable(securityQuestions, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Account> CREATOR = new Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel in) {
            return new Account(in);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };
    // endregion
}
