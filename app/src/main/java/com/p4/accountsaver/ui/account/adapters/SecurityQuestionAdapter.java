package com.p4.accountsaver.ui.account.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.p4.accountsaver.R;
import com.p4.accountsaver.databinding.CellSecurityQuestionBinding;
import com.p4.accountsaver.model.SecurityQuestion;
import com.p4.accountsaver.ui.base.BindingRecyclerAdapter;

public class SecurityQuestionAdapter extends BindingRecyclerAdapter<SecurityQuestionAdapter.SecurityQuestionCell, SecurityQuestion>{
    private SecurityQuestionCell.SecurityQuestionListener mListener;

    public SecurityQuestionAdapter(SecurityQuestionCell.SecurityQuestionListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public SecurityQuestionCell onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_security_question, parent, false);
        return new SecurityQuestionCell(rootView, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SecurityQuestionCell holder, int position) {
        holder.setQuestion(getItems().get(position));
    }

    public static class SecurityQuestionCell extends RecyclerView.ViewHolder {
        public interface SecurityQuestionListener {
            boolean onSecurityQuestionLongClicked(SecurityQuestion securityQuestion);
        }

        private CellSecurityQuestionBinding mBinding;

        public SecurityQuestionCell(View itemView, SecurityQuestionListener listener) {
            super(itemView);
            mBinding = CellSecurityQuestionBinding.bind(itemView);
            mBinding.setListener(listener);
        }

        public void setQuestion(SecurityQuestion securityQuestion) {
            mBinding.setSecurityQuestion(securityQuestion);
        }
    }
}
