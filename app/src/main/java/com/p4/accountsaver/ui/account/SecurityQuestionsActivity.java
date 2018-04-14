package com.p4.accountsaver.ui.account;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import com.p4.accountsaver.R;
import com.p4.accountsaver.databinding.ActivitySecurityQuestionsBinding;
import com.p4.accountsaver.model.SecurityQuestion;
import com.p4.accountsaver.model.SecurityQuestions;
import com.p4.accountsaver.ui.account.adapters.SecurityQuestionAdapter;
import com.p4.accountsaver.ui.account.dialog.SecurityQuestionDialog;
import com.p4.accountsaver.ui.base.BaseActivity;

public class SecurityQuestionsActivity extends BaseActivity implements SecurityQuestionDialog.DialogListener {
    public static final String SECURITY_QUESTIONS_KEY = "SECURITY_QUESTIONS_KEY";
    public static final String CAN_EDIT_KEY = "CAN_EDIT_KEY";

    private ActivitySecurityQuestionsBinding mBinding;
    private SecurityQuestionsViewModel mViewModel;
    private SecurityQuestionAdapter mAdapter;
    private SecurityQuestions mSecurityQuestions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivitySecurityQuestionsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setTitle(R.string.security_questions);

        mViewModel = ViewModelProviders.of(this).get(SecurityQuestionsViewModel.class);
        mBinding.setViewmodel(mViewModel);

        mAdapter = new SecurityQuestionAdapter(mViewModel);
        initRecyclerView();

        mViewModel.getShowAddDialogEvent().observe(this, (Void mVoid) -> {
            new SecurityQuestionDialog().show(getSupportFragmentManager(), SecurityQuestionDialog.class.getSimpleName());
        });

        mSecurityQuestions = getIntent().getParcelableExtra(SECURITY_QUESTIONS_KEY);
        if (mSecurityQuestions == null) {
            mSecurityQuestions = new SecurityQuestions();
        }
        mViewModel.start(mSecurityQuestions, getIntent().getBooleanExtra(CAN_EDIT_KEY, false));
    }

    private void initRecyclerView() {
        mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerView.setNestedScrollingEnabled(false);
        mBinding.recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(SECURITY_QUESTIONS_KEY, mSecurityQuestions);
        setResult(Activity.RESULT_OK, intent);
        super.onBackPressed();
    }

    @Override
    public void onSecurityQuestionAdded(SecurityQuestion securityQuestion) {
        if (mViewModel != null) {
            mViewModel.addSecurityQuestion(securityQuestion);
        }
    }

    @Override
    public void onEmptyQuestion() {
        Toast.makeText(this, R.string.question_empty_error, Toast.LENGTH_SHORT).show();
    }
}
