package com.p4.accountsaver.ui.account;

import android.app.Activity;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.p4.accountsaver.R;
import com.p4.accountsaver.databinding.FragmentAccountDetailBinding;
import com.p4.accountsaver.model.Account;
import com.p4.accountsaver.repository.ApiEvent;
import com.p4.accountsaver.ui.base.BaseFragment;
import com.p4.accountsaver.utils.DialogUtils;

/**
 * Created by averychoke on 11/3/18.
 */

public class AccountDetailFragment extends BaseFragment {
    private FragmentAccountDetailBinding mBinding;
    private AccountDetailViewModel mViewModel;
    private ProgressDialog mProgressDialog;

    public static AccountDetailFragment newInstance(Account account) {
        Bundle args = new Bundle();
        args.putParcelable(AccountDetailActivity.ACCOUNT_EXTRA, account);

        AccountDetailFragment fragment = new AccountDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentAccountDetailBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(AccountDetailViewModel.class);
        mBinding.setViewmodel(mViewModel);

        mViewModel.getTitle().observe(this, (String title) -> {
            if (getActivity() != null) {
                getActivity().setTitle(title);
            }
        });

        mViewModel.getViewType().observe(this, (AccountDetailViewModel.ViewType viewType) -> {
            if (viewType != null && getActivity() != null) {
                getActivity().invalidateOptionsMenu();
            }
        });

        mViewModel.getSaveEditEvent().observe(this, (ApiEvent<Account> event) -> {
            if (event != null) {
                if (event.isInProgress()) {
                    getProgressDialog().show();
                } else {
                    getProgressDialog().dismiss();
                    if (event.isSuccess() && event.getResultData() != null && getActivity() != null) {
                        getActivity().setResult(Activity.RESULT_OK);
                        getActivity().finish();
                    } else if (event.getError() != null) {
                        DialogUtils.showErrorDialog(getActivity(), event.getError().getMessage());
                    }
                }
            }
        });

        Account account = getArguments().getParcelable(AccountDetailActivity.ACCOUNT_EXTRA);
        mViewModel.start(account);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mViewModel != null && mViewModel.getViewType().getValue() != null) {
            switch (mViewModel.getViewType().getValue()) {
                case VIEW:
                    menu.findItem(R.id.action_save_edit).setIcon(R.drawable.ic_edit);
                    break;
                case EDIT:
                    Toast.makeText(getActivity(), R.string.edit_mode, Toast.LENGTH_SHORT).show();
                case ADD:
                    menu.findItem(R.id.action_save_edit).setIcon(R.drawable.ic_save);
                    break;
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.account_detail_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save_edit:
                mViewModel.editSaveData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private ProgressDialog getProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setCancelable(false);
        }
        return mProgressDialog;
    }
}
