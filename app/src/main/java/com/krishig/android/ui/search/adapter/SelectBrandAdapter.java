package com.krishig.android.ui.search.adapter;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.krishig.android.R;
import com.krishig.android.data.remote.glide.GlideImageLoader;
import com.krishig.android.data.remote.glide.GlideImageLoadingListener;
import com.krishig.android.model.ProductBrands;
import com.krishig.android.model.SubCategory;
import com.library.adapter.recyclerview.adapter.BaseSingleItemAdapter;
import com.library.adapter.recyclerview.adapter.BaseViewHolder;

public class SelectBrandAdapter extends BaseSingleItemAdapter<ProductBrands.Result, BaseViewHolder> {

    private Context context;

    public SelectBrandAdapter(Context context) {
        addChildClickViewIds(R.id.searchDialogRowLinearLayout);
        this.context = context;
    }

    @Override
    protected int getViewHolderLayoutResId() {
        return R.layout.item_select_brand;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder viewHolder, ProductBrands.Result home, int position) {
        viewHolder.setText(R.id.brandNameTextView, home.getBrandName());
    }
}
