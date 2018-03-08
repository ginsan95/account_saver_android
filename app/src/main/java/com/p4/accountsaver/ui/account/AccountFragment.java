package com.p4.accountsaver.ui.account;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

        mAdapter = new AccountAdapter(viewModel);
        initRecyclerView(viewModel);

        viewModel.getViewDetailsEvent().observe(this, (Account account) -> {
            Log.d(AccountFragment.class.getSimpleName(), "view details " + account);
        });
        viewModel.getLockConfirmationEvent().observe(this, (Account account) -> {
            Log.d(AccountFragment.class.getSimpleName(), "open lock");
        });
        viewModel.start();
    }

    private void initRecyclerView(AccountViewModel viewModel) {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mBinding.recyclerView.setLayoutManager(layoutManager);
        mBinding.recyclerView.setNestedScrollingEnabled(false);
        mBinding.recyclerView.setAdapter(mAdapter);
        mBinding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0) {
                    viewModel.fetchNextPage();
                }
            }
        });
    }
}
