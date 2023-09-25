package com.krishig.android.ui.home.fragments.home.SubProductDetail.view;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.core.widget.NestedScrollView;

import com.krishig.android.Navigator.activity.ActivityNavigator;
import com.krishig.android.Navigator.fragment.FragmentNavigator;
import com.krishig.android.R;
import com.krishig.android.data.local.sharedpreferences.SharedPreferencesHelper;
import com.krishig.android.data.remote.ApiResponseObject;
import com.krishig.android.data.remote.api.ApiService;
import com.krishig.android.data.remote.helper.RetrofitClient;
import com.krishig.android.databinding.ActivitySubCategoryAndProductBinding;
import com.krishig.android.model.Product;
import com.krishig.android.ui.AppConstants;
import com.krishig.android.ui.base.BaseActivity;
import com.krishig.android.ui.home.fragments.ProductDetail.view.ProductDetailActivity;
import com.krishig.android.ui.home.fragments.home.adapter.TopSeedsViewAdapter;
import com.krishig.android.ui.home.fragments.home.viewmodel.HomeFragmentViewModel;
import com.library.adapter.recyclerview.LayoutManagerUtils;
import com.library.adapter.recyclerview.listener.OnRecyclerViewItemChildClick;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class SubCategoryAndProductActivity extends BaseActivity<ActivitySubCategoryAndProductBinding> {

    HomeFragmentViewModel viewModel;
    @Inject
    SharedPreferencesHelper sharedPreferencesHelper;
    String subCategoryId = "", brandId = "",subCategoryName = "";
    TopSeedsViewAdapter topSeedsViewAdapter;
    ArrayList<Product.Result> seedArrayList = new ArrayList<>();
    private ApiService apiService;

    int totalPages = 0, count = 1;
    String itemPerPage = "10";

    @Override
    protected ActivitySubCategoryAndProductBinding getViewBinding() {
        return ActivitySubCategoryAndProductBinding.inflate(getLayoutInflater());
    }

    @Override
    protected ActivityNavigator getActivityNavigator() {
        return new ActivityNavigator(SubCategoryAndProductActivity.this);
    }

    @Override
    protected FragmentNavigator getFragmentNavigator() {
        return null;
    }

    @Override
    protected void initializeViewModel() {
        viewModel = createViewModel(HomeFragmentViewModel.class);

    }

    @Override
    protected void initializeToolBar() {

    }

    @Override
    protected void initializeObject() {
        setSeedRecyclerView();

        if (getIntent().getExtras() != null) {
            subCategoryId = getIntent().getExtras().getString(AppConstants.Extras.SUB_CATEGORY_ID);
            brandId = getIntent().getExtras().getString(AppConstants.Extras.PRODUCT_NAME_ID);
            subCategoryName = getIntent().getExtras().getString(AppConstants.Extras.SUB_CATEGORY_NAME);
            if(subCategoryName!=null){
                viewBinding.subCategoryTextView.setText(subCategoryName);
            }
            getProductFilter(subCategoryId, brandId, itemPerPage, String.valueOf(count), "application/json", "application/json",
                    sharedPreferencesHelper.getKeyToken());
            showProgressDialog();
        }
    }

    @Override
    protected void addTextChangedListener() {

    }

    @Override
    protected void setOnFocusChangeListener() {

    }

    @Override
    protected void observeViewModel() {

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
                        getProductFilter(subCategoryId, brandId, itemPerPage, String.valueOf(count), "application/json", "application/json",
                                sharedPreferencesHelper.getKeyToken());
                        //showProgressDialog();
                    }

                }
            }
        });

        viewBinding.icBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        topSeedsViewAdapter.setOnRecyclerViewItemChildClick(new OnRecyclerViewItemChildClick<Product.Result>() {
            @Override
            public void OnItemChildClick(View viewChild, Product.Result result, int position) {
                switch (viewChild.getId()) {
                    case R.id.addSeedMaterialCardView:
                        Intent intent = new Intent(SubCategoryAndProductActivity.this, ProductDetailActivity.class);
                        intent.putExtra(AppConstants.Extras.PRODUCT_ID, result.getId());
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

    private void setSeedRecyclerView() {
        viewBinding.seedsRecyclerView.setHasFixedSize(true);
        viewBinding.seedsRecyclerView.setLayoutManager(LayoutManagerUtils.getGridLayoutManagerVertical(this, 2));
        topSeedsViewAdapter = new TopSeedsViewAdapter(this);
        topSeedsViewAdapter.addArrayList(seedArrayList);
        viewBinding.seedsRecyclerView.setAdapter(topSeedsViewAdapter);
    }

    private void getProductFilter(String subCategoryId, String brandId, String itemPerPage, String pageNumber, String accept, String authorisation, String token) {
        apiService = RetrofitClient.getRetrofitInstance2().create(ApiService.class);
        // Example API call
        Call<ApiResponseObject<Product>> call = apiService.productFilter(subCategoryId, brandId, itemPerPage, pageNumber,
                accept, authorisation, token);
        call.enqueue(new Callback<ApiResponseObject<Product>>() {
            @Override
            public void onResponse(Call<ApiResponseObject<Product>> call, Response<ApiResponseObject<Product>> response) {
                ApiResponseObject<Product> categories = response.body();
                hideProgressDialog();
                viewBinding.idPBLoading.setVisibility(View.GONE);
                if (categories != null) {
                    int size = categories.getData().getResult().size();
                    if (size == 0) {
                        viewBinding.seedsRecyclerView.setVisibility(View.GONE);
                        viewBinding.errorImageView.setVisibility(View.VISIBLE);
                        viewBinding.errorTextView.setVisibility(View.VISIBLE);
                    } else {
                        viewBinding.seedsRecyclerView.setVisibility(View.VISIBLE);
                        viewBinding.errorImageView.setVisibility(View.GONE);
                        viewBinding.errorTextView.setVisibility(View.GONE);
                        totalPages = categories.getData().getTotalPages();
                        seedArrayList = categories.getData().getResult();
                        topSeedsViewAdapter.clearAllItem();
                        topSeedsViewAdapter.replaceArrayList(seedArrayList);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponseObject<Product>> call, Throwable t) {
                // Handle network or API call failure
                Log.e("", "onFailure  =" + call.toString());
                Log.e("", "onFailure  =" + t);

            }
        });
    }


}