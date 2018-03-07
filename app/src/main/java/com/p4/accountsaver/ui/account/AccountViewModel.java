package com.p4.accountsaver.ui.account;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.util.Log;

import com.p4.accountsaver.model.Account;
import com.p4.accountsaver.repository.API;
import com.p4.accountsaver.repository.ApiError;
import com.p4.accountsaver.repository.BackendlessAPI;
import com.p4.accountsaver.ui.account.adapters.AccountAdapter;

import java.util.List;

/**
 * Created by averychoke on 6/3/18.
 */

public class AccountViewModel extends AndroidViewModel implements AccountAdapter.AccountCell.AccountListener {
    private final String TAG = AccountViewModel.class.getSimpleName();
    public final ObservableList<Account> accounts = new ObservableArrayList<>();
    public final ObservableBoolean isFetching = new ObservableBoolean();

    private final MutableLiveData<Account> mViewDetailsEvent = new MutableLiveData<>();
    private final MutableLiveData<Account> mLockConfirmationEvent = new MutableLiveData<>();

    public AccountViewModel(@NonNull Application application) {
        super(application);
    }

    public void start() {
        fetchAccounts();
    }

    public void onRefresh() {
        fetchAccounts();
    }

    private void fetchAccounts() {
        isFetching.set(true);
        BackendlessAPI.getInstance().fetchAccounts(new API.ApiListener<List<Account>>() {
            @Override
            public void onSuccess(List<Account> apiAccounts) {
                isFetching.set(false);
                accounts.clear();
                accounts.addAll(apiAccounts);
            }

            @Override
            public void onFailure(ApiError error) {
                isFetching.set(false);
                Log.e(TAG, error.getMessage());
            }
        });
    }

    @Override
    public void onAccountSelected(Account account) {
        if (account.isLocked()) {
            mLockConfirmationEvent.setValue(account);
        } else {
            mViewDetailsEvent.setValue(account);
        }
    }

    // region get set
    public MutableLiveData<Account> getViewDetailsEvent() {
        return mViewDetailsEvent;
    }

    public MutableLiveData<Account> getLockConfirmationEvent() {
        return mLockConfirmationEvent;
    }
    // endregion
}