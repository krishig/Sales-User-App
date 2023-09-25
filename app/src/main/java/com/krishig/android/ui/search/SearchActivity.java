package com.krishig.android.ui.search;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.krishig.android.Navigator.activity.ActivityNavigator;
import com.krishig.android.Navigator.fragment.FragmentNavigator;
import com.krishig.android.R;
import com.krishig.android.data.local.sharedpreferences.SharedPreferencesHelper;
import com.krishig.android.data.remote.ApiResponseObject;
import com.krishig.android.data.remote.api.ApiService;
import com.krishig.android.data.remote.helper.RetrofitClient;
import com.krishig.android.databinding.ActivitySearchBinding;
import com.krishig.android.model.SubCategory;
import com.krishig.android.ui.AppConstants;
import com.krishig.android.ui.base.BaseActivity;
import com.krishig.android.ui.home.fragments.home.SubProductDetail.view.SubCategoryAndProductActivity;
import com.krishig.android.ui.home.fragments.home.adapter.CategoryRecyclerViewAdapter;
import com.krishig.android.ui.home.fragments.home.viewmodel.SubCategoryModel;
import com.library.adapter.recyclerview.LayoutManagerUtils;
import com.library.adapter.recyclerview.listener.OnRecyclerViewItemChildClick;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class SearchActivity extends BaseActivity<ActivitySearchBinding> {
    CategoryRecyclerViewAdapter categoryRecyclerViewAdapter;
    ArrayList<SubCategory.Result> arrayList = new ArrayList<>();
    SubCategoryModel viewModel;

    @Inject
    SharedPreferencesHelper sharedPreferencesHelper;
    private ApiService apiService;

    @Override
    protected ActivitySearchBinding getViewBinding() {
        return ActivitySearchBinding.inflate(getLayoutInflater());
    }

    @Override
    protected ActivityNavigator getActivityNavigator() {
        return new ActivityNavigator(SearchActivity.this);
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
        setSeedRecyclerView();
        viewBinding.seedsRecyclerView.setVisibility(View.GONE);
        viewBinding.errorImageViewSeeds.setVisibility(View.VISIBLE);
        viewBinding.errorTextViewSeeds.setVisibility(View.VISIBLE);
    }

    private void setSeedRecyclerView() {
        viewBinding.seedsRecyclerView.setHasFixedSize(true);
        viewBinding.seedsRecyclerView.setLayoutManager(LayoutManagerUtils.getGridLayoutManagerVertical(this, 5));
        categoryRecyclerViewAdapter = new CategoryRecyclerViewAdapter(this);
        categoryRecyclerViewAdapter.addArrayList(arrayList);
        viewBinding.seedsRecyclerView.setAdapter(categoryRecyclerViewAdapter);
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
                    viewBinding.seedsRecyclerView.setVisibility(View.GONE);
                    viewBinding.errorImageViewSeeds.setVisibility(View.VISIBLE);
                    viewBinding.errorTextViewSeeds.setVisibility(View.VISIBLE);
                } else {
                    viewBinding.seedsRecyclerView.setVisibility(View.VISIBLE);
                    viewBinding.errorImageViewSeeds.setVisibility(View.GONE);
                    viewBinding.errorTextViewSeeds.setVisibility(View.GONE);
                    subCategorySearch(editable.toString(), "application/json", "application/json",
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


    }

    @Override
    protected void setOnClickListener() {


        categoryRecyclerViewAdapter.setOnRecyclerViewItemChildClick(new OnRecyclerViewItemChildClick<SubCategory.Result>() {
            @Override
            public void OnItemChildClick(View viewChild, SubCategory.Result market, int position) {
                switch (viewChild.getId()) {
                    case R.id.addMaterialCardView:
                        Intent intent = new Intent(SearchActivity.this, SubCategoryAndProductActivity.class);
                        intent.putExtra(AppConstants.Extras.SUB_CATEGORY_ID, market.getId());
                        intent.putExtra(AppConstants.Extras.SUB_CATEGORY_NAME, market.getSubCategoryName());
                        startActivity(intent);
                        break;
                    default:
                        break;

                }
            }
        });

        viewBinding.icBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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

    private void subCategorySearch(String search_sub_category, String accept, String authorisation, String token) {
        apiService = RetrofitClient.getRetrofitInstance2().create(ApiService.class);
        // Example API call
        Call<ApiResponseObject<SubCategory>> call = apiService.subCategorySearch(search_sub_category, accept, authorisation, token);
        call.enqueue(new Callback<ApiResponseObject<SubCategory>>() {
            @Override
            public void onResponse(Call<ApiResponseObject<SubCategory>> call, Response<ApiResponseObject<SubCategory>> response) {
                ApiResponseObject<SubCategory> categories = response.body();
                hideProgressDialog();
                if (categories != null) {
                    int size = categories.getData().getResultArrayList().size();
                    if (size == 0) {
                        viewBinding.seedsRecyclerView.setVisibility(View.GONE);
                        viewBinding.errorImageViewSeeds.setVisibility(View.VISIBLE);
                        viewBinding.errorTextViewSeeds.setVisibility(View.VISIBLE);
                    } else {
                        viewBinding.seedsRecyclerView.setVisibility(View.VISIBLE);
                        viewBinding.errorImageViewSeeds.setVisibility(View.GONE);
                        viewBinding.errorTextViewSeeds.setVisibility(View.GONE);
                        arrayList = categories.getData().getResultArrayList();
                        categoryRecyclerViewAdapter.clearAllItem();
                        categoryRecyclerViewAdapter.replaceArrayList(arrayList);
                    }
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


}