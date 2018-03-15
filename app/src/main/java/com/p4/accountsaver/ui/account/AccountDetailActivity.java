package com.p4.accountsaver.ui.account;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.p4.accountsaver.databinding.ActivityAccountDetailBinding;
import com.p4.accountsaver.model.Account;
import com.p4.accountsaver.ui.base.BaseActivity;

/**
 * Created by averychoke on 11/3/18.
 */

public class AccountDetailActivity extends BaseActivity {
    public static final String ACCOUNT_EXTRA = "ACCOUNT_EXTRA";
    private ActivityAccountDetailBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityAccountDetailBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Account account = getIntent().getParcelableExtra(ACCOUNT_EXTRA);
        getSupportFragmentManager()
                .beginTransaction()
                .add(mBinding.containerLayout.getId(), AccountDetailFragment.newInstance(account))
                .commit();
    }
}
