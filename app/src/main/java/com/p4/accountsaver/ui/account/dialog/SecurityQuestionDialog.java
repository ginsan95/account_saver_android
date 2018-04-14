package com.p4.accountsaver.ui.account.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.p4.accountsaver.R;
import com.p4.accountsaver.databinding.DialogSecurityQuestionBinding;
import com.p4.accountsaver.model.SecurityQuestion;

public class SecurityQuestionDialog extends DialogFragment {
    public interface DialogListener {
        void onSecurityQuestionAdded(SecurityQuestion securityQuestion);
        void onEmptyQuestion();
    }

    private DialogSecurityQuestionBinding mBinding;
    private DialogListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() != null && getActivity() instanceof DialogListener) {
            mListener = ((DialogListener) getActivity());
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final SecurityQuestion securityQuestion = new SecurityQuestion();

        mBinding = DialogSecurityQuestionBinding.inflate(getActivity().getLayoutInflater());
        mBinding.setSecurityQuestion(securityQuestion);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(mBinding.getRoot())
                .setPositiveButton(R.string.dialog_ok, (DialogInterface dialog, int id) -> {
                    if (mListener != null) {
                        if (TextUtils.isEmpty(securityQuestion.getQuestion())) {
                            mListener.onEmptyQuestion();
                        } else {
                            mListener.onSecurityQuestionAdded(securityQuestion);
                        }
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, (DialogInterface dialog, int id) -> {

                });
        return builder.create();
    }
}
