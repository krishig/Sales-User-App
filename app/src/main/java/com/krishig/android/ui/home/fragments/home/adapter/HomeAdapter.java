package com.krishig.android.ui.home.fragments.home.adapter;

import android.content.Context;

import androidx.annotation.NonNull;

import com.krishig.android.R;
import com.krishig.android.model.Home;
import com.library.adapter.recyclerview.adapter.BaseSingleItemAdapter;
import com.library.adapter.recyclerview.adapter.BaseViewHolder;

public class HomeAdapter extends BaseSingleItemAdapter<Home, BaseViewHolder> {

    private Context context;

    public HomeAdapter(Context context) {
        this.context = context;
        addChildClickViewIds(R.id.viewDetailLinearLayout);
    }

    @Override
    protected int getViewHolderLayoutResId() {
        return R.layout.item_home_layout;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder viewHolder, Home stock, int position) {
        viewHolder.setText(R.id.orderIdDetailTextView, stock.getOrderIdDetailTextView());
        viewHolder.setText(R.id.customerNameDetailTextView, stock.getCustomerNameDetailTextView());
        viewHolder.setText(R.id.productDetailTextView, stock.getProductDetailTextView());
        viewHolder.setText(R.id.quantityDetailTextView, stock.getQuantityDetailTextView());
        viewHolder.setText(R.id.totalAmountDetailTextView, stock.getTotalAmountDetailTextView());

    }
}
