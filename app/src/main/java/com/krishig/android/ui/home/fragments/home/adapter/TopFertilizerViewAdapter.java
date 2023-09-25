package com.krishig.android.ui.home.fragments.home.adapter;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.krishig.android.R;
import com.krishig.android.data.remote.glide.GlideImageLoader;
import com.krishig.android.data.remote.glide.GlideImageLoadingListener;
import com.krishig.android.model.Product;
import com.library.adapter.recyclerview.adapter.BaseSingleItemAdapter;
import com.library.adapter.recyclerview.adapter.BaseViewHolder;

public class TopFertilizerViewAdapter extends BaseSingleItemAdapter<Product.Result, BaseViewHolder> {

    private Context context;

    public TopFertilizerViewAdapter(Context context) {
        addChildClickViewIds(R.id.addSeedMaterialCardView);
        this.context = context;
    }

    @Override
    protected int getViewHolderLayoutResId() {
        return R.layout.item_seeds;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder viewHolder, Product.Result home, int position) {
        ImageView imageView = viewHolder.findView(R.id.categoryImageView);

        viewHolder.setText(R.id.categoryTextView,home.getProductName());
        viewHolder.setText(R.id.categoryPriceTextView,"Rs "+home.getPrice());

        if(home.getProductImage().size()==0){

        }else{
            GlideImageLoader.load(context, home.getProductImage().get(0).getImageUrl()
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
}
