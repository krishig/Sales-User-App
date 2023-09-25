package com.krishig.android.ui.home.fragments.home.adapter;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.krishig.android.R;
import com.krishig.android.data.remote.glide.GlideImageLoader;
import com.krishig.android.data.remote.glide.GlideImageLoadingListener;
import com.krishig.android.model.Category;
import com.krishig.android.model.Market;
import com.krishig.android.model.SubCategory;
import com.library.adapter.recyclerview.adapter.BaseSingleItemAdapter;
import com.library.adapter.recyclerview.adapter.BaseViewHolder;

public class CategoryRecyclerViewAdapter extends BaseSingleItemAdapter<SubCategory.Result, BaseViewHolder> {

    private Context context;

    public CategoryRecyclerViewAdapter(Context context) {
        addChildClickViewIds(R.id.addMaterialCardView);
        this.context = context;
    }

    @Override
    protected int getViewHolderLayoutResId() {
        return R.layout.item_home_fragment;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder viewHolder, SubCategory.Result home, int position) {

        viewHolder.setText(R.id.categoryTextView,home.getSubCategoryName());
        ImageView imageView=viewHolder.findView(R.id.categoryImageView);

        GlideImageLoader.load(context, home.getImage_url()
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
