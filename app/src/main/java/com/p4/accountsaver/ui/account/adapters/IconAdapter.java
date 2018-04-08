package com.p4.accountsaver.ui.account.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.p4.accountsaver.R;
import com.p4.accountsaver.databinding.CellIconBinding;
import com.p4.accountsaver.ui.account.IconsViewModel;
import com.p4.accountsaver.ui.base.BindingRecyclerAdapter;

public class IconAdapter extends BindingRecyclerAdapter<IconAdapter.IconCell, String> {
    private String mSelectedUrl;
    private IconCell.IconListener mListener;
    private IconsViewModel.ViewType mViewType;

    public IconAdapter(String selectedUrl, IconCell.IconListener listener) {
        mSelectedUrl = selectedUrl;
        mListener = listener;
    }

    @NonNull
    @Override
    public IconCell onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_icon, parent, false);
        return new IconCell(rootView, mSelectedUrl, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull IconCell holder, int position) {
        holder.setUrl(getItems().get(position));
        if (mViewType != null) {
            holder.setViewType(mViewType);
        }
    }

    public void setViewType(IconsViewModel.ViewType viewType) {
        mViewType = viewType;
        notifyDataSetChanged();
    }

    public static class IconCell extends RecyclerView.ViewHolder {
        public interface IconListener {
            void onIconSelected(String url);
            boolean onIconLongClicked();
            void onIconDelete(String url);
        }

        private CellIconBinding mBinding;
        private String mSelectedUrl;

        public IconCell(View itemView, String selectedUrl, IconListener listener) {
            super(itemView);
            mBinding = CellIconBinding.bind(itemView);
            mSelectedUrl = selectedUrl;
            mBinding.setListener(listener);
        }

        void setUrl(String url) {
            mBinding.setUrl(url);
            mBinding.setIsSelected(url.equals(mSelectedUrl));
        }

        void setViewType(IconsViewModel.ViewType viewType) {
            mBinding.setIsDeleteMode(viewType == IconsViewModel.ViewType.DELETE);
        }
    }
}
