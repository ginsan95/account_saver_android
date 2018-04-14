package com.p4.accountsaver.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Parcel;
import android.os.Parcelable;

import com.android.databinding.library.baseAdapters.BR;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SecurityQuestions extends BaseObservable implements Parcelable {
    private Map<String, String> mQuestions;
    private List<SecurityQuestion> mQuestionList;

    public SecurityQuestions() {
        mQuestions = new LinkedHashMap<>();
        mQuestionList = new ArrayList<>();
    }

    public Map<String, String> getQuestions() {
        return mQuestions;
    }

    public void setQuestions(Map<String, String> questions) {
        mQuestions = questions;
        refreshList();
    }

    @Bindable
    public List<SecurityQuestion> getQuestionList() {
        return mQuestionList;
    }

    public void addQuestion(SecurityQuestion question) {
        mQuestions.put(question.getQuestion(), question.getAnswer());
        refreshList();
    }

    public void removeQuestion(SecurityQuestion question) {
        String value = mQuestions.remove(question.getQuestion());
        if (value != null) {
            refreshList();
        }
    }

    private void refreshList() {
        mQuestionList.clear();
        for (Map.Entry<String, String> entrySet : mQuestions.entrySet()) {
            mQuestionList.add(new SecurityQuestion(entrySet.getKey(), entrySet.getValue()));
        }
        notifyPropertyChanged(BR.questionList);
    }

    // region Parcelable
    protected SecurityQuestions(Parcel in) {
        this();
        int size = in.readInt();
        for(int i = 0; i < size; i++){
            String key = in.readString();
            String value = in.readString();
            mQuestions.put(key,value);
        }
        mQuestionList = in.createTypedArrayList(SecurityQuestion.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mQuestions.size());
        for(Map.Entry<String,String> entry : mQuestions.entrySet()){
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue());
        }
        dest.writeTypedList(mQuestionList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SecurityQuestions> CREATOR = new Creator<SecurityQuestions>() {
        @Override
        public SecurityQuestions createFromParcel(Parcel in) {
            return new SecurityQuestions(in);
        }

        @Override
        public SecurityQuestions[] newArray(int size) {
            return new SecurityQuestions[size];
        }
    };
    // endregion
}
