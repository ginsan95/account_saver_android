package com.p4.accountsaver.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.p4.accountsaver.R;

/**
 * Created by averychoke on 28/2/18.
 */

public class DialogUtils {
    public static void showErrorDialog(Activity activity, String message) {
        if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
            new AlertDialog.Builder(activity)
                    .setMessage(message)
                    .setPositiveButton(R.string.dialog_ok, (DialogInterface dialog, int which) -> {})
                    .show();
        }
    }
}
