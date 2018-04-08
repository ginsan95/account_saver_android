package com.p4.accountsaver.ui.account;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.p4.accountsaver.R;
import com.p4.accountsaver.databinding.ActivityIconsBinding;
import com.p4.accountsaver.ui.account.adapters.IconAdapter;
import com.p4.accountsaver.ui.base.BaseActivity;

public class IconsActivity extends BaseActivity {
    public static final String URL_KEY = "URL_KEY";
    private ActivityIconsBinding mBinding;
    private IconsViewModel mViewModel;
    private IconAdapter mAdapter;

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

    private void initRecyclerView() {
        mBinding.recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mBinding.recyclerView.setNestedScrollingEnabled(false);
        mBinding.recyclerView.setAdapter(mAdapter);
    }
}
