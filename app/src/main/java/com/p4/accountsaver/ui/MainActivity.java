package com.p4.accountsaver.ui;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.p4.accountsaver.R;
import com.p4.accountsaver.databinding.ActivityMainBinding;
import com.p4.accountsaver.ui.account.AccountFragment;
import com.p4.accountsaver.ui.profile.ProfileFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        mBinding.navView.setNavigationItemSelectedListener(this);
        setSupportActionBar(mBinding.toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mBinding.drawerLayout, R.string.nav_drawer_open, R.string.nav_drawer_close);
        mBinding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        onNavigationItemSelected(mBinding.navView.getMenu().findItem(R.id.nav_account));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (!item.isChecked()) {
            item.setChecked(true);
            switch (item.getItemId()) {
                case R.id.nav_account:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(mBinding.containerLayout.getId(), new AccountFragment())
                            .commit();
                    break;
                case R.id.nav_profile:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(mBinding.containerLayout.getId(), new ProfileFragment())
                            .commit();
                    break;
            }
        }

        mBinding.drawerLayout.closeDrawers();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mBinding.drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

    }
}
