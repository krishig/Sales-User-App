package com.krishig.android.ui.ManageCustomer.view;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.krishig.android.Navigator.activity.ActivityNavigator;
import com.krishig.android.Navigator.fragment.FragmentNavigator;
import com.krishig.android.R;
import com.krishig.android.data.local.sharedpreferences.SharedPreferencesHelper;
import com.krishig.android.databinding.ActivityManageCustomerBinding;
import com.krishig.android.model.Customer;
import com.krishig.android.ui.AppConstants;
import com.krishig.android.ui.ManageCustomer.adapter.ManageCustomerAdapter;
import com.krishig.android.ui.base.BaseActivity;
import com.krishig.android.ui.home.addCustomer.AddCustomerActivity;
import com.krishig.android.ui.home.fragments.home.viewmodel.SubCategoryModel;
import com.library.adapter.recyclerview.listener.OnRecyclerViewItemChildClick;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ManageCustomerActivity extends BaseActivity<ActivityManageCustomerBinding> {
    ManageCustomerAdapter manageCustomerAdapter;
    ArrayList<Customer.Content> customerArrayList = new ArrayList<>();
    ArrayList<Customer.Content> DataArrayList = new ArrayList<>();

    SubCategoryModel viewModel;

    @Inject
    SharedPreferencesHelper sharedPreferencesHelper;

    int totalPages = 0, count = 1;
    String itemPerPageInProduct = "15";

    @Override
    protected ActivityManageCustomerBinding getViewBinding() {
        return ActivityManageCustomerBinding.inflate(getLayoutInflater());
    }

    @Override
    protected ActivityNavigator getActivityNavigator() {
        return new ActivityNavigator(ManageCustomerActivity.this);
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

    private void setRecyclerView() {
        viewBinding.recyclerView.setHasFixedSize(true);
        viewBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        manageCustomerAdapter = new ManageCustomerAdapter(this);
        manageCustomerAdapter.clearAllItem();
        manageCustomerAdapter.addArrayList(customerArrayList);
        viewBinding.recyclerView.setAdapter(manageCustomerAdapter);
    }

    @Override
    protected void initializeObject() {
        setRecyclerView();
    }

    @Override
    protected void addTextChangedListener() {
        viewBinding.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equalsIgnoreCase("")) {
                    DataArrayList.clear();
                    count = 1;
                    viewModel.getCustomer(String.valueOf(count), itemPerPageInProduct, "application/json", "application/json",
                            sharedPreferencesHelper.getKeyToken());
                    showProgressDialog();
                } else {
                    viewModel.searchCustomer(editable.toString(), "application/json", "application/json",
                            sharedPreferencesHelper.getKeyToken());
                }

            }
        });

    }

    @Override
    protected void setOnFocusChangeListener() {

    }

    @Override
    protected void observeViewModel() {
        Observer<String> getCustomerUserError = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String error) {
                hideProgressDialog();
                viewBinding.idPBLoading.setVisibility(View.GONE);
                if (error == null) {
                    showToast(getString(R.string.something_went_wrong_please_try_again));
                } else {
                    showToast(error);
                }
                viewBinding.recyclerView.setVisibility(View.GONE);
                viewBinding.errorImageView.setVisibility(View.VISIBLE);
                viewBinding.errorTextView.setVisibility(View.VISIBLE);
            }
        };
        viewModel.getCustomerUserError().observe(this, getCustomerUserError);

        final Observer<Customer> getCustomerUserSuccess = new Observer<Customer>() {
            @Override
            public void onChanged(Customer list) {
                hideProgressDialog();
                viewBinding.recyclerView.setVisibility(View.VISIBLE);
                viewBinding.errorImageView.setVisibility(View.GONE);
                viewBinding.errorTextView.setVisibility(View.GONE);
                viewBinding.idPBLoading.setVisibility(View.GONE);
                totalPages = list.getTotalPages();
                customerArrayList = list.getContent();
                DataArrayList.addAll(customerArrayList);
                manageCustomerAdapter.clearAllItem();
                manageCustomerAdapter.addArrayList(DataArrayList);
            }
        };
        viewModel.getCustomerUserSuccess().observe(this, getCustomerUserSuccess);

        Observer<String> searchUserError = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String error) {
                hideProgressDialog();
                if (error == null) {
                    //showToast(getString(R.string.something_went_wrong_please_try_again));
                } else {
                    // showToast(error);
                }
            }
        };
        viewModel.searchUserError().observe(this, searchUserError);

        final Observer<Customer> searchUserSuccess = new Observer<Customer>() {
            @Override
            public void onChanged(Customer list) {
                hideProgressDialog();
                customerArrayList = list.getContent();
                manageCustomerAdapter.clearAllItem();
                manageCustomerAdapter.replaceArrayList(customerArrayList);
            }
        };
        viewModel.searchUserSuccess().observe(this, searchUserSuccess);


        Observer<String> getCustomerWithIdUserError = new Observer<String>() {
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
        viewModel.getCustomerWithIdUserError().observe(this, getCustomerWithIdUserError);

        final Observer<Customer.Content> getCustomerWithIdUserSuccess = new Observer<Customer.Content>() {
            @Override
            public void onChanged(Customer.Content list) {
                hideProgressDialog();
                finish();
                sharedPreferencesHelper.setCustomerName(list.getFullName());
                sharedPreferencesHelper.setCustomerCartId(list.getCustomerCartResponseDto().getId());
                sharedPreferencesHelper.setCustomerID(list.getCustomerCartResponseDto().getCustomerId());
                sharedPreferencesHelper.setCustomerMobileNumber(list.getMobileNumber());
            }
        };
        viewModel.getCustomerWithIdUserSuccess().observe(this, getCustomerWithIdUserSuccess);


    }

    @Override
    protected void setOnClickListener() {
        viewBinding.icBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        viewBinding.addMaterialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManageCustomerActivity.this, AddCustomerActivity.class);
                intent.putExtra(AppConstants.Extras.CUSTOMER_FROM, "0");
                startActivity(intent);
            }
        });

        manageCustomerAdapter.setOnRecyclerViewItemChildClick(new OnRecyclerViewItemChildClick<Customer.Content>() {
            @Override
            public void OnItemChildClick(View viewChild, Customer.Content content, int position) {
                switch (viewChild.getId()) {
                    case R.id.icEditImageView:
                        String addressId=null;
                        if (content.getAddress().size() != 0) {
                            addressId=content.getAddress().get(0).getId();
                        }
                        Intent intent = new Intent(ManageCustomerActivity.this, AddCustomerActivity.class);
                        intent.putExtra(AppConstants.Extras.CUSTOMER_ID, content.getId());
                        intent.putExtra(AppConstants.Extras.ADDRESS_ID,addressId );
                        intent.putExtra(AppConstants.Extras.CUSTOMER_FROM, "1");
                        startActivity(intent);
                        break;
                    case R.id.relativeLayout:
                        viewModel.getCustomerWithId(content.getId(), "application/json", "application/json",
                                sharedPreferencesHelper.getKeyToken());
                        showProgressDialog();
                        break;
                    case R.id.profileImageView:
                        viewModel.getCustomerWithId(content.getId(), "application/json", "application/json",
                                sharedPreferencesHelper.getKeyToken());
                        showProgressDialog();
                        break;
                    default:
                        break;
                }
            }
        });

        viewBinding.nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    ++count;
                    if (count <= totalPages) {
                        viewBinding.idPBLoading.setVisibility(View.VISIBLE);
                        viewModel.getCustomer(String.valueOf(count), itemPerPageInProduct, "application/json", "application/json",
                                sharedPreferencesHelper.getKeyToken());
                    }

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


    @Override
    protected void onResume() {
        super.onResume();
        DataArrayList.clear();
        count = 1;
        viewModel.getCustomer(String.valueOf(count), itemPerPageInProduct, "application/json", "application/json",
                sharedPreferencesHelper.getKeyToken());
        showProgressDialog();
    }
}