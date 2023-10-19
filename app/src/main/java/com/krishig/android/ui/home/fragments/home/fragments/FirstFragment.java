package com.krishig.android.ui.home.fragments.home.fragments;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.krishig.android.Navigator.activity.ActivityNavigator;
import com.krishig.android.Navigator.fragment.FragmentNavigator;
import com.krishig.android.R;
import com.krishig.android.data.local.sharedpreferences.SharedPreferencesHelper;
import com.krishig.android.data.remote.ApiResponseArray;
import com.krishig.android.data.remote.ApiResponseObject;
import com.krishig.android.data.remote.api.ApiService;
import com.krishig.android.data.remote.helper.RetrofitClient;
import com.krishig.android.databinding.FragmentFirstBinding;
import com.krishig.android.model.Category;
import com.krishig.android.model.Market;
import com.krishig.android.model.Product;
import com.krishig.android.model.ProductBrands;
import com.krishig.android.model.SubCategory;
import com.krishig.android.ui.AppConstants;
import com.krishig.android.ui.base.BaseFragment;
import com.krishig.android.ui.home.fragments.home.SubProductDetail.view.SubCategoryAndProductActivity;
import com.krishig.android.ui.home.fragments.home.adapter.CategoryRecyclerViewAdapter;
import com.krishig.android.ui.home.fragments.home.adapter.TopBrandsViewAdapter;
import com.krishig.android.ui.home.fragments.home.adapter.TopSeedsViewAdapter;
import com.krishig.android.ui.home.fragments.home.viewmodel.SubCategoryModel;
import com.krishig.android.ui.home.view.HomeActivity;
import com.krishig.android.ui.seeAllSubCategory.SeeAllSubCategoryActivity;
import com.library.adapter.recyclerview.listener.OnRecyclerViewItemChildClick;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class FirstFragment extends BaseFragment<FragmentFirstBinding> {
    CategoryRecyclerViewAdapter categoryRecyclerViewAdapter;
    TopBrandsViewAdapter topBrandsViewAdapter;
    TopSeedsViewAdapter topSeedsViewAdapter;
    ArrayList<Market> homeArrayList = new ArrayList<>();
    ArrayList<ProductBrands.Result> topBrandArrayList = new ArrayList<>();
    ArrayList<Product.Result> seedArrayList = new ArrayList<>();
    SubCategoryModel viewModel;

    ArrayList<SubCategory.Result> arrayList = new ArrayList<>();

    ArrayList<ProductBrands.Result> topBrandArrayList2 = new ArrayList<>();
    String categoryId = "";


    @Inject
    SharedPreferencesHelper sharedPreferencesHelper;
    private ApiService apiService;


    @Override
    protected ActivityNavigator getActivityNavigator() {
        return new ActivityNavigator(getActivity());
    }

    @Override
    protected FragmentNavigator getFragmentNavigator() {
        return null;
    }

    @Override
    protected FragmentFirstBinding getViewBinding() {
        return FragmentFirstBinding.inflate(getLayoutInflater());
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
        setCategoryRecyclerView();


    }

    private void setCategoryRecyclerView() {
        viewBinding.homeRecyclerView.setHasFixedSize(true);
        viewBinding.homeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryRecyclerViewAdapter = new CategoryRecyclerViewAdapter(getContext());
        categoryRecyclerViewAdapter.addArrayList(arrayList);
        viewBinding.homeRecyclerView.setAdapter(categoryRecyclerViewAdapter);
    }


    @Override
    protected void addTextChangedListener() {

    }

    @Override
    protected void setOnFocusChangeListener() {

    }

    @Override
    protected void observeViewModel() {
        Observer<String> getCategoryUserError = new Observer<String>() {
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
        viewModel.getCategoryUserError().observe(this, getCategoryUserError);

        final Observer<ArrayList<Category>> getCategoryUserSuccess = new Observer<ArrayList<Category>>() {
            @Override
            public void onChanged(ArrayList<Category> categories) {
                hideProgressDialog();
                String categoryId = "";
                for (int i = 0; i < categories.size(); i++) {
                    if (categories.get(i).getCategory_name().equalsIgnoreCase("seed")) {
                        categoryId = categories.get(i).getId();
                    }
                }
              /*  viewModel.getSubCategories(categoryId, "application/json", "application/json",
                        sharedPreferencesHelper.getKeyToken());
                showProgressDialog();

                viewModel.productFilter(categoryId, "application/json", "application/json",
                        sharedPreferencesHelper.getKeyToken());
                showProgressDialog();*/
            }
        };
        viewModel.getCategoryUserSuccess().observe(this, getCategoryUserSuccess);


        Observer<String> getSubCategoryUserError = new Observer<String>() {
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
        viewModel.getSubCategoryUserError().observe(this, getSubCategoryUserError);

        final Observer<SubCategory> getSubCategoryUserSuccess = new Observer<SubCategory>() {
            @Override
            public void onChanged(SubCategory categories) {
                hideProgressDialog();
                int size = categories.getResultArrayList().size();
                if (size == 0) {
                    viewBinding.homeRecyclerView.setVisibility(View.GONE);
                    viewBinding.errorImageView.setVisibility(View.VISIBLE);
                    viewBinding.errorTextView.setVisibility(View.VISIBLE);
                } else {
                    viewBinding.homeRecyclerView.setVisibility(View.VISIBLE);
                    viewBinding.errorImageView.setVisibility(View.GONE);
                    viewBinding.errorTextView.setVisibility(View.GONE);
                    arrayList.clear();
                    SubCategory.Result resultArrayList = null;
                    if (size >= 10) {
                        for (int i = 0; i < 10; i++) {
                            resultArrayList = categories.getResultArrayList().get(i);
                            arrayList.add(resultArrayList);
                        }
                    } else {
                        for (int i = 0; i < size; i++) {
                            resultArrayList = categories.getResultArrayList().get(i);
                            arrayList.add(resultArrayList);
                        }
                    }

                    categoryRecyclerViewAdapter.clearAllItem();
                    categoryRecyclerViewAdapter.addArrayList(arrayList);
                }

            }
        };
        viewModel.getSubCategoryUserSuccess().observe(this, getSubCategoryUserSuccess);


/*        Observer<String> getProductBrandsUserError = new Observer<String>() {
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
        viewModel.getProductBrandsUserError().observe(this, getProductBrandsUserError);

        final Observer<ProductBrands> getProductBrandsUserSuccess = new Observer<ProductBrands>() {
            @Override
            public void onChanged(ProductBrands categories) {
                hideProgressDialog();
                int size = categories.getResultArrayList().size();
                if (size == 0) {
                    viewBinding.topBrandRecyclerView.setVisibility(View.GONE);
                    viewBinding.errorImageViewTopBrands.setVisibility(View.VISIBLE);
                    viewBinding.errorTextViewTopBrands.setVisibility(View.VISIBLE);
                } else {
                    viewBinding.topBrandRecyclerView.setVisibility(View.VISIBLE);
                    viewBinding.errorImageViewTopBrands.setVisibility(View.GONE);
                    viewBinding.errorTextViewTopBrands.setVisibility(View.GONE);
                    topBrandArrayList = categories.getResultArrayList();
                    topBrandsViewAdapter.clearAllItem();
                    topBrandsViewAdapter.addArrayList(topBrandArrayList);
                }

            }
        };
        viewModel.getProductBrandsUserSuccess().observe(this, getProductBrandsUserSuccess);


        //filter
        Observer<String> getFilterSearchUserError = new Observer<String>() {
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
        viewModel.getFilterSearchUserError().observe(this, getFilterSearchUserError);

        final Observer<Product> getFilterSearchUserSuccess = new Observer<Product>() {
            @Override
            public void onChanged(Product categories) {
                hideProgressDialog();
                if (categories.getResult().size() == 0) {
                    viewBinding.seedsRecyclerView.setVisibility(View.GONE);
                    viewBinding.errorImageViewSeeds.setVisibility(View.VISIBLE);
                    viewBinding.errorTextViewSeeds.setVisibility(View.VISIBLE);
                } else {
                    viewBinding.seedsRecyclerView.setVisibility(View.VISIBLE);
                    viewBinding.errorImageViewSeeds.setVisibility(View.GONE);
                    viewBinding.errorTextViewSeeds.setVisibility(View.GONE);
                    seedArrayList = categories.getResult();
                    topSeedsViewAdapter.clearAllItem();
                    topSeedsViewAdapter.replaceArrayList(seedArrayList);
                }

            }
        };
        viewModel.getFilterSearchUserSuccess().observe(this, getFilterSearchUserSuccess);*/
    }

    @Override
    protected void setOnClickListener() {
        categoryRecyclerViewAdapter.setOnRecyclerViewItemChildClick(new OnRecyclerViewItemChildClick<SubCategory.Result>() {
            @Override
            public void OnItemChildClick(View viewChild, SubCategory.Result market, int position) {
                switch (viewChild.getId()) {
                    case R.id.addMaterialCardView:
                        Intent intent = new Intent(getActivity(), SubCategoryAndProductActivity.class);
                        intent.putExtra(AppConstants.Extras.SUB_CATEGORY_ID, market.getId());
                        intent.putExtra(AppConstants.Extras.SUB_CATEGORY_NAME, market.getSubCategoryName());
                        startActivity(intent);
                        break;
                    default:
                        break;

                }
            }
        });

        viewBinding.seeAllSubCategoryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SeeAllSubCategoryActivity.class);
                intent.putExtra(AppConstants.Extras.CATEGORY_ID, categoryId);
                intent.putExtra(AppConstants.Extras.FROM_SUB_CATEGORY, "Seed");
                startActivity(intent);
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
        System.out.println("===========Resume============");
        boolean check = false;
        if (sharedPreferencesHelper.getCustomerCartId() == null) {
            check = true;
        } else {
            check = false;
        }
        if (getActivity() instanceof HomeActivity) {
            HomeActivity leftNavDrawerActivity = (HomeActivity) getActivity();
            leftNavDrawerActivity.setFooterCheck(check);
            Log.e("", "HomeFragmentWalletVisibility" + check);
        }
        getCategories("application/json", "application/json",
                sharedPreferencesHelper.getKeyToken());
        showProgressDialog();

   /*     getProductBrands("application/json", "application/json",
                sharedPreferencesHelper.getKeyToken());
        showProgressDialog();*/
    }


    private void getCategories(String accept, String authorisation, String token) {
        apiService = RetrofitClient.getRetrofitInstance2().create(ApiService.class);
        // Example API call
        Call<ApiResponseArray<Category>> call = apiService.getCategories(accept, authorisation, token);
        call.enqueue(new Callback<ApiResponseArray<Category>>() {
            @Override
            public void onResponse(Call<ApiResponseArray<Category>> call, Response<ApiResponseArray<Category>> response) {
                ApiResponseArray<Category> categories = response.body();
                hideProgressDialog();
                if (response.body() != null) {
                    if (categories.getData() != null) {
                        for (int i = 0; i < categories.getData().size(); i++) {
                            if (categories.getData().get(i).getCategory_name().equalsIgnoreCase("seeds")) {
                                categoryId = categories.getData().get(i).getId();
                                Log.e("", "1==" + categories.getData().get(i).getCategory_name());
                            }
                            Log.e("", "2==" + categories.getData().get(i).getCategory_name());
                        }
                        getSubCategory(categoryId, "application/json", "application/json",
                                sharedPreferencesHelper.getKeyToken());
                        showProgressDialog();

               /*     getProductFilter(categoryId, "application/json", "application/json",
                            sharedPreferencesHelper.getKeyToken());
                    showProgressDialog();*/

                    }else{
                        viewBinding.homeRecyclerView.setVisibility(View.GONE);
                        viewBinding.errorImageView.setVisibility(View.VISIBLE);
                        viewBinding.errorTextView.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onFailure(Call<ApiResponseArray<Category>> call, Throwable t) {
                // Handle network or API call failure
                Log.e("", "onFailure  =" + call.toString());
                Log.e("", "onFailure  =" + t);

            }
        });
    }

    private void getSubCategory(String categoryId, String accept, String authorisation, String token) {
        apiService = RetrofitClient.getRetrofitInstance2().create(ApiService.class);
        // Example API call
        Call<ApiResponseObject<SubCategory>> call = apiService.getSubCategories(categoryId, accept, authorisation, token);
        call.enqueue(new Callback<ApiResponseObject<SubCategory>>() {
            @Override
            public void onResponse(Call<ApiResponseObject<SubCategory>> call, Response<ApiResponseObject<SubCategory>> response) {
                ApiResponseObject<SubCategory> categories = response.body();
                hideProgressDialog();
                if (categories.getData() != null) {
                    int size = categories.getData().getResultArrayList().size();
                    if (size == 0) {
                        viewBinding.homeRecyclerView.setVisibility(View.GONE);
                        viewBinding.errorImageView.setVisibility(View.VISIBLE);
                        viewBinding.errorTextView.setVisibility(View.VISIBLE);
                    } else {
                        viewBinding.homeRecyclerView.setVisibility(View.VISIBLE);
                        viewBinding.errorImageView.setVisibility(View.GONE);
                        viewBinding.errorTextView.setVisibility(View.GONE);
                        arrayList.clear();
                        SubCategory.Result resultArrayList = null;
                        if (size >= 10) {
                            for (int i = 0; i < 10; i++) {
                                resultArrayList = categories.getData().getResultArrayList().get(i);
                                arrayList.add(resultArrayList);
                            }
                        } else {
                            for (int i = 0; i < size; i++) {
                                resultArrayList = categories.getData().getResultArrayList().get(i);
                                arrayList.add(resultArrayList);
                            }
                        }
                        categoryRecyclerViewAdapter.clearAllItem();
                        categoryRecyclerViewAdapter.addArrayList(arrayList);
                    }
                }else{
                    viewBinding.homeRecyclerView.setVisibility(View.GONE);
                    viewBinding.errorImageView.setVisibility(View.VISIBLE);
                    viewBinding.errorTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ApiResponseObject<SubCategory>> call, Throwable t) {
                // Handle network or API call failure
                Log.e("", "onFailure  =" + call.toString());
                Log.e("", "onFailure  =" + t);

            }
        });
    }
/*
    private void getProductBrands(String accept,String authorisation,String token) {
        apiService = RetrofitClient.getRetrofitInstance2().create(ApiService.class);
        // Example API call
        Call<ApiResponseObject<ProductBrands>> call = apiService.getProductBrands(accept,authorisation,token);
        call.enqueue(new Callback<ApiResponseObject<ProductBrands>>() {
            @Override
            public void onResponse(Call<ApiResponseObject<ProductBrands>> call, Response<ApiResponseObject<ProductBrands>> response) {
                ApiResponseObject<ProductBrands> categories = response.body();
                hideProgressDialog();
                if (categories != null) {
                    int size = categories.getData().getResultArrayList().size();
                    if (size == 0) {
                        viewBinding.topBrandRecyclerView.setVisibility(View.GONE);
                        viewBinding.errorImageViewTopBrands.setVisibility(View.VISIBLE);
                        viewBinding.errorTextViewTopBrands.setVisibility(View.VISIBLE);
                    } else {
                        viewBinding.topBrandRecyclerView.setVisibility(View.VISIBLE);
                        viewBinding.errorImageViewTopBrands.setVisibility(View.GONE);
                        viewBinding.errorTextViewTopBrands.setVisibility(View.GONE);
                        // topBrandArrayList = categories.getData().getResultArrayList();

                        topBrandArrayList.clear();
                        ProductBrands.Result resultArrayList = null;
                        if (size >= 8) {
                            for (int i = 0; i < 8; i++) {
                                resultArrayList = categories.getData().getResultArrayList().get(i);
                                topBrandArrayList.add(resultArrayList);
                            }
                        } else {
                            for (int i = 0; i < size; i++) {
                                resultArrayList = categories.getData().getResultArrayList().get(i);
                                topBrandArrayList.add(resultArrayList);
                            }
                        }

                        topBrandsViewAdapter.clearAllItem();
                        topBrandsViewAdapter.addArrayList(topBrandArrayList);
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

    private void getProductFilter(String categoryId,String accept,String authorisation,String token) {
        apiService = RetrofitClient.getRetrofitInstance2().create(ApiService.class);
        // Example API call
        Call<ApiResponseObject<Product>> call = apiService.productFilter(categoryId,accept,authorisation,token);
        call.enqueue(new Callback<ApiResponseObject<Product>>() {
            @Override
            public void onResponse(Call<ApiResponseObject<Product>> call, Response<ApiResponseObject<Product>> response) {
                ApiResponseObject<Product> categories = response.body();
                hideProgressDialog();
                if (categories != null) {
                    int size = categories.getData().getResult().size();
                    if (size == 0) {
                        viewBinding.seedsRecyclerView.setVisibility(View.GONE);
                        viewBinding.errorImageViewSeeds.setVisibility(View.VISIBLE);
                        viewBinding.errorTextViewSeeds.setVisibility(View.VISIBLE);
                    } else {
                        viewBinding.seedsRecyclerView.setVisibility(View.VISIBLE);
                        viewBinding.errorImageViewSeeds.setVisibility(View.GONE);
                        viewBinding.errorTextViewSeeds.setVisibility(View.GONE);
                       // seedArrayList = categories.getData().getResult();

                        seedArrayList.clear();
                        Product.Result resultArrayList = null;
                        if (size >= 8) {
                            for (int i = 0; i < 8; i++) {
                                resultArrayList = categories.getData().getResult().get(i);
                                seedArrayList.add(resultArrayList);
                            }
                        } else {
                            for (int i = 0; i < size; i++) {
                                resultArrayList = categories.getData().getResult().get(i);
                                seedArrayList.add(resultArrayList);
                            }
                        }
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
    }*/

}