package com.krishig.android.ui.home.fragments.manageOrders.view;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.krishig.android.Navigator.activity.ActivityNavigator;
import com.krishig.android.Navigator.fragment.FragmentNavigator;
import com.krishig.android.R;
import com.krishig.android.data.local.sharedpreferences.SharedPreferencesHelper;
import com.krishig.android.databinding.FragmentManageOrdersBinding;
import com.krishig.android.model.Order;
import com.krishig.android.ui.AppConstants;
import com.krishig.android.ui.KeyboardVisibility;
import com.krishig.android.ui.base.BaseFragment;
import com.krishig.android.ui.home.fragments.home.viewmodel.SubCategoryModel;
import com.krishig.android.ui.home.fragments.manageOrders.adapter.ManageOrdersAdapter;
import com.krishig.android.ui.home.fragments.orderDetail.OrderDetailActivity;
import com.krishig.android.ui.home.view.HomeActivity;
import com.library.adapter.recyclerview.LayoutManagerUtils;
import com.library.adapter.recyclerview.listener.OnRecyclerViewItemChildClick;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ManageOrdersFragment extends BaseFragment<FragmentManageOrdersBinding> {
    SubCategoryModel viewModel;
    @Inject
    SharedPreferencesHelper sharedPreferencesHelper;
    String itemPerPageInProduct = "5",searchData = "";
    int totalPages = 0, count = 1;
    ManageOrdersAdapter manageProductAdapter;
    ArrayList<Order.Content> homeArrayList = new ArrayList<Order.Content>();
    ArrayList<Order.Content> DataArrayList = new ArrayList<Order.Content>();

    @Override
    protected ActivityNavigator getActivityNavigator() {
        return new ActivityNavigator(getActivity());
    }

    @Override
    protected FragmentNavigator getFragmentNavigator() {
        return null;
    }

    @Override
    protected FragmentManageOrdersBinding getViewBinding() {
        return FragmentManageOrdersBinding.inflate(getLayoutInflater());
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
        registerKeyboardVisibilityListener(getActivity());
        setRecyclerView(viewBinding.recyclerView);

    }

    private void setRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(LayoutManagerUtils.getGridLayoutManagerVertical(getContext(), 1));
        recyclerView.setHasFixedSize(true);
        manageProductAdapter = new ManageOrdersAdapter(getContext());
        manageProductAdapter.addArrayList(homeArrayList);
        recyclerView.setAdapter(manageProductAdapter);
    }

    @Override
    protected void addTextChangedListener() {
        viewBinding.orderEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchData=editable.toString();
                if (searchData.equalsIgnoreCase("")) {
                  /*  DataArrayList.clear();
                    count = 1;
                    Log.e("","search-------------------------------------");
                    viewModel.getAllOrder(String.valueOf(count),itemPerPageInProduct, "application/json", "application/json",
                            sharedPreferencesHelper.getKeyToken());
                    showProgressDialog();*/
                } else {
                    DataArrayList.clear();
                    count = 1;
                    viewModel.searchOrder(itemPerPageInProduct, String.valueOf(count),editable.toString(), "application/json", "application/json",
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
        Observer<String> getOrderBookUserError = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String error) {
                hideProgressDialog();
                viewBinding.idPBLoading.setVisibility(View.GONE);

                if (error == null) {
                    showToast(getString(R.string.something_went_wrong_please_try_again));
                } else {
                    showToast(error);
                }
            }
        };
        viewModel.getOrderBookUserError().observe(this, getOrderBookUserError);

        final Observer<Order> getOrderBookUserSuccess = new Observer<Order>() {
            @Override
            public void onChanged(Order categories) {
                hideProgressDialog();
                totalPages = categories.getTotalPages();
                viewBinding.idPBLoading.setVisibility(View.GONE);
                if (categories.getContent().size() == 0) {
                    viewBinding.recyclerView.setVisibility(View.GONE);
                    viewBinding.errorImageView.setVisibility(View.VISIBLE);
                    viewBinding.errorTextView.setVisibility(View.VISIBLE);
                } else {
                    viewBinding.recyclerView.setVisibility(View.VISIBLE);
                    viewBinding.errorImageView.setVisibility(View.GONE);
                    viewBinding.errorTextView.setVisibility(View.GONE);
                    homeArrayList = categories.getContent();
                    DataArrayList.addAll(homeArrayList);
                    manageProductAdapter.clearAllItem();
                    manageProductAdapter.replaceArrayList(DataArrayList);
                    System.out.println("="+DataArrayList.size());
                }

            }
        };
        viewModel.getOrderBookUserSuccess().observe(this, getOrderBookUserSuccess);


        Observer<String> searchOrderUserError = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String error) {
                hideProgressDialog();
                viewBinding.idPBLoading.setVisibility(View.GONE);
                if (error == null) {
                    showToast(getString(R.string.something_went_wrong_please_try_again));
                } else {
                    showToast(error);
                }
            }
        };
        viewModel.searchOrderUserError().observe(this, searchOrderUserError);

        final Observer<Order> searchOrderUserSuccess = new Observer<Order>() {
            @Override
            public void onChanged(Order categories) {
                hideProgressDialog();
                viewBinding.idPBLoading.setVisibility(View.GONE);
                if (categories != null) {
                    totalPages = categories.getTotalPages();
                    if (categories.getContent().size() == 0) {
                        viewBinding.recyclerView.setVisibility(View.GONE);
                        viewBinding.errorImageView.setVisibility(View.VISIBLE);
                        viewBinding.errorTextView.setVisibility(View.VISIBLE);
                    } else {
                        viewBinding.recyclerView.setVisibility(View.VISIBLE);
                        viewBinding.errorImageView.setVisibility(View.GONE);
                        viewBinding.errorTextView.setVisibility(View.GONE);
                        homeArrayList = categories.getContent();
                        DataArrayList.addAll(homeArrayList);
                        manageProductAdapter.clearAllItem();
                        manageProductAdapter.replaceArrayList(DataArrayList);
                    }
                }
            }
        };
        viewModel.searchOrderUserSuccess().observe(this, searchOrderUserSuccess);

    }

    @Override
    protected void setOnClickListener() {
        viewBinding.nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    ++count;
                    if (count <= totalPages) {
                        viewBinding.idPBLoading.setVisibility(View.VISIBLE);
                        if (searchData.equalsIgnoreCase("")) {
                            Log.e("","scroller-------------------------------------");
                            viewModel.getAllOrder(sharedPreferencesHelper.getCustomerId(),String.valueOf(count),itemPerPageInProduct,  "application/json", "application/json",
                                    sharedPreferencesHelper.getKeyToken());
                            showProgressDialog();
                        } else {
                            viewModel.searchOrder(itemPerPageInProduct, String.valueOf(count),searchData, "application/json", "application/json",
                                    sharedPreferencesHelper.getKeyToken());
                        }

                    }

                }
            }
        });

        manageProductAdapter.setOnRecyclerViewItemChildClick(new OnRecyclerViewItemChildClick<Order.Content>() {
            @Override
            public void OnItemChildClick(View viewChild, Order.Content content, int position) {
                switch (viewChild.getId()) {
                    case R.id.addMaterialCardView:
                        Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                        intent.putExtra(AppConstants.Extras.ORDER_ID, content.getId());
                        startActivity(intent);
                        break;
                    default:
                        break;
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
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

    }

    @Override
    public void hideProgressDialog() {
        if (viewBinding.progressDialog.pleaseWaitProgressBar.getVisibility() == View.VISIBLE) {
            viewBinding.progressDialog.pleaseWaitProgressBar.setVisibility(View.GONE);
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("","resume-------------------------------------");
        DataArrayList.clear();
        count = 1;
        viewModel.getAllOrder(sharedPreferencesHelper.getCustomerId(),String.valueOf(count),itemPerPageInProduct,  "application/json", "application/json",
                sharedPreferencesHelper.getKeyToken());
        showProgressDialog();
    }

    public void registerKeyboardVisibilityListener(Activity activity) {
        unRegisterKeyboardVisibilityListener();
        KeyboardVisibility.addKeyboardToggleListener(activity, new KeyboardVisibility.SoftKeyboardToggleListener() {
            @Override
            public void onToggleSoftKeyboard(boolean isVisible) {
                if (isVisible) {
                    if (getActivity() instanceof HomeActivity) {
                        HomeActivity leftNavDrawerActivity = (HomeActivity) getActivity();
                        leftNavDrawerActivity.hideBottomNavigation();
                    }
                } else {
                    if (getActivity() instanceof HomeActivity) {
                        HomeActivity leftNavDrawerActivity = (HomeActivity) getActivity();
                        leftNavDrawerActivity.showBottomNavigation();
                    }
                }
            }
        });
    }

    public void unRegisterKeyboardVisibilityListener() {
        KeyboardVisibility.removeAllKeyboardToggleListeners();
    }
}