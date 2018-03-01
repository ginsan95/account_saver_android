package com.p4.accountsaver.binding;

import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.widget.EditText;

/**
 * Created by averychoke on 26/2/18.
 */

public class EditTextBinding {

    @BindingAdapter("app:error")
    public static void setError(EditText editText, String errorMessage) {
        if (errorMessage != null && !TextUtils.isEmpty(errorMessage)) {
            editText.setError(errorMessage);
        }
    }

}
