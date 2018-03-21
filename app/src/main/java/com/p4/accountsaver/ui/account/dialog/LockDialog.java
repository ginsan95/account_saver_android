package com.p4.accountsaver.ui.account.dialog;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.p4.accountsaver.R;
import com.p4.accountsaver.databinding.DialogLockBinding;
import com.p4.accountsaver.model.Account;

/**
 * Created by averychoke on 18/3/18.
 */

public class LockDialog extends DialogFragment {
    public interface LockDialogListener {
        void onLockPasswordPlaced(Account account, String password, String confirmPassword);
    }

    private static final String ACCOUNT_KEY = "ACCOUNT_KEY";
    private static final String TITLE_KEY = "TITLE_KEY";
    private DialogLockBinding mBinding;
    private LockDialogListener mListener;

    public static LockDialog newInstance(Account account) {
        Bundle args = new Bundle();
        args.putParcelable(ACCOUNT_KEY, account);

        LockDialog fragment = new LockDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public static LockDialog newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(TITLE_KEY, title);

        LockDialog fragment = new LockDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getParentFragment() != null && getParentFragment() instanceof LockDialogListener) {
            mListener = ((LockDialogListener) getParentFragment());
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mBinding = DialogLockBinding.inflate(getActivity().getLayoutInflater());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        if (getArguments().containsKey(TITLE_KEY)) {
            builder.setTitle(getArguments().getString(TITLE_KEY));
        }

        builder.setView(mBinding.getRoot())
                .setPositiveButton(R.string.dialog_ok, (DialogInterface dialog, int id) -> {
                    Account account = getArguments().getParcelable(ACCOUNT_KEY);
                    if (mListener != null) {
                        mListener.onLockPasswordPlaced(
                                account,
                                mBinding.passwordEditText.getText().toString(),
                                mBinding.confirmPasswordEditText.getText().toString());
                    }
                })
                .setNegativeButton(R.string.dialog_cancel, (DialogInterface dialog, int id) -> {

                });

        return builder.create();
    }
}
