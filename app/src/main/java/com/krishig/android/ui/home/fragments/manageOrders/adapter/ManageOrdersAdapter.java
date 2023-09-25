package com.krishig.android.ui.home.fragments.manageOrders.adapter;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.krishig.android.R;
import com.krishig.android.data.remote.glide.GlideImageLoader;
import com.krishig.android.data.remote.glide.GlideImageLoadingListener;
import com.krishig.android.model.Order;
import com.krishig.android.model.Product;
import com.library.adapter.recyclerview.adapter.BaseSingleItemAdapter;
import com.library.adapter.recyclerview.adapter.BaseViewHolder;

public class ManageOrdersAdapter extends BaseSingleItemAdapter<Order.Content, BaseViewHolder> {

    private Context context;

    public ManageOrdersAdapter(Context context) {
        this.context = context;
        addChildClickViewIds(R.id.addMaterialCardView);
    }

    @Override
    protected int getViewHolderLayoutResId() {
        return R.layout.item_orders;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder viewHolder, Order.Content stock, int position) {

        viewHolder.setText(R.id.categoryTextView,stock.getOrderId());
        viewHolder.setText(R.id.categoryPriceTextView,"Rs "+stock.getTotalPrice());
        viewHolder.setText(R.id.statusTextView,stock.getStatus());

        ImageView imageView = viewHolder.findView(R.id.categoryImageView);

        if (stock.getProductResponseDtos().size()!=0) {
            GlideImageLoader.load(context,
                    stock.getProductResponseDtos().get(0).getProductImageResponse().getImageUrl(),
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
