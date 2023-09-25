package com.krishig.android.ui.seeAllBrands.adapter;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.krishig.android.R;
import com.krishig.android.data.remote.glide.GlideImageLoader;
import com.krishig.android.data.remote.glide.GlideImageLoadingListener;
import com.krishig.android.model.ProductBrands;
import com.library.adapter.recyclerview.adapter.BaseSingleItemAdapter;
import com.library.adapter.recyclerview.adapter.BaseViewHolder;

public class SeeAllTopBrandsViewAdapter extends BaseSingleItemAdapter<ProductBrands.Result, BaseViewHolder> {

    private Context context;

    public SeeAllTopBrandsViewAdapter(Context context) {
        addChildClickViewIds(R.id.addMaterialCardView);
        this.context = context;
    }

    @Override
    protected int getViewHolderLayoutResId() {
        return R.layout.item_top_brand_fragment_second;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder viewHolder, ProductBrands.Result home, int position) {
        ImageView imageView = viewHolder.findView(R.id.categoryImageView);
        viewHolder.setText(R.id.categoryTextView,home.getBrandName());
        GlideImageLoader.load(context, home.getBrandImageUrl()
                , R.drawable.image_product
                , R.drawable.image_product,
                imageView,
                new GlideImageLoadingListener() {
                    @Override
                    public void imageLoadSuccess() {

                    }

                    @Override
                    public void imageLoadError() {

                    }
                }
        );
    }
}
