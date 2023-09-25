package com.krishig.android.ui.home.fragments.orderDetail;

import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.krishig.android.Navigator.activity.ActivityNavigator;
import com.krishig.android.Navigator.fragment.FragmentNavigator;
import com.krishig.android.R;
import com.krishig.android.data.local.sharedpreferences.SharedPreferencesHelper;
import com.krishig.android.databinding.ActivityOrderDetailBinding;
import com.krishig.android.model.Order;
import com.krishig.android.ui.AppConstants;
import com.krishig.android.ui.base.BaseActivity;
import com.krishig.android.ui.home.fragments.home.viewmodel.SubCategoryModel;
import com.krishig.android.ui.home.fragments.manageOrders.adapter.ManageOrdersAdapter;
import com.krishig.android.ui.home.fragments.orderDetail.adapter.OrderDetailAdapter;
import com.library.adapter.recyclerview.LayoutManagerUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class OrderDetailActivity extends BaseActivity<ActivityOrderDetailBinding> {
    SubCategoryModel viewModel;
    @Inject
    SharedPreferencesHelper sharedPreferencesHelper;

    String orderId = "";
    OrderDetailAdapter manageProductAdapter;
    ArrayList<Order.ProductResponseDtos> homeArrayList = new ArrayList<Order.ProductResponseDtos>();
    boolean visible = false;
    boolean visible2 = false;

    @Override
    protected ActivityOrderDetailBinding getViewBinding() {
        return ActivityOrderDetailBinding.inflate(getLayoutInflater());
    }

    @Override
    protected ActivityNavigator getActivityNavigator() {
        return new ActivityNavigator(OrderDetailActivity.this);
    }

    @Override
    protected FragmentNavigator getFragmentNavigator() {
        return null;
    }

    @Override
    protected void initializeViewModel() {
        viewModel = createViewModel(SubCategoryModel.class);

    }


    @Override
    protected void initializeToolBar() {

    }

    @Override
    protected void initializeObject() {
        if (getIntent().getExtras() != null) {
            orderId = getIntent().getExtras().getString(AppConstants.Extras.ORDER_ID);
        }
        if (orderId != null) {
            viewModel.fetchOrderDetail(orderId, "application/json", "application/json",
                    sharedPreferencesHelper.getKeyToken());
            showProgressDialog();
        }
        setRecyclerView(viewBinding.seedsRecyclerView);
    }

    private void setRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(LayoutManagerUtils.getGridLayoutManagerVertical(this, 1));
        recyclerView.setHasFixedSize(true);
        manageProductAdapter = new OrderDetailAdapter(this);
        manageProductAdapter.addArrayList(homeArrayList);
        recyclerView.setAdapter(manageProductAdapter);
    }

    @Override
    protected void addTextChangedListener() {

    }

    @Override
    protected void setOnFocusChangeListener() {

    }

    @Override
    protected void observeViewModel() {

        Observer<String> fetchOrderUserError = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String error) {
                hideProgressDialog();
                if (error == null) {
                    showToast(getString(R.string.something_went_wrong_please_try_again));
                } else {
                    showToast(error);
                }
            }
        };
        viewModel.fetchOrderUserError().observe(this, fetchOrderUserError);

        final Observer<Order> fetchOrderUserSuccess = new Observer<Order>() {
            @Override
            public void onChanged(Order order) {
                hideProgressDialog();

                viewBinding.categoryTextView.setText(order.getOrderId());
                viewBinding.categoryPriceTextView.setText("Rs. " + order.getTotalPrice());
                viewBinding.statusTextView.setText(order.getStatus());
                viewBinding.contactNumberTextView.setText(order.getContactNumber());

                String inputDateString = order.getCreatedDate();
                String formattedDate = convertDateFormat(inputDateString);

                viewBinding.dateTextView.setText(formattedDate);

                viewBinding.customerNameTextView.setText("Name - " + order.getCustomerId().getFullName());
                viewBinding.customerMobileNumberTextView.setText("Contact Number - " + order.getCustomerId().getMobileNumber());

                if (order.getAddressResponseDto() != null) {
                    viewBinding.locationTextView.setText(order.getAddressResponseDto().getHouseNumber() + ","
                            + order.getAddressResponseDto().getStreetName() + ","
                            + order.getAddressResponseDto().getVillageName() + ","
                            + order.getAddressResponseDto().getDistrict() + ","
                            + order.getAddressResponseDto().getState() + ","
                            + order.getAddressResponseDto().getPostalCode()
                    );
                }
                homeArrayList = order.getProductResponseDtos();
                manageProductAdapter.clearAllItem();
                manageProductAdapter.replaceArrayList(homeArrayList);
            }
        };
        viewModel.fetchOrderUserSuccess().observe(this, fetchOrderUserSuccess);
    }

    public static String convertDateFormat(String inputDateString) {
        try {
            SimpleDateFormat inputFormat = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.US);
            }
            Date date = inputFormat.parse(inputDateString);
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    protected void setOnClickListener() {
        viewBinding.icBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        viewBinding.dropDownImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (visible) {
                    visible = false;
                    viewBinding.dropDownImageView.setRotation(360);
                    viewBinding.contentLinearLayout.setVisibility(View.VISIBLE);
                } else {
                    visible = true;
                    viewBinding.dropDownImageView.setRotation(180);
                    viewBinding.contentLinearLayout.setVisibility(View.GONE);
                }
            }

        });
        viewBinding.customerDetailTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (visible) {
                    visible = false;
                    viewBinding.dropDownImageView.setRotation(360);
                    viewBinding.contentLinearLayout.setVisibility(View.VISIBLE);
                } else {
                    visible = true;
                    viewBinding.dropDownImageView.setRotation(180);
                    viewBinding.contentLinearLayout.setVisibility(View.GONE);
                }
            }

        });


        viewBinding.customerAddressTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (visible2) {
                    visible2 = false;
                    viewBinding.dropDownAddressImageView.setRotation(360);
                    viewBinding.locationLinearLayout.setVisibility(View.VISIBLE);
                } else {
                    visible2 = true;
                    viewBinding.dropDownAddressImageView.setRotation(180);
                    viewBinding.locationLinearLayout.setVisibility(View.GONE);
                }
            }

        });
        viewBinding.dropDownAddressImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (visible2) {
                    visible2 = false;
                    viewBinding.dropDownImageView.setRotation(360);
                    viewBinding.locationLinearLayout.setVisibility(View.VISIBLE);
                } else {
                    visible2 = true;
                    viewBinding.dropDownImageView.setRotation(180);
                    viewBinding.locationLinearLayout.setVisibility(View.GONE);
                }
            }

        });
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void showProgressDialog() {
        if (viewBinding.progressDialog.pleaseWaitProgressBar.getVisibility() == View.GONE) {
            viewBinding.progressDialog.pleaseWaitProgressBar.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

    }

    @Override
    public void hideProgressDialog() {
        if (viewBinding.progressDialog.pleaseWaitProgressBar.getVisibility() == View.VISIBLE) {
            viewBinding.progressDialog.pleaseWaitProgressBar.setVisibility(View.GONE);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }
}