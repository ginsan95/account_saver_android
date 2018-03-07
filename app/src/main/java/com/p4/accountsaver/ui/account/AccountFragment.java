package com.p4.accountsaver.ui.account;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.p4.accountsaver.databinding.FragmentAccountBinding;
import com.p4.accountsaver.model.Account;
import com.p4.accountsaver.ui.account.adapters.AccountAdapter;
import com.p4.accountsaver.ui.base.BaseFragment;

import java.util.List;

/**
 * Created by averychoke on 5/3/18.
 */

public class AccountFragment extends BaseFragment {
    private FragmentAccountBinding mBinding;
    private AccountAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentAccountBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AccountViewModel viewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
        mBinding.setViewmodel(viewModel);

        // recycler view
        mAdapter = new AccountAdapter(viewModel);
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mBinding.recyclerView.setNestedScrollingEnabled(false);
        mBinding.recyclerView.setAdapter(mAdapter);

        viewModel.getViewDetailsEvent().observe(this, (Account account) -> {
            Log.d(AccountFragment.class.getSimpleName(), "view details " + account);
        });
        viewModel.getLockConfirmationEvent().observe(this, (Account account) -> {
            Log.d(AccountFragment.class.getSimpleName(), "open lock");
        });
        viewModel.start();
    }
}
