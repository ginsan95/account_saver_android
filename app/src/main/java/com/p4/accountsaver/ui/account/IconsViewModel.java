package com.p4.accountsaver.ui.account;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.util.Log;

import com.p4.accountsaver.repository.API;
import com.p4.accountsaver.repository.ApiError;
import com.p4.accountsaver.repository.ApiEvent;
import com.p4.accountsaver.repository.BackendlessAPI;
import com.p4.accountsaver.ui.account.adapters.IconAdapter;

import java.util.List;

import retrofit2.http.DELETE;

public class IconsViewModel extends ViewModel implements IconAdapter.IconCell.IconListener {
    private final String TAG = IconsViewModel.class.getSimpleName();
    public enum ViewType {
        NORMAL, DELETE
    }

    public final ObservableList<String> urls = new ObservableArrayList<>();
    public final ObservableBoolean isRefreshing = new ObservableBoolean();

    private MutableLiveData<ViewType> mViewType = new MutableLiveData<>();
    private MutableLiveData<String> mSelectedIconEvent = new MutableLiveData<>();
    private MutableLiveData<Void> mSelectImageEvent = new MutableLiveData<>();
    private MutableLiveData<ApiEvent<String>> mUploadEvent = new MutableLiveData<>();

    public void start() {
        mViewType.setValue(ViewType.NORMAL);
        onRefresh();
    }

    public void onRefresh() {
        fetchGameIcons();
    }

    public void selectImage() {
        mSelectImageEvent.setValue(null);
    }

    public void doneDelete() {
        mViewType.setValue(ViewType.NORMAL);
    }

    @Override
    public void onIconSelected(String url) {
        mSelectedIconEvent.setValue(url);
    }

    @Override
    public boolean onIconLongClicked() {
        mViewType.setValue(ViewType.DELETE);
        return true;
    }

    @Override
    public void onIconDelete(String url) {
        if (mViewType.getValue() == ViewType.DELETE) {
            BackendlessAPI.getInstance().deleteGameIcons(url);
            urls.remove(url);
        }
    }

    private void fetchGameIcons() {
        isRefreshing.set(true);
        BackendlessAPI.getInstance().fetchGameIcons(new API.ApiListener<List<String>>() {
            @Override
            public void onSuccess(List<String> gameIcons) {
                urls.clear();
                urls.addAll(gameIcons);
                isRefreshing.set(false);
            }

            @Override
            public void onFailure(ApiError error) {
                isRefreshing.set(false);
                Log.e(TAG, error.getMessage());
            }
        });
    }

    public void uploadGameIcon(byte[] iconData) {
        mUploadEvent.setValue(new ApiEvent.Builder().inProgress(true).build());
        BackendlessAPI.getInstance().uploadGameIcon(iconData, new API.ApiListener<String>() {
            @Override
            public void onSuccess(String url) {
                urls.add(url);
                mUploadEvent.setValue(new ApiEvent.Builder().success(true).setResultData(url).build());
            }

            @Override
            public void onFailure(ApiError error) {
                mUploadEvent.setValue(new ApiEvent.Builder().setError(error).build());
            }
        });
    }

    public LiveData<ViewType> getViewType() {
        return mViewType;
    }

    public LiveData<String> getSelectedIconEvent() {
        return mSelectedIconEvent;
    }

    public LiveData<Void> getSelectImageEvent() {
        return mSelectImageEvent;
    }

    public LiveData<ApiEvent<String>> getUploadEvent() {
        return mUploadEvent;
    }
}
