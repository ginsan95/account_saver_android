package com.p4.accountsaver.ui.account;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;
import android.text.TextUtils;
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
    public final ObservableBoolean isRefreshing = new ObservableBoolean();
    public final ObservableBoolean isPaginating = new ObservableBoolean();

    private final MutableLiveData<Account> mViewDetailsEvent = new MutableLiveData<>();
    private final MutableLiveData<Account> mLockConfirmationEvent = new MutableLiveData<>();

    private boolean mIsFetching = false;
    private String mSearchTerm = "";

    public AccountViewModel(@NonNull Application application) {
        super(application);
    }

    public void start() {
        onRefresh();
    }

    public void onRefresh() {
        mIsFetching = false;
        fetchAccounts(0);
    }

    public void fetchNextPage() {
        if (!mIsFetching) {
            fetchAccounts(accounts.size());
        }
    }

    public void searchAccounts(String searchTerm) {
        mSearchTerm = searchTerm;
        mIsFetching = false;
        fetchAccounts(0);
    }

    public void dismissSearch() {
        if (!TextUtils.isEmpty(mSearchTerm)) {
            mSearchTerm = "";
            onRefresh();
        }
    }

    private void fetchAccounts(int offset) {
        mIsFetching = true;
        if (offset == 0) {
            isRefreshing.set(true);
        } else {
            isPaginating.set(true);
        }

        BackendlessAPI.getInstance().fetchAccounts(offset, mSearchTerm, new API.ApiListener<List<Account>>() {
            @Override
            public void onSuccess(List<Account> apiAccounts) {
                if (offset == 0) {
                    accounts.clear();
                    accounts.addAll(apiAccounts);
                } else { // Pagination
                    accounts.addAll(apiAccounts);
                }

                // determine if still need to fetch
                mIsFetching = apiAccounts.size() < BackendlessAPI.PAGE_SIZE || apiAccounts.isEmpty();

                // dismiss ui
                isRefreshing.set(false);
                isPaginating.set(false);
            }

            @Override
            public void onFailure(ApiError error) {
                mIsFetching = false;
                // dismiss ui
                isRefreshing.set(false);
                isPaginating.set(false);
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
