package com.p4.accountsaver.repository;

/**
 * Created by averychoke on 1/3/18.
 */

public class ApiEvent {
    private boolean mInProgress;
    private boolean mSuccess;
    private ApiError mError;

    public ApiEvent() {}

    public boolean isInProgress() {
        return mInProgress;
    }

    private void inProgress(boolean inProgress) {
        mInProgress = inProgress;
    }

    public boolean isSuccess() {
        return mSuccess;
    }

    private void success(boolean success) {
        mSuccess = success;
    }

    public ApiError getError() {
        return mError;
    }

    private void setError(ApiError error) {
        mError = error;
    }

    public static class Builder {
        private ApiEvent mEvent;

        public Builder() {
            mEvent = new ApiEvent();
        }

        public ApiEvent build() {
            return mEvent;
        }

        public Builder inProgress(boolean inProgress) {
            mEvent.inProgress(inProgress);
            return this;
        }

        public Builder success(boolean success) {
            mEvent.success(success);
            return this;
        }

        public Builder setError(ApiError error) {
            mEvent.setError(error);
            return this;
        }
    }
}
