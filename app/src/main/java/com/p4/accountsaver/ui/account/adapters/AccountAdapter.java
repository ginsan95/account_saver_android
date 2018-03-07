package com.p4.accountsaver.ui.account.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.p4.accountsaver.R;
import com.p4.accountsaver.databinding.CellAccountBinding;
import com.p4.accountsaver.model.Account;
import com.p4.accountsaver.ui.base.BindingRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by averychoke on 5/3/18.
 */

public class AccountAdapter extends BindingRecyclerAdapter<AccountAdapter.AccountCell, Account> {
    private AccountCell.AccountListener mListener;

    public AccountAdapter(AccountCell.AccountListener listener) {
        mListener = listener;
    }

    @Override
    public AccountCell onCreateViewHolder(ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_account, parent, false);
        return new AccountCell(rootView, mListener);
    }

    @Override
    public void onBindViewHolder(AccountCell holder, int position) {
        holder.setAccount(getItems().get(position));
    }

    public static class AccountCell extends RecyclerView.ViewHolder {
        public interface AccountListener {
            void onAccountSelected(Account account);
        }

        private CellAccountBinding mBinding;

        AccountCell(View itemView, AccountListener listener) {
            super(itemView);
            mBinding = CellAccountBinding.bind(itemView);
            mBinding.setListener(listener);
        }

        void setAccount(Account account) {
            mBinding.setAccount(account);
        }
    }
}
