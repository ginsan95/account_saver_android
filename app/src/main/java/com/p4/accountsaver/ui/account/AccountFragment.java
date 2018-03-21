package com.p4.accountsaver.ui.account;

import android.app.Activity;
import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.p4.accountsaver.R;
import com.p4.accountsaver.databinding.FragmentAccountBinding;
import com.p4.accountsaver.model.Account;
import com.p4.accountsaver.ui.account.adapters.AccountAdapter;
import com.p4.accountsaver.ui.account.dialog.LockDialog;
import com.p4.accountsaver.ui.base.BaseFragment;

/**
 * Created by averychoke on 5/3/18.
 */

public class AccountFragment extends BaseFragment implements LockDialog.LockDialogListener {
    private static final int VIEW_REQUEST_CODE = 1;
    private FragmentAccountBinding mBinding;
    private AccountViewModel mViewModel;
    private AccountAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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

        mViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
        mBinding.setViewmodel(mViewModel);

        mAdapter = new AccountAdapter(mViewModel);
        initRecyclerView();

        mViewModel.getAddAccountEvent().observe(this, (Void nth) -> {
            startActivityForResult(new Intent(getActivity(), AccountDetailActivity.class), VIEW_REQUEST_CODE);
        });

        mViewModel.getViewDetailsEvent().observe(this, (Account account) -> {
            Intent intent = new Intent(getActivity(), AccountDetailActivity.class);
            intent.putExtra(AccountDetailActivity.ACCOUNT_EXTRA, account);
            startActivityForResult(intent, VIEW_REQUEST_CODE);
        });

        mViewModel.getLockConfirmationEvent().observe(this, (Account account) -> {
            if (getActivity() != null && !getActivity().isFinishing()) {
                LockDialog.newInstance(account).show(getChildFragmentManager(), AccountFragment.class.getSimpleName());
            }
        });

        mViewModel.start();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.account_menu, menu);

        // Setup search
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setIconified(false);
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null && imm.isAcceptingText() && getActivity().getCurrentFocus() != null) {
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                }

                if (mViewModel != null) {
                    mViewModel.searchAccounts(s);
                    return true;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        MenuItemCompat.setOnActionExpandListener(menu.findItem(R.id.action_search), new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                if (mViewModel != null) {
                    mViewModel.dismissSearch();
                }
                return true;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case VIEW_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK && mViewModel != null) {
                    mViewModel.start();
                }
                break;
        }
    }

    private void initRecyclerView() {
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
                    if (mViewModel != null) {
                        mViewModel.fetchNextPage();
                    }
                }
            }
        });
    }

    @Override
    public void onLockPasswordPlaced(Account account, String password, String confirmPassword) {
        if (mViewModel != null) {
            String message = mViewModel.checkAccountLockPassword(account, password, confirmPassword);
            if (message == null) {
                Intent intent = new Intent(getActivity(), AccountDetailActivity.class);
                intent.putExtra(AccountDetailActivity.ACCOUNT_EXTRA, account);
                startActivityForResult(intent, VIEW_REQUEST_CODE);
            } else {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
