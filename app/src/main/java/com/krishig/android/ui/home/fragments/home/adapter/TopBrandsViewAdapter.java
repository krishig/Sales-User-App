package com.krishig.android.ui.home.fragments.home.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.krishig.android.R;
import com.krishig.android.data.remote.glide.GlideImageLoader;
import com.krishig.android.data.remote.glide.GlideImageLoadingListener;
import com.krishig.android.model.Market;
import com.krishig.android.model.ProductBrands;
import com.library.adapter.recyclerview.adapter.BaseSingleItemAdapter;
import com.library.adapter.recyclerview.adapter.BaseViewHolder;

public class TopBrandsViewAdapter extends BaseSingleItemAdapter<ProductBrands.Result, BaseViewHolder> {

    private Context context;

    public TopBrandsViewAdapter(Context context) {
        addChildClickViewIds(R.id.addMaterialCardView);
        this.context = context;
    }

    @Override
    protected int getViewHolderLayoutResId() {
        return R.layout.item_top_brand_fragment;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder viewHolder, ProductBrands.Result home, int position) {
        ImageView imageView = viewHolder.findView(R.id.categoryImageView);
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
