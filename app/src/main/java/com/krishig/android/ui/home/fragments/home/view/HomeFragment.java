package com.krishig.android.ui.home.fragments.home.view;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.krishig.android.Navigator.activity.ActivityNavigator;
import com.krishig.android.Navigator.fragment.FragmentNavigator;
import com.krishig.android.R;
import com.krishig.android.data.local.sharedpreferences.SharedPreferencesHelper;
import com.krishig.android.data.remote.ApiResponseArray;
import com.krishig.android.data.remote.ApiResponseObject;
import com.krishig.android.data.remote.api.ApiService;
import com.krishig.android.data.remote.helper.RetrofitClient;
import com.krishig.android.databinding.FragmentHomeBinding;
import com.krishig.android.model.Cart;
import com.krishig.android.model.Category;
import com.krishig.android.model.Market;
import com.krishig.android.model.Product;
import com.krishig.android.model.ProductBrands;
import com.krishig.android.model.SubCategory;
import com.krishig.android.ui.AppConstants;
import com.krishig.android.ui.addToBag.view.AddToFragment;
import com.krishig.android.ui.base.BaseFragment;
import com.krishig.android.ui.home.fragments.ProductDetail.view.ProductDetailActivity;
import com.krishig.android.ui.home.fragments.home.SubProductDetail.view.SubCategoryAndProductActivity;
import com.krishig.android.ui.home.fragments.home.adapter.CategoryRecyclerViewAdapter;
import com.krishig.android.ui.home.fragments.home.adapter.SliderAdapter;
import com.krishig.android.ui.home.fragments.home.adapter.SliderItem;
import com.krishig.android.ui.home.fragments.home.adapter.TopBrandsViewAdapter;
import com.krishig.android.ui.home.fragments.home.adapter.TopFertilizerViewAdapter;
import com.krishig.android.ui.home.fragments.home.adapter.TopSeedsViewAdapter;
import com.krishig.android.ui.home.fragments.home.adapter.ViewPagerLeadAdapter;
import com.krishig.android.ui.home.fragments.home.viewmodel.HomeFragmentViewModel;
import com.krishig.android.ui.home.view.HomeActivity;
import com.krishig.android.ui.search.SearchActivity;
import com.krishig.android.ui.seeAllBrands.SeeAllBrandsActivity;
import com.krishig.android.ui.seeAllProduct.SeeAllProductActivity;
import com.library.adapter.recyclerview.LayoutManagerUtils;
import com.library.adapter.recyclerview.listener.OnRecyclerViewItemChildClick;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class HomeFragment extends BaseFragment<FragmentHomeBinding> {
    //  private ArrayList<SubCategoryBanner> subCategoryBannerArrayList = new ArrayList<>();
    NavController navController;
    ArrayList<Market> homeArrayList = new ArrayList<>();
    private SliderAdapter categoriesOffersBannerSliderAdapter;
    List<SliderItem> sliderItemList = new ArrayList<>();
    HomeFragmentViewModel viewModel;
    @Inject
    SharedPreferencesHelper sharedPreferencesHelper;
    ViewPagerLeadAdapter assessmentViewPagerAdapter;
    private int indicatorWidth;
    private ApiService apiService;
    CategoryRecyclerViewAdapter categoryRecyclerViewAdapter;
    TopBrandsViewAdapter topBrandsViewAdapter;
    TopSeedsViewAdapter topSeedsViewAdapter;
    TopFertilizerViewAdapter topFertilizerViewAdapter;
    ArrayList<ProductBrands.Result> topBrandArrayList = new ArrayList<>();
    ArrayList<Product.Result> seedArrayList = new ArrayList<>();
    ArrayList<Product.Result> fertilizerList = new ArrayList<>();

    ArrayList<SubCategory.Result> arrayList = new ArrayList<>();


    String categoryId = "";
    String categoryId2 = "";

    @Override
    protected ActivityNavigator getActivityNavigator() {
        return new ActivityNavigator(getActivity());
    }

    @Override
    protected FragmentNavigator getFragmentNavigator() {
        return new FragmentNavigator(getActivity().getSupportFragmentManager(), R.id.nav_host_fragment_content_main);
    }

    @Override
    protected FragmentHomeBinding getViewBinding() {
        return FragmentHomeBinding.inflate(getLayoutInflater());
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
        setTopBrandRecyclerView();
        setSeedRecyclerView();
        setFertilizerRecyclerView();
        navController = Navigation.findNavController(rootView);
        categoriesOffersBannerSliderAdapter = new SliderAdapter(getActivity(), null);
        viewBinding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM);
        viewBinding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        viewBinding.imageSlider.startAutoCycle();
        viewBinding.imageSlider.setSliderAdapter(categoriesOffersBannerSliderAdapter);


        viewBinding.tabLayout.addTab(viewBinding.tabLayout.newTab().setText("Seed"));
        viewBinding.tabLayout.addTab(viewBinding.tabLayout.newTab().setText("Fertiliser"));
        FragmentManager fragmentManager = getChildFragmentManager();
        assessmentViewPagerAdapter = new ViewPagerLeadAdapter(fragmentManager, getLifecycle());
        viewBinding.viewPager2.setAdapter(assessmentViewPagerAdapter);

        // Determine indicator width at runtime
        viewBinding.tabLayout.post(new Runnable() {
            @Override
            public void run() {
                indicatorWidth = viewBinding.tabLayout.getWidth() / viewBinding.tabLayout.getTabCount();
                //Assign new width
                FrameLayout.LayoutParams indicatorParams = (FrameLayout.LayoutParams) viewBinding.indicator.getLayoutParams();
                indicatorParams.width = indicatorWidth;
                viewBinding.indicator.setLayoutParams(indicatorParams);
            }
        });

        viewBinding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewBinding.viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewBinding.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) viewBinding.indicator.getLayoutParams();

                //Multiply positionOffset with indicatorWidth to get translation
                float translationOffset = (positionOffset + position) * indicatorWidth;
                params.leftMargin = (int) translationOffset;
                viewBinding.indicator.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                viewBinding.tabLayout.selectTab(viewBinding.tabLayout.getTabAt(position));

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

    }

    @Override
    protected void addTextChangedListener() {

    }

    @Override
    protected void setOnFocusChangeListener() {

    }

    @Override
    protected void observeViewModel() {

        slider_image("application/json", "application/json",
                sharedPreferencesHelper.getKeyToken());
        showProgressDialog();


        Observer<String> getCartUserError = new Observer<String>() {
            @Override
            public void onChanged(@Nullable String error) {
                hideProgressDialog();
                if (error == null) {
                    //  showToast(getString(R.string.something_went_wrong_please_try_again));
                } else {
                    // showToast(error);
                }
            }
        };
        viewModel.getCartUserError().observe(this, getCartUserError);

        final Observer<Cart> getCartUserSuccess = new Observer<Cart>() {
            @Override
            public void onChanged(Cart cart) {
                hideProgressDialog();
                int size = cart.getCartProductResponseDtoList().size();
                viewBinding.cartCountTextView.setText(String.valueOf(size));
            }
        };
        viewModel.getCartUserSuccess().observe(this, getCartUserSuccess);

    }

    @Override
    protected void setOnClickListener() {

        viewBinding.searchEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });

        topBrandsViewAdapter.setOnRecyclerViewItemChildClick(new OnRecyclerViewItemChildClick<ProductBrands.Result>() {
            @Override
            public void OnItemChildClick(View viewChild, ProductBrands.Result market, int position) {
                switch (viewChild.getId()) {
                    case R.id.addMaterialCardView:
                        Intent intent = new Intent(getActivity(), SubCategoryAndProductActivity.class);
                        intent.putExtra(AppConstants.Extras.PRODUCT_NAME_ID, market.getId());
                        intent.putExtra(AppConstants.Extras.SUB_CATEGORY_NAME, market.getBrandName());
                        startActivity(intent);
                        break;
                    default:
                        break;

                }
            }
        });


        topSeedsViewAdapter.setOnRecyclerViewItemChildClick(new OnRecyclerViewItemChildClick<Product.Result>() {
            @Override
            public void OnItemChildClick(View viewChild, Product.Result result, int position) {
                switch (viewChild.getId()) {
                    case R.id.addSeedMaterialCardView:
                        Intent intent = new Intent(getContext(), ProductDetailActivity.class);
                        intent.putExtra(AppConstants.Extras.PRODUCT_ID, result.getId());
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });

        topFertilizerViewAdapter.setOnRecyclerViewItemChildClick(new OnRecyclerViewItemChildClick<Product.Result>() {
            @Override
            public void OnItemChildClick(View viewChild, Product.Result result, int position) {
                switch (viewChild.getId()) {
                    case R.id.addSeedMaterialCardView:
                        Intent intent = new Intent(getContext(), ProductDetailActivity.class);
                        intent.putExtra(AppConstants.Extras.PRODUCT_ID, result.getId());
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });

        viewBinding.cartImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentNavigator.popAll();
                AddToFragment addToFragment = new AddToFragment();
                fragmentNavigator.push(addToFragment, false, true);
            }
        });


        viewBinding.seeAllBrandsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SeeAllBrandsActivity.class);
                startActivity(intent);
            }
        });
        viewBinding.seeAllTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SeeAllProductActivity.class);
                intent.putExtra(AppConstants.Extras.CATEGORY_ID, categoryId);
                intent.putExtra(AppConstants.Extras.FROM_PRODUCT, "Seeds");
                startActivity(intent);
            }
        });
        viewBinding.seeAllTextViewFertilizer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SeeAllProductActivity.class);
                intent.putExtra(AppConstants.Extras.CATEGORY_ID, categoryId2);
                intent.putExtra(AppConstants.Extras.FROM_PRODUCT, "Fertilizer");
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
//            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

    }

    @Override
    public void hideProgressDialog() {
        if (viewBinding.progressDialog.pleaseWaitProgressBar.getVisibility() == View.VISIBLE) {
            viewBinding.progressDialog.pleaseWaitProgressBar.setVisibility(View.GONE);
            //getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("===========Resume============");

        boolean check = false;
        if (sharedPreferencesHelper.getCustomerCartId() == null) {
            viewBinding.addMaterialCardView.setVisibility(View.GONE);
            viewBinding.countMaterialCardView.setVisibility(View.GONE);
            check = true;
        } else {
            viewModel.getCartProduct(sharedPreferencesHelper.getCustomerCartId(), "application/json", "application/json",
                    sharedPreferencesHelper.getKeyToken());
            showProgressDialog();
            viewBinding.addMaterialCardView.setVisibility(View.VISIBLE);
            viewBinding.countMaterialCardView.setVisibility(View.VISIBLE);
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

        getProductBrands("application/json", "application/json",
                sharedPreferencesHelper.getKeyToken());
        showProgressDialog();


    }

    private void setTopBrandRecyclerView() {
        viewBinding.topBrandRecyclerView.setHasFixedSize(true);
        viewBinding.topBrandRecyclerView.setLayoutManager(LayoutManagerUtils.getGridLayoutManagerVertical(getContext(), 4));
        topBrandsViewAdapter = new TopBrandsViewAdapter(getContext());
        topBrandsViewAdapter.addArrayList(topBrandArrayList);
        viewBinding.topBrandRecyclerView.setAdapter(topBrandsViewAdapter);
    }

    private void setSeedRecyclerView() {
        viewBinding.seedsRecyclerView.setHasFixedSize(true);
        viewBinding.seedsRecyclerView.setLayoutManager(LayoutManagerUtils.getGridLayoutManagerVertical(getContext(), 2));
        topSeedsViewAdapter = new TopSeedsViewAdapter(getContext());
        topSeedsViewAdapter.addArrayList(seedArrayList);
        viewBinding.seedsRecyclerView.setAdapter(topSeedsViewAdapter);
    }

    private void setFertilizerRecyclerView() {
        viewBinding.fertilizerRecyclerView.setHasFixedSize(true);
        viewBinding.fertilizerRecyclerView.setLayoutManager(LayoutManagerUtils.getGridLayoutManagerVertical(getContext(), 2));
        topFertilizerViewAdapter = new TopFertilizerViewAdapter(getContext());
        fertilizerList.clear();
        topFertilizerViewAdapter.addArrayList(fertilizerList);
        viewBinding.fertilizerRecyclerView.setAdapter(topFertilizerViewAdapter);
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
                if (categories != null) {

                    for (int i = 0; i < categories.getData().size(); i++) {
                        if (categories.getData().get(i).getCategory_name().equalsIgnoreCase("seeds")) {
                            categoryId = categories.getData().get(i).getId();
                            Log.e("", "1==" + categories.getData().get(i).getCategory_name());
                        }
                        if (categories.getData().get(i).getCategory_name().equalsIgnoreCase("fertilizer")) {
                            categoryId2 = categories.getData().get(i).getId();
                            Log.e("", "2==" + categories.getData().get(i).getCategory_name());
                        }
                    }
                    getSubCategory(categoryId, "application/json", "application/json",
                            sharedPreferencesHelper.getKeyToken());
                    showProgressDialog();

                    getProductFilter(categoryId, "application/json", "application/json",
                            sharedPreferencesHelper.getKeyToken());
                    showProgressDialog();

                    getProductFilterFertiliser(categoryId2, "application/json", "application/json",
                            sharedPreferencesHelper.getKeyToken());
                    showProgressDialog();

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
                if (categories != null) {
                    int size = categories.getData().getResultArrayList().size();
                    if (size == 0) {

                    } else {

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

    private void getProductBrands(String accept, String authorisation, String token) {
        apiService = RetrofitClient.getRetrofitInstance2().create(ApiService.class);
        // Example API call
        Call<ApiResponseObject<ProductBrands>> call = apiService.getProductBrands(accept, authorisation, token);
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

    private void getProductFilter(String categoryId, String accept, String authorisation, String token) {
        apiService = RetrofitClient.getRetrofitInstance2().create(ApiService.class);
        // Example API call
        Call<ApiResponseObject<Product>> call = apiService.productFilter(categoryId, accept, authorisation, token);
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
    }

    private void getProductFilterFertiliser(String categoryId, String accept, String authorisation, String token) {
        apiService = RetrofitClient.getRetrofitInstance2().create(ApiService.class);
        // Example API call
        Call<ApiResponseObject<Product>> call = apiService.productFilter(categoryId, accept, authorisation, token);
        call.enqueue(new Callback<ApiResponseObject<Product>>() {
            @Override
            public void onResponse(Call<ApiResponseObject<Product>> call, Response<ApiResponseObject<Product>> response) {
                ApiResponseObject<Product> categories = response.body();
                hideProgressDialog();
                if (categories != null) {
                    int size = categories.getData().getResult().size();
                    if (size == 0) {
                        viewBinding.fertilizerRecyclerView.setVisibility(View.GONE);
                        viewBinding.errorImageViewFertilizer.setVisibility(View.VISIBLE);
                        viewBinding.errorTextViewFertilizer.setVisibility(View.VISIBLE);
                    } else {
                        viewBinding.fertilizerRecyclerView.setVisibility(View.VISIBLE);
                        viewBinding.errorImageViewFertilizer.setVisibility(View.GONE);
                        viewBinding.errorTextViewFertilizer.setVisibility(View.GONE);
                        // seedArrayList = categories.getData().getResult();

                        fertilizerList.clear();
                        Product.Result resultArrayList = null;
                        if (size >= 8) {
                            for (int i = 0; i < 8; i++) {
                                resultArrayList = categories.getData().getResult().get(i);
                                fertilizerList.add(resultArrayList);
                            }
                        } else {
                            for (int i = 0; i < size; i++) {
                                resultArrayList = categories.getData().getResult().get(i);
                                fertilizerList.add(resultArrayList);
                            }
                        }
                        topFertilizerViewAdapter.clearAllItem();
                        topFertilizerViewAdapter.replaceArrayList(fertilizerList);
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

    private void slider_image(String accept, String authorisation, String token) {
        apiService = RetrofitClient.getRetrofitInstance2().create(ApiService.class);
        // Example API call
        Call<ApiResponseArray<SliderItem>> call = apiService.slider_image(accept, authorisation, token);
        call.enqueue(new Callback<ApiResponseArray<SliderItem>>() {
            @Override
            public void onResponse(Call<ApiResponseArray<SliderItem>> call, Response<ApiResponseArray<SliderItem>> response) {
                ApiResponseArray<SliderItem> categories = response.body();
                hideProgressDialog();
                sliderItemList.clear();
                for (int i = 0; i < categories.getData().size(); i++) {
                    sliderItemList.add(new SliderItem(categories.getData().get(i).getImage()));
                }
                categoriesOffersBannerSliderAdapter.renewItems(sliderItemList);
                viewBinding.imageSlider.setSliderAdapter(categoriesOffersBannerSliderAdapter);

            }

            @Override
            public void onFailure(Call<ApiResponseArray<SliderItem>> call, Throwable t) {
                // Handle network or API call failure
                Log.e("", "onFailure  =" + call.toString());
                Log.e("", "onFailure  =" + t);

            }
        });
    }

}