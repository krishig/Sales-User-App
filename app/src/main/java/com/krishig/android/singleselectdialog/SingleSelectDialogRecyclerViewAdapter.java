package com.krishig.android.singleselectdialog;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.library.adapter.recyclerview.adapter.BaseSingleItemAdapter;
import com.library.adapter.recyclerview.adapter.BaseViewHolder;
import com.krishig.android.R;
import com.krishig.android.data.remote.glide.GlideImageLoader;
import com.krishig.android.data.remote.glide.GlideImageLoadingListener;
import com.krishig.android.ui.AppConstants;


public class SingleSelectDialogRecyclerViewAdapter extends BaseSingleItemAdapter<SingleSelect, BaseViewHolder>  {

    private static final String TAG = SingleSelectDialogRecyclerViewAdapter.class.getSimpleName();

    public Context context;

    public SingleSelectDialogRecyclerViewAdapter(Context context) {
        this.context    = context;
        addChildClickViewIds(R.id.itemLinearLayout);
    }

    @Override
    protected int getViewHolderLayoutResId() {
        return R.layout.single_select_dialog_item;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder viewHolder, SingleSelect item, int position) {
        ImageView itemImageView = viewHolder.findView(R.id.itemImageView);
        TextView itemNameTextView = viewHolder.findView(R.id.itemNameTextView);

        if (item.getImage()==null)
        {
           itemImageView.setVisibility(View.GONE);
        }
        else
        {
            GlideImageLoader.load(
                    context,
                    AppConstants.APIPath.BASE_URL +item.getImage(),
                    R.drawable.checkbox_icon,
                    R.drawable.checkbox_icon,
                    itemImageView,
                    new GlideImageLoadingListener() {
                        @Override
                        public void imageLoadSuccess() {
                        }

                        @Override
                        public void imageLoadError() {
                        }
                    });
        }

        itemNameTextView.setText(item.getName());
    }
}
