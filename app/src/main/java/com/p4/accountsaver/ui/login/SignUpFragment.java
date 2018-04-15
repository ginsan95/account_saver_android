package com.p4.accountsaver.ui.login;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.p4.accountsaver.R;
import com.p4.accountsaver.databinding.FragmentSignUpBinding;
import com.p4.accountsaver.repository.ApiEvent;
import com.p4.accountsaver.ui.base.BaseFragment;
import com.p4.accountsaver.utils.DialogUtils;

public class SignUpFragment extends BaseFragment {
    private FragmentSignUpBinding mBinding;
    private ProgressDialog mProgressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentSignUpBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SignUpViewModel viewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);
        mBinding.setViewmodel(viewModel);

        viewModel.getSignUpEvent().observe(this, (ApiEvent event) -> {
            if (event != null) {
                if (event.isInProgress()) {
                    getProgressDialog().show();
                } else {
                    getProgressDialog().dismiss();
                    if (event.isSuccess() && getActivity() != null) {
                        Toast.makeText(getActivity(), R.string.sign_up_success_message, Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                    } else if (event.getError() != null) {
                        DialogUtils.showErrorDialog(getActivity(), event.getError().getMessage());
                    }
                }
            }
        });
    }

    private ProgressDialog getProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage(getString(R.string.signing_up));
            mProgressDialog.setCancelable(false);
        }
        return mProgressDialog;
    }
}
