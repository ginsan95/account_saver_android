package com.p4.accountsaver.ui.login;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.p4.accountsaver.ui.MainActivity;
import com.p4.accountsaver.R;
import com.p4.accountsaver.databinding.ActivityLoginBinding;
import com.p4.accountsaver.repository.ApiEvent;
import com.p4.accountsaver.ui.base.BaseActivity;
import com.p4.accountsaver.utils.DialogUtils;

/**
 * Created by averychoke on 25/2/18.
 */

public class LoginActivity extends BaseActivity {

    private ActivityLoginBinding mBinding;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        LoginViewModel viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        mBinding.setViewmodel(viewModel);

        viewModel.getLoginEvent().observe(this, (ApiEvent event) -> {
            if (event != null) {
                if (event.isInProgress()) {
                    getProgressDialog().show();
                } else {
                    getProgressDialog().dismiss();
                    if (event.isSuccess()) {
                        startActivity(new Intent(this, MainActivity.class));
                    } else if (event.getError() != null) {
                        DialogUtils.showErrorDialog(this, event.getError().getMessage());
                    }
                }
            }
        });
    }

    private ProgressDialog getProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.logging_in));
            mProgressDialog.setCancelable(false);
        }
        return mProgressDialog;
    }
}
