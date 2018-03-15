package com.p4.accountsaver.ui.account;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.databinding.ObservableBoolean;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.p4.accountsaver.R;
import com.p4.accountsaver.model.Account;

/**
 * Created by averychoke on 11/3/18.
 */

public class AccountDetailViewModel extends AndroidViewModel {
    public enum ViewType {
        VIEW, ADD, EDIT
    }

    private Context mContext;
    private Account mAccount;

    public final ObservableBoolean isEditMode = new ObservableBoolean(false);

    private final MutableLiveData<ViewType> mViewType = new MutableLiveData<>();
    private final MutableLiveData<String> mTitle = new MutableLiveData<>();

    public AccountDetailViewModel(@NonNull Application application) {
        super(application);
        mContext = application.getApplicationContext();
    }

    public void start(@Nullable Account account) {
        if (account != null) {
            mAccount = account;
            mTitle.setValue(account.getGameName());
            setViewType(ViewType.VIEW);
        } else {
            mAccount = new Account();
            mTitle.setValue(mContext.getString(R.string.new_account_title));
            setViewType(ViewType.ADD);
        }
        isEditMode.set(account == null);
    }

    public void editSaveData() {
        switch (mViewType.getValue()) {
            case VIEW:
                setViewType(ViewType.EDIT);
                break;
            case ADD:
                break;
            case EDIT:
                break;
        }
    }

    public void selectImage() {
        Log.d("selectImage", "selectImage");
    }

    // region Get Set
    public Account getAccount() {
        return mAccount;
    }

    public LiveData<ViewType> getViewType() {
        return mViewType;
    }

    private void setViewType(ViewType viewType) {
        mViewType.setValue(viewType);
        switch (viewType) {
            case VIEW:
                isEditMode.set(false);
                break;
            case ADD:
                isEditMode.set(true);
                break;
            case EDIT:
                isEditMode.set(true);
                break;
        }
    }

    public LiveData<String> getTitle() {
        return mTitle;
    }
    // endregion
}
