package com.krishig.android.ui.ManageCustomer.adapter;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.krishig.android.R;
import com.krishig.android.data.remote.glide.GlideImageLoader;
import com.krishig.android.data.remote.glide.GlideImageLoadingListener;
import com.krishig.android.model.Customer;
import com.krishig.android.model.ProductId;
import com.library.adapter.recyclerview.adapter.BaseSingleItemAdapter;
import com.library.adapter.recyclerview.adapter.BaseViewHolder;

public class ManageCustomerAdapter extends BaseSingleItemAdapter<Customer.Content, BaseViewHolder> {

    private Context context;

    public ManageCustomerAdapter(Context context) {
        this.context = context;
        addChildClickViewIds(R.id.icEditImageView);
        addChildClickViewIds(R.id.relativeLayout);
        addChildClickViewIds(R.id.profileImageView);
    }

    @Override
    protected int getViewHolderLayoutResId() {
        return R.layout.item_customer;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder viewHolder, Customer.Content customer, int position) {

        ImageView imageView = viewHolder.findView(R.id.profileImageView);

        /*GlideImageLoader.load(
                context,
                customer.getImageUrl(),
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
                });*/

        viewHolder.setText(R.id.nameTextView, customer.getFullName());
        viewHolder.setText(R.id.orderIdTextView, customer.getMobileNumber());

    }
}
