package com.p4.accountsaver.ui.login;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        getSupportFragmentManager()
                .beginTransaction()
                .add(mBinding.containerLayout.getId(), new LoginFragment(), LoginFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public void changeFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(mBinding.containerLayout.getId(), fragment, fragment.getClass().getSimpleName())
                .addToBackStack(fragment.getClass().getSimpleName())
                .commit();
    }
}
