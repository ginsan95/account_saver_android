package com.p4.accountsaver.binding;

import android.databinding.BindingAdapter;
import android.support.v7.widget.RecyclerView;

import com.p4.accountsaver.ui.base.BindingRecyclerAdapter;

import java.util.List;

/**
 * Created by averychoke on 6/3/18.
 */

public class RecyclerViewBinding {

    @BindingAdapter("app:items")
    public static void setItems(RecyclerView recyclerView, List<?> objects) {
        if (recyclerView.getAdapter() != null && recyclerView.getAdapter() instanceof BindingRecyclerAdapter) {
            BindingRecyclerAdapter adapter = (BindingRecyclerAdapter) recyclerView.getAdapter();
            adapter.setItems(objects);
        }
    }

}
