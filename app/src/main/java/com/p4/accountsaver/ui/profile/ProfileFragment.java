package com.p4.accountsaver.ui.profile;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.p4.accountsaver.R;
import com.p4.accountsaver.databinding.FragmentProfileBinding;
import com.p4.accountsaver.repository.ApiEvent;
import com.p4.accountsaver.ui.base.BaseFragment;
import com.p4.accountsaver.ui.login.LoginActivity;
import com.p4.accountsaver.utils.DialogUtils;

public class ProfileFragment extends BaseFragment {
    private FragmentProfileBinding mBinding;
    private ProgressDialog mProgressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentProfileBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ProfileViewModel viewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        mBinding.setViewmodel(viewModel);

        viewModel.getLogoutEvent().observe(this, (ApiEvent<Void> event) -> {
            if (event != null) {
                if (event.isInProgress()) {
                    getProgressDialog().show();
                } else {
                    getProgressDialog().dismiss();
                    if (event.isSuccess() && getActivity() != null) {
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                    } else if (event.getError() != null) {
                        DialogUtils.showErrorDialog(getActivity(), event.getError().getMessage());
                    }
                }
            }
        });

        viewModel.start();
    }

    private ProgressDialog getProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage(getString(R.string.logging_out));
            mProgressDialog.setCancelable(false);
        }
        return mProgressDialog;
    }
}
