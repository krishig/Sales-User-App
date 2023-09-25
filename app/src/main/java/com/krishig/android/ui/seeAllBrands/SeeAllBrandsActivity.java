package com.krishig.android.ui.seeAllBrands;

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
import com.krishig.android.databinding.ActivitySeeAllBrandsBinding;
import com.krishig.android.model.ProductBrands;
import com.krishig.android.ui.AppConstants;
import com.krishig.android.ui.base.BaseActivity;
import com.krishig.android.ui.home.fragments.home.SubProductDetail.view.SubCategoryAndProductActivity;
import com.krishig.android.ui.seeAllBrands.adapter.SeeAllTopBrandsViewAdapter;
import com.library.adapter.recyclerview.LayoutManagerUtils;
import com.library.adapter.recyclerview.listener.OnRecyclerViewItemChildClick;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class SeeAllBrandsActivity extends BaseActivity<ActivitySeeAllBrandsBinding> {
    SeeAllTopBrandsViewAdapter topBrandsViewAdapter;
    ArrayList<ProductBrands.Result> topBrandArrayList = new ArrayList<>();
    ArrayList<ProductBrands.Result> DataArrayList = new ArrayList<>();

    @Inject
    SharedPreferencesHelper sharedPreferencesHelper;
    private ApiService apiService;

    int totalPages = 0, count = 1;
    String itemPerPageInProduct = "40";
    String categoryId = "";

    @Override
    protected ActivitySeeAllBrandsBinding getViewBinding() {
        return ActivitySeeAllBrandsBinding.inflate(getLayoutInflater());
    }

    @Override
    protected ActivityNavigator getActivityNavigator() {
        return new ActivityNavigator(SeeAllBrandsActivity.this);
    }

    @Override
    protected FragmentNavigator getFragmentNavigator() {
        return null;
    }

    @Override
    protected void initializeViewModel() {

    }

    @Override
    protected void initializeToolBar() {
        setSeedRecyclerView();
    }

    @Override
    protected void initializeObject() {

    }

    private void setSeedRecyclerView() {
        viewBinding.seedsRecyclerView.setHasFixedSize(true);
        viewBinding.seedsRecyclerView.setLayoutManager(LayoutManagerUtils.getGridLayoutManagerVertical(this, 4));
        topBrandsViewAdapter = new SeeAllTopBrandsViewAdapter(this);
        topBrandsViewAdapter.addArrayList(topBrandArrayList);
        viewBinding.seedsRecyclerView.setAdapter(topBrandsViewAdapter);
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
        viewBinding.icBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        topBrandsViewAdapter.setOnRecyclerViewItemChildClick(new OnRecyclerViewItemChildClick<ProductBrands.Result>() {
            @Override
            public void OnItemChildClick(View viewChild, ProductBrands.Result market, int position) {
                switch (viewChild.getId()) {
                    case R.id.addMaterialCardView:
                        Intent intent = new Intent(SeeAllBrandsActivity.this, SubCategoryAndProductActivity.class);
                        intent.putExtra(AppConstants.Extras.PRODUCT_NAME_ID, market.getId());
                        intent.putExtra(AppConstants.Extras.SUB_CATEGORY_NAME, market.getBrandName());
                        startActivity(intent);
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
                        if (categoryId != null) {
                            getProductBrands(itemPerPageInProduct, String.valueOf(count), "application/json", "application/json",
                                    sharedPreferencesHelper.getKeyToken());
                            showProgressDialog();
                        }
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
    public void onResume() {
        super.onResume();
        DataArrayList.clear();
        count = 1;
        System.out.println("===========Resume============");

        getProductBrands(itemPerPageInProduct, String.valueOf(count), "application/json", "application/json",
                sharedPreferencesHelper.getKeyToken());
        showProgressDialog();

    }

    private void getProductBrands(String items_per_page,
                                  String page_number, String accept, String authorisation, String token) {
        apiService = RetrofitClient.getRetrofitInstance2().create(ApiService.class);
        // Example API call
        Call<ApiResponseObject<ProductBrands>> call = apiService.getProductBrands(items_per_page, page_number, accept, authorisation, token);
        call.enqueue(new Callback<ApiResponseObject<ProductBrands>>() {
            @Override
            public void onResponse(Call<ApiResponseObject<ProductBrands>> call, Response<ApiResponseObject<ProductBrands>> response) {
                ApiResponseObject<ProductBrands> categories = response.body();
                hideProgressDialog();
                if (categories != null) {
                    viewBinding.idPBLoading.setVisibility(View.GONE);
                    int size = categories.getData().getResultArrayList().size();
                    if (size == 0) {
                        viewBinding.seedsRecyclerView.setVisibility(View.GONE);
                        viewBinding.errorImageViewSeeds.setVisibility(View.VISIBLE);
                        viewBinding.errorTextViewSeeds.setVisibility(View.VISIBLE);
                    } else {
                        totalPages = categories.getData().getTotal_pages();
                        viewBinding.seedsRecyclerView.setVisibility(View.VISIBLE);
                        viewBinding.errorImageViewSeeds.setVisibility(View.GONE);
                        viewBinding.errorTextViewSeeds.setVisibility(View.GONE);
                        topBrandArrayList = categories.getData().getResultArrayList();
                        DataArrayList.addAll(topBrandArrayList);
                        topBrandsViewAdapter.clearAllItem();
                        topBrandsViewAdapter.addArrayList(DataArrayList);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponseObject<ProductBrands>> call, Throwable t) {
                // Handle network or API call failure
                Log.e("", "onFailure  =" + call.toString());
                Log.e("", "onFailure  =" + t);

            }
        });
    }

}