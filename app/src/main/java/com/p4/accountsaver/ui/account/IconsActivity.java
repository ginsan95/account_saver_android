package com.p4.accountsaver.ui.account;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;

import com.p4.accountsaver.R;
import com.p4.accountsaver.databinding.ActivityIconsBinding;
import com.p4.accountsaver.repository.ApiEvent;
import com.p4.accountsaver.ui.account.adapters.IconAdapter;
import com.p4.accountsaver.ui.base.BaseActivity;
import com.p4.accountsaver.utils.DialogUtils;

import java.io.IOException;
import java.io.InputStream;

public class IconsActivity extends BaseActivity {
    public static final String URL_KEY = "URL_KEY";
    private static final int PERMISSION_REQUEST_CODE = 101;
    private static final int IMAGE_REQUEST_CODE = 102;

    private ActivityIconsBinding mBinding;
    private IconsViewModel mViewModel;
    private IconAdapter mAdapter;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityIconsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setTitle(R.string.game_icon_title);

        mViewModel = ViewModelProviders.of(this).get(IconsViewModel.class);
        mBinding.setViewmodel(mViewModel);

        String url = getIntent().getStringExtra(URL_KEY);
        mAdapter = new IconAdapter(url, mViewModel);
        initRecyclerView();

        mViewModel.getViewType().observe(this, (IconsViewModel.ViewType viewType) -> {
            mAdapter.setViewType(viewType);
            invalidateOptionsMenu();
        });

        mViewModel.getSelectedIconEvent().observe(this, (String selectedUrl) -> {
            Intent intent = new Intent();
            intent.putExtra(URL_KEY, selectedUrl);
            setResult(RESULT_OK, intent);
            finish();
        });

        mViewModel.getSelectImageEvent().observe(this, (Void mVoid) -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            } else {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, IMAGE_REQUEST_CODE);
            }
        });

        mViewModel.getUploadEvent().observe(this, (ApiEvent<String> event) -> {
            if (event != null) {
                if (event.isInProgress()) {
                    getProgressDialog().show();
                } else {
                    getProgressDialog().dismiss();
                    if (!event.isSuccess() && event.getError() != null) {
                        DialogUtils.showErrorDialog(this, event.getError().getMessage());
                    }
                }
            }
        });

        mViewModel.start();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mViewModel != null && mViewModel.getViewType().getValue() != null) {
            menu.findItem(R.id.action_done).setVisible(mViewModel.getViewType().getValue() == IconsViewModel.ViewType.DELETE);
            return true;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.icons_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                if (mViewModel != null) {
                    mViewModel.doneDelete();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, IMAGE_REQUEST_CODE);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IMAGE_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK && data.getData() != null) {
                    InputStream is = null;
                    try {
                        is = getContentResolver().openInputStream(data.getData());
                        byte[] buf = new byte[is.available()];
                        while (is.read(buf) != -1);
                        if (mViewModel != null) {
                            mViewModel.uploadGameIcon(buf);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (is != null) {
                            try {
                                is.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                break;
        }
    }

    private void initRecyclerView() {
        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mBinding.recyclerView.setNestedScrollingEnabled(false);
        mBinding.recyclerView.setAdapter(mAdapter);
    }

    private ProgressDialog getProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.uploading));
            mProgressDialog.setCancelable(false);
        }
        return mProgressDialog;
    }
}
