package com.krishig.android.ui.addToBag.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.krishig.android.R;
import com.krishig.android.data.remote.glide.GlideImageLoader;
import com.krishig.android.data.remote.glide.GlideImageLoadingListener;
import com.krishig.android.model.Cart;
import com.krishig.android.model.Customer;
import com.library.adapter.recyclerview.adapter.BaseSingleItemAdapter;
import com.library.adapter.recyclerview.adapter.BaseViewHolder;

public class AddressAdapter extends BaseSingleItemAdapter<Customer.Address, BaseViewHolder> {

    private Context context;
    int selectedPosition = -1;
    RadioButtonCheck radioButtonCheck;

    public AddressAdapter(Context context, RadioButtonCheck radioButtonCheck) {
        addChildClickViewIds(R.id.imageDelete);
        this.context = context;
        this.radioButtonCheck = radioButtonCheck;
    }

    @Override
    protected int getViewHolderLayoutResId() {
        return R.layout.item_address_layout;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder viewHolder, Customer.Address address, int position) {

        viewHolder.setText(R.id.locationTextView, address.getHouseNumber() + ","
                + address.getStreetName() + ","
                + address.getVillageName() + ","
                + address.getDistrict() + ","
                + address.getState() + ","
                + address.getPostalCode()
        );
    }
}
