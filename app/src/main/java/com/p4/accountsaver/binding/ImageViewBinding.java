package com.p4.accountsaver.binding;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.p4.accountsaver.R;
import com.squareup.picasso.Picasso;

/**
 * Created by averychoke on 8/3/18.
 */

public class ImageViewBinding {

    @BindingAdapter("app:imageUrl")
    public static void setImageUrl(ImageView imageView, String url) {
        Picasso.get()
                .load(url)
                .placeholder(R.color.colorPrimary)
                .into(imageView);
    }

}
