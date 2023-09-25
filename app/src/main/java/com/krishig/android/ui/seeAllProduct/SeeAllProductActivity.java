package com.krishig.android.ui.seeAllProduct;

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
import com.krishig.android.databinding.ActivitySeeAllProductBinding;
import com.krishig.android.model.Product;
import com.krishig.android.ui.AppConstants;
import com.krishig.android.ui.base.BaseActivity;
import com.krishig.android.ui.home.fragments.ProductDetail.view.ProductDetailActivity;
import com.krishig.android.ui.home.fragments.home.adapter.TopSeedsViewAdapter;
import com.library.adapter.recyclerview.LayoutManagerUtils;
import com.library.adapter.recyclerview.listener.OnRecyclerViewItemChildClick;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class SeeAllProductActivity extends BaseActivity<ActivitySeeAllProductBinding> {
    ArrayList<Product.Result> seedArrayList = new ArrayList<>();
    ArrayList<Product.Result> DataArrayList = new ArrayList<>();
    TopSeedsViewAdapter topSeedsViewAdapter;
    @Inject
    SharedPreferencesHelper sharedPreferencesHelper;
    private ApiService apiService;

    int totalPages = 0, count = 1;
    String itemPerPageInProduct = "20";
    String categoryId = "";
    String from = "";


    @Override
    protected ActivitySeeAllProductBinding getViewBinding() {
        return ActivitySeeAllProductBinding.inflate(getLayoutInflater());
    }

    @Override
    protected ActivityNavigator getActivityNavigator() {
        return new ActivityNavigator(SeeAllProductActivity.this);
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

    }

    @Override
    protected void initializeObject() {
        setSeedRecyclerView();
        if (getIntent().getExtras() != null) {
            categoryId = getIntent().getExtras().getString(AppConstants.Extras.CATEGORY_ID);
            from = getIntent().getExtras().getString(AppConstants.Extras.FROM_PRODUCT);
            viewBinding.subCategoryTextView.setText(from);
        }
    }

    private void setSeedRecyclerView() {
        viewBinding.seedsRecyclerView.setHasFixedSize(true);
        viewBinding.seedsRecyclerView.setLayoutManager(LayoutManagerUtils.getGridLayoutManagerVertical(this, 2));
        topSeedsViewAdapter = new TopSeedsViewAdapter(this);
        topSeedsViewAdapter.addArrayList(seedArrayList);
        viewBinding.seedsRecyclerView.setAdapter(topSeedsViewAdapter);
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

        viewBinding.nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    ++count;
                    if (count <= totalPages) {
                        viewBinding.idPBLoading.setVisibility(View.VISIBLE);
                        if (categoryId != null) {
                            getProductFilter(itemPerPageInProduct, String.valueOf(count), categoryId, "application/json", "application/json",
                                    sharedPreferencesHelper.getKeyToken());
                            showProgressDialog();
                        }
                    }

                }
            }
        });

        topSeedsViewAdapter.setOnRecyclerViewItemChildClick(new OnRecyclerViewItemChildClick<Product.Result>() {
            @Override
            public void OnItemChildClick(View viewChild, Product.Result result, int position) {
                switch (viewChild.getId()) {
                    case R.id.addSeedMaterialCardView:
                        Intent intent = new Intent(SeeAllProductActivity.this, ProductDetailActivity.class);
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


    @Override
    public void onResume() {
        super.onResume();
        DataArrayList.clear();
        count = 1;
        System.out.println("===========Resume============");
        if (categoryId != null) {
            getProductFilter(itemPerPageInProduct, String.valueOf(count), categoryId, "application/json", "application/json",
                    sharedPreferencesHelper.getKeyToken());
            showProgressDialog();
        }
    }

    private void getProductFilter(String items_per_page,
                                  String page_number, String categoryId, String accept, String authorisation, String token) {
        apiService = RetrofitClient.getRetrofitInstance2().create(ApiService.class);
        // Example API call
        Call<ApiResponseObject<Product>> call = apiService.productFilter(items_per_page, page_number, categoryId, accept, authorisation, token);
        call.enqueue(new Callback<ApiResponseObject<Product>>() {
            @Override
            public void onResponse(Call<ApiResponseObject<Product>> call, Response<ApiResponseObject<Product>> response) {
                ApiResponseObject<Product> categories = response.body();
                hideProgressDialog();
                if (categories != null) {
                    viewBinding.idPBLoading.setVisibility(View.GONE);
                    int size = categories.getData().getResult().size();
                    if (size == 0) {
                        viewBinding.seedsRecyclerView.setVisibility(View.GONE);
                        viewBinding.errorImageViewSeeds.setVisibility(View.VISIBLE);
                        viewBinding.errorTextViewSeeds.setVisibility(View.VISIBLE);
                    } else {
                        totalPages = categories.getData().getTotalPages();
                        viewBinding.seedsRecyclerView.setVisibility(View.VISIBLE);
                        viewBinding.errorImageViewSeeds.setVisibility(View.GONE);
                        viewBinding.errorTextViewSeeds.setVisibility(View.GONE);
                        seedArrayList = categories.getData().getResult();
                        DataArrayList.addAll(seedArrayList);
                        topSeedsViewAdapter.clearAllItem();
                        topSeedsViewAdapter.replaceArrayList(DataArrayList);
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