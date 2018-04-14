package com.p4.accountsaver.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SecurityQuestion implements Parcelable {
    private String mQuestion;
    private String mAnswer;

    public SecurityQuestion() {}

    public SecurityQuestion(String question, String answer) {
        mQuestion = question;
        mAnswer = answer;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public void setQuestion(String question) {
        mQuestion = question;
    }

    public String getAnswer() {
        return mAnswer;
    }

    public void setAnswer(String answer) {
        mAnswer = answer;
    }

    // region Parcelable
    protected SecurityQuestion(Parcel in) {
        mQuestion = in.readString();
        mAnswer = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mQuestion);
        dest.writeString(mAnswer);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SecurityQuestion> CREATOR = new Creator<SecurityQuestion>() {
        @Override
        public SecurityQuestion createFromParcel(Parcel in) {
            return new SecurityQuestion(in);
        }

        @Override
        public SecurityQuestion[] newArray(int size) {
            return new SecurityQuestion[size];
        }
    };
    // endregion
}
