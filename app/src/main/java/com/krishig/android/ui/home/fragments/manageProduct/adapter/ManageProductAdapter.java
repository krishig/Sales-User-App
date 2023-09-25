package com.krishig.android.ui.home.fragments.manageProduct.adapter;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.krishig.android.R;
import com.krishig.android.data.remote.glide.GlideImageLoader;
import com.krishig.android.data.remote.glide.GlideImageLoadingListener;
import com.krishig.android.model.Product;
import com.library.adapter.recyclerview.adapter.BaseSingleItemAdapter;
import com.library.adapter.recyclerview.adapter.BaseViewHolder;

public class ManageProductAdapter extends BaseSingleItemAdapter<Product.Result, BaseViewHolder> {

    private Context context;

    public ManageProductAdapter(Context context) {
        this.context = context;
        addChildClickViewIds(R.id.addSeedMaterialCardView);
    }

    @Override
    protected int getViewHolderLayoutResId() {
        return R.layout.item_seeds;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder viewHolder, Product.Result stock, int position) {

        viewHolder.setText(R.id.categoryTextView,stock.getProductName());
        viewHolder.setText(R.id.categoryPriceTextView,"Rs "+stock.getPrice());

        ImageView imageView = viewHolder.findView(R.id.categoryImageView);

        if (stock.getProductImage().size()!=0) {
            GlideImageLoader.load(context,
                    stock.getProductImage().get(0).getImageUrl(),
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
                    }
            );
        }else{
            imageView.setImageResource(R.drawable.image_product);
        }

    }
}
