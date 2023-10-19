package com.krishig.android.ui.addToBag.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AdapterListUpdateCallback;
import androidx.recyclerview.widget.RecyclerView;

import com.krishig.android.R;
import com.krishig.android.model.Customer;

import java.io.*;
import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    // Initialize variable
    ArrayList<Customer.Address> addressArraylist = new ArrayList<>();
    RadioButtonCheck itemClickListener;
    deleteAddress deleteAddress;
    int selectedPosition = -1;

    // Create constructor
    public MainAdapter(ArrayList<Customer.Address> addressArraylist, RadioButtonCheck itemClickListener, deleteAddress deleteAddress) {
        this.addressArraylist = addressArraylist;
        this.itemClickListener = itemClickListener;
        this.deleteAddress = deleteAddress;
    }

    @NonNull
    @Override
    public ViewHolder
    onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address_layout, parent,
                        false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Customer.Address address = addressArraylist.get(position);
        for (int i = 0; i < addressArraylist.size(); i++) {
            holder.textView.setText(
                    address.getHouseNumber() + ","
                            + address.getStreetName() + ","
                            + address.getVillageName() + ","
                            + address.getBlock() + ","
                            + address.getDistrict() + ","
                            + address.getState() + ","
                            + address.getPostalCode()
            );
        }

        holder.imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAddress.check(address.getId());
            }
        });

        holder.radioButton.setChecked(position== selectedPosition);

        holder.radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b) {
                            selectedPosition = holder.getAdapterPosition();
                            itemClickListener.radio(address.getId());
                        }
                    }
                });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return addressArraylist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RadioButton radioButton;
        TextView textView;
        ImageView imageDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Assign variable
            radioButton = itemView.findViewById(R.id.radioButton);
            textView = itemView.findViewById(R.id.locationTextView);
            imageDelete = itemView.findViewById(R.id.imageDelete);
        }
    }
}


