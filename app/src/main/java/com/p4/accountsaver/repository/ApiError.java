package com.p4.accountsaver.repository;

import android.support.annotation.Nullable;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.BufferedSource;

/**
 * Created by averychoke on 27/2/18.
 */

public class ApiError {
    private ResponseBody mErrorBody;
    private String mErrorBodyMessage;
    private Throwable mThrowable;

    public ApiError(ResponseBody errorBody) {
        mErrorBody = errorBody;
        try {
            mErrorBodyMessage = errorBody.string();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ApiError(Throwable throwable) {
        mThrowable = throwable;
    }

    @Nullable
    public String getMessage() {
        if (mErrorBodyMessage != null) {
            try {
                JSONObject jsonObject = new JSONObject(mErrorBodyMessage);
                return jsonObject.getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (mThrowable != null) {
            return mThrowable.getLocalizedMessage();
        }

        return null;
    }

    @Nullable
    public ResponseBody getErrorBody() {
        return mErrorBody;
    }

    public void setErrorBody(ResponseBody errorBody) {
        mErrorBody = errorBody;
    }

    @Nullable
    public Throwable getThrowable() {
        return mThrowable;
    }

    public void setThrowable(Throwable throwable) {
        mThrowable = throwable;
    }
}
