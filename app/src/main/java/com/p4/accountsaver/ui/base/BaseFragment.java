package com.p4.accountsaver.ui.base;

import android.support.v4.app.Fragment;

/**
 * Created by averychoke on 25/2/18.
 */

public class BaseFragment extends Fragment {
    public BaseActivity getBaseActivity() {
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            return (BaseActivity) getActivity();
        } else {
            return null;
        }
    }
}
