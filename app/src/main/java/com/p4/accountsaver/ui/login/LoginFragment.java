package com.p4.accountsaver.ui.login;

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
import com.p4.accountsaver.databinding.FragmentLoginBinding;
import com.p4.accountsaver.repository.ApiEvent;
import com.p4.accountsaver.ui.MainActivity;
import com.p4.accountsaver.ui.base.BaseFragment;
import com.p4.accountsaver.utils.DialogUtils;

public class LoginFragment extends BaseFragment {
    private FragmentLoginBinding mBinding;
    private ProgressDialog mProgressDialog;
    private Object mSignUpObject; //Prevent sign up event get called again and again when pop back stack

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentLoginBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LoginViewModel viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        mBinding.setViewmodel(viewModel);

        viewModel.getLoginEvent().observe(this, (ApiEvent event) -> {
            if (event != null) {
                if (event.isInProgress()) {
                    getProgressDialog().show();
                } else {
                    getProgressDialog().dismiss();
                    if (event.isSuccess() && getActivity() != null) {
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        getActivity().finish();
                    } else if (event.getError() != null) {
                        DialogUtils.showErrorDialog(getActivity(), event.getError().getMessage());
                    }
                }
            }
        });

        viewModel.getSignUpEvent().observe(this, (Object object) -> {
            if (getBaseActivity() != null && (mSignUpObject == null || !mSignUpObject.equals(object))) {
                mSignUpObject = object;
                getBaseActivity().changeFragment(new SignUpFragment());
            }
        });
    }

    private ProgressDialog getProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage(getString(R.string.logging_in));
            mProgressDialog.setCancelable(false);
        }
        return mProgressDialog;
    }
}
