package com.krishig.android.ui.addToBag.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.krishig.android.R;
import com.krishig.android.data.remote.glide.GlideImageLoader;
import com.krishig.android.data.remote.glide.GlideImageLoadingListener;
import com.krishig.android.model.Cart;
import com.krishig.android.model.Product;
import com.krishig.android.model.SubCategory;
import com.library.adapter.recyclerview.adapter.BaseSingleItemAdapter;
import com.library.adapter.recyclerview.adapter.BaseViewHolder;

public class AddToBagAdapter extends BaseSingleItemAdapter<Cart.CartProductResponseDto, BaseViewHolder> {

    private Context context;

    public AddToBagAdapter(Context context) {
        addChildClickViewIds(R.id.addMaterialCardView);
        addChildClickViewIds(R.id.minusMaterialCardView);
        this.context = context;
    }

    @Override
    protected int getViewHolderLayoutResId() {
        return R.layout.item_product_layout;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder viewHolder, Cart.CartProductResponseDto cart, int position) {

        viewHolder.setText(R.id.orderIdTextView, cart.getProductResponseDto().getProductName());
        viewHolder.setText(R.id.discountTextView, "Discount - "+cart.getProductResponseDto().getDiscount()+"%");
        TextView textView = viewHolder.findView(R.id.productTextView);
        TextView countTextView = viewHolder.findView(R.id.countTextView);
        textView.setText("Rs. " + cart.getProductResponseDto().getPrice());
        countTextView.setText(cart.getProductQuantity());
        textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        viewHolder.setText(R.id.quantityTextView, "Rs. " + cart.getDiscountPrice());

        ImageView imageView = viewHolder.findView(R.id.imageProduct);

        if (cart.getProductResponseDto().getProductImageResponse().getImageUrl() != null) {
            GlideImageLoader.load(context, cart.getProductResponseDto().getProductImageResponse().getImageUrl()
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
