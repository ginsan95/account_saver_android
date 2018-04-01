package com.p4.accountsaver.ui.account;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.p4.accountsaver.R;
import com.p4.accountsaver.model.Account;
import com.p4.accountsaver.repository.API;
import com.p4.accountsaver.repository.ApiError;
import com.p4.accountsaver.repository.ApiEvent;
import com.p4.accountsaver.repository.BackendlessAPI;

/**
 * Created by averychoke on 11/3/18.
 */

public class AccountDetailViewModel extends AndroidViewModel {
    public enum ViewType {
        VIEW, ADD, EDIT
    }

    private Context mContext;
    private Account mPrevAccount;

    public final ObservableField<Account> account = new ObservableField<>();
    public final ObservableBoolean isEditMode = new ObservableBoolean(false);
    public final ObservableField<String> gameNameError = new ObservableField<>();
    public final ObservableField<String> usernameError = new ObservableField<>();
    public final ObservableField<String> passwordError = new ObservableField<>();

    private final MutableLiveData<ViewType> mViewType = new MutableLiveData<>();
    private final MutableLiveData<String> mTitle = new MutableLiveData<>();
    private final MutableLiveData<ApiEvent<Account>> mSaveEditEvent = new MutableLiveData<>();
    private final MutableLiveData<String> mChangeLockEvent = new MutableLiveData<>();
    private final MutableLiveData<Boolean> mBackPressedEvent = new MutableLiveData<>();

    public AccountDetailViewModel(@NonNull Application application) {
        super(application);
        mContext = application.getApplicationContext();
    }

    public void start(@Nullable Account account) {
        if (account != null) {
            this.account.set(account);
            mTitle.setValue(account.getGameName());
            setViewType(ViewType.VIEW);
        } else {
            this.account.set(new Account());
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
                saveAccount();
                break;
            case EDIT:
                updateAccount();
                break;
        }
    }

    public void selectImage() {
        Log.d("selectImage", "selectImage");
    }

    public void showLockDialog() {
        if (isEditMode.get()) {
            mChangeLockEvent.setValue(account.get().isLocked() ?
                    mContext.getString(R.string.dialog_lock_unloack_title) : mContext.getString(R.string.dialog_lock_add_title));
        }
    }

    public String changeLockStatus(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            return mContext.getString(R.string.password_diff_message);
        } else if (account.get().isLocked() && account.get().getLockPassword() != null && !password.equals(account.get().getLockPassword())) {
            return mContext.getString(R.string.incorrect_lock_password);
        } else {
            account.get().setLocked(!account.get().isLocked());
            if (account.get().isLocked()) {
                account.get().setLockPassword(password);
            }
            account.notifyChange();
            return null;
        }
    }

    private boolean checkCompulsaryData() {
        if (TextUtils.isEmpty(account.get().getGameName())) {
            gameNameError.set(mContext.getString(R.string.game_name_empty_error));
            return false;
        } else if (TextUtils.isEmpty(account.get().getUsername())) {
            usernameError.set(mContext.getString(R.string.username_empty_error));
            return false;
        } else if (TextUtils.isEmpty(account.get().getPassword())) {
            passwordError.set(mContext.getString(R.string.password_empty_error));
            return false;
        } else {
            return true;
        }
    }

    public void onBackPressed() {
        mBackPressedEvent.setValue(getViewType().getValue() == ViewType.EDIT);
    }

    public void dismissEdit() {
        setViewType(ViewType.VIEW);
    }

    private void saveAccount() {
        if (account.get() != null && checkCompulsaryData()) {
            mSaveEditEvent.setValue(new ApiEvent.Builder().inProgress(true).build());
            BackendlessAPI.getInstance().saveAccount(account.get(), new API.ApiListener<Account>() {
                @Override
                public void onSuccess(Account account) {
                    mSaveEditEvent.setValue(new ApiEvent.Builder().success(true).setResultData(account).build());
                }

                @Override
                public void onFailure(ApiError error) {
                    mSaveEditEvent.setValue(new ApiEvent.Builder().setError(error).build());
                }
            });
        }
    }

    private void updateAccount() {
        if (account.get() != null && checkCompulsaryData()) {
            mSaveEditEvent.setValue(new ApiEvent.Builder().inProgress(true).build());
            BackendlessAPI.getInstance().updateAccount(account.get(), new API.ApiListener<Account>() {
                @Override
                public void onSuccess(Account account) {
                    mSaveEditEvent.setValue(new ApiEvent.Builder().success(true).setResultData(account).build());
                }

                @Override
                public void onFailure(ApiError error) {
                    mSaveEditEvent.setValue(new ApiEvent.Builder().setError(error).build());
                }
            });
        }
    }

    // region Get Set
    public LiveData<ViewType> getViewType() {
        return mViewType;
    }

    private void setViewType(ViewType viewType) {
        mViewType.setValue(viewType);
        switch (viewType) {
            case VIEW:
                if (mPrevAccount != null) {
                    account.set(mPrevAccount);
                }
                isEditMode.set(false);
                break;
            case ADD:
                isEditMode.set(true);
                break;
            case EDIT:
                mPrevAccount = account.get();
                account.set(account.get().getClone());
                isEditMode.set(true);
                break;
        }
    }

    public LiveData<String> getTitle() {
        return mTitle;
    }

    public LiveData<ApiEvent<Account>> getSaveEditEvent() {
        return mSaveEditEvent;
    }

    public LiveData<String> getChangeLockEvent() {
        return mChangeLockEvent;
    }

    public LiveData<Boolean> getBackPressedEvent() {
        return mBackPressedEvent;
    }
    // endregion
}
