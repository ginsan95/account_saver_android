package com.p4.accountsaver.ui.account;

import android.app.Activity;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
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
import com.p4.accountsaver.ui.account.dialog.LockDialog;
import com.p4.accountsaver.ui.base.BaseFragment;
import com.p4.accountsaver.utils.DialogUtils;

/**
 * Created by averychoke on 11/3/18.
 */

public class AccountDetailFragment extends BaseFragment implements LockDialog.LockDialogListener {
    private static final int ICON_REQUEST_CODE = 1;
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

        mViewModel.getChangeLockEvent().observe(this, (String title) -> {
            if (title != null && getActivity() != null && !getActivity().isFinishing()) {
                LockDialog.newInstance(title).show(getChildFragmentManager(), AccountFragment.class.getSimpleName());
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

        mViewModel.getBackPressedEvent().observe(this, (Boolean showConfirmDialog) -> {
            if (showConfirmDialog != null && showConfirmDialog && getActivity() != null && !getActivity().isDestroyed()) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.edit_dismiss_message)
                        .setPositiveButton(R.string.dialog_ok, (DialogInterface dialog, int which) -> {
                            if (mViewModel != null) {
                                mViewModel.dismissEdit();
                            }
                        })
                        .setNegativeButton(R.string.dialog_cancel, (DialogInterface dialog, int which) -> {

                        })
                        .show();
            } else {
                getActivity().finish();
            }
        });

        mViewModel.getSelectImageEvent().observe(this, (String url) -> {
            Intent intent = new Intent(getActivity(), IconsActivity.class);
            intent.putExtra(IconsActivity.URL_KEY, url);
            startActivityForResult(intent, ICON_REQUEST_CODE);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ICON_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK && mViewModel != null) {
                    String url = data.getStringExtra(IconsActivity.URL_KEY);
                    mViewModel.setAccountIcon(url);
                }
                break;
        }
    }

    @Override
    public void onLockPasswordPlaced(Account account, String password, String confirmPassword) {
        if (mViewModel != null) {
            String message = mViewModel.changeLockStatus(password, confirmPassword);
            if (message != null) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onBackPressed() {
        if (mViewModel != null) {
            mViewModel.onBackPressed();
        }
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
