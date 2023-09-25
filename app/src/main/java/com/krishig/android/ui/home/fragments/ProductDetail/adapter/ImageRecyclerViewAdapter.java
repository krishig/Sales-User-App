package com.krishig.android.ui.home.fragments.ProductDetail.adapter;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.krishig.android.R;
import com.krishig.android.data.remote.glide.GlideImageLoader;
import com.krishig.android.data.remote.glide.GlideImageLoadingListener;
import com.krishig.android.model.ProductId;
import com.library.adapter.recyclerview.adapter.BaseSingleItemAdapter;
import com.library.adapter.recyclerview.adapter.BaseViewHolder;

public class ImageRecyclerViewAdapter extends BaseSingleItemAdapter<ProductId.Result.ProductImage, BaseViewHolder> {

    private Context context;

    public ImageRecyclerViewAdapter(Context context) {
        this.context = context;
        addChildClickViewIds(R.id.zoomImageView);
    }

    @Override
    protected int getViewHolderLayoutResId() {
        return R.layout.viewpager_image;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder viewHolder, ProductId.Result.ProductImage subCategory, int position) {

        ImageView imageView = viewHolder.findView(R.id.productImageView);

        System.out.println(subCategory.getImageUrl());
        GlideImageLoader.load(
                context,
                subCategory.getImageUrl(),
                R.drawable.image_product,
                R.drawable.image_product,
                imageView,
                new GlideImageLoadingListener() {
                    @Override
                    public void imageLoadSuccess() {

                    }

                    @Override
                    public void imageLoadError() {

                    }
                });

    }
}
