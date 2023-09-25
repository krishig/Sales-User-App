package com.krishig.android.ui.home.fragments.manageProduct.view;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.krishig.android.Navigator.activity.ActivityNavigator;
import com.krishig.android.Navigator.fragment.FragmentNavigator;
import com.krishig.android.R;
import com.krishig.android.data.local.sharedpreferences.SharedPreferencesHelper;
import com.krishig.android.data.remote.ApiResponseArray;
import com.krishig.android.data.remote.ApiResponseObject;
import com.krishig.android.data.remote.api.ApiService;
import com.krishig.android.data.remote.helper.RetrofitClient;
import com.krishig.android.databinding.FragmentManageProductBinding;
import com.krishig.android.model.Category;
import com.krishig.android.model.Product;
import com.krishig.android.model.ProductBrands;
import com.krishig.android.model.SubCategory;
import com.krishig.android.searchdialog.Search;
import com.krishig.android.searchdialog.SearchUtils;
import com.krishig.android.searchdialog.SelectListener;
import com.krishig.android.ui.AppConstants;
import com.krishig.android.ui.KeyboardVisibility;
import com.krishig.android.ui.base.BaseFragment;
import com.krishig.android.ui.home.fragments.ProductDetail.view.ProductDetailActivity;
import com.krishig.android.ui.home.fragments.home.viewmodel.SubCategoryModel;
import com.krishig.android.ui.home.fragments.manageProduct.adapter.ManageProductAdapter;
import com.krishig.android.ui.home.view.HomeActivity;
import com.krishig.android.ui.search.adapter.SelectBrandAdapter;
import com.krishig.android.ui.search.adapter.SelectSubCategoryAdapter;
import com.library.adapter.recyclerview.LayoutManagerUtils;
import com.library.adapter.recyclerview.listener.OnRecyclerViewItemChildClick;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class ManageProductFragment extends BaseFragment<FragmentManageProductBinding> {
    ManageProductAdapter manageProductAdapter;
    ArrayList<Product.Result> homeArrayList = new ArrayList<Product.Result>();
    ArrayList<Product.Result> dataStoreHomeArrayList = new ArrayList<Product.Result>();

    @Inject
    SharedPreferencesHelper sharedPreferencesHelper;

    //BottomSheet
    SelectBrandAdapter categoryRecyclerViewAdapter;
    SelectSubCategoryAdapter selectSubCategoryAdapter;
    ArrayList<ProductBrands.Result> brandArrayList = new ArrayList<>();
    ArrayList<SubCategory.Result> subCategoryArrayList = new ArrayList<>();
    SubCategoryModel viewModel;

    private ApiService apiService;


    private ArrayList<Search> vehicleTypeSearchArrayList = new ArrayList<Search>();
    private ArrayList<SubCategory.Result> vehicleTypeArraylist = new ArrayList<SubCategory.Result>();

    private ArrayList<Search> categorySearchArrayList = new ArrayList<Search>();
    private ArrayList<Category> categoryArraylist = new ArrayList<Category>();


    private ArrayList<Search> brandTypeSearchArrayList = new ArrayList<Search>();
    private ArrayList<ProductBrands.Result> brandTypeArraylist = new ArrayList<ProductBrands.Result>();

    String from = "", subCategoryId = "", categoryId = "", brandId = "", subCategoryName = "", brandName = "";

    boolean visible = true;

    int totalPages = 0, count = 1, totalPagesSearch = 0;


    String itemPerPageInProduct = "20";


    String brandNameData = "", subCategoryNameData = "";

    BottomSheetDialog bottomSheetDialog;


    @Override
    protected ActivityNavigator getActivityNavigator() {
        return new ActivityNavigator(getActivity());
    }

    @Override
    protected FragmentNavigator getFragmentNavigator() {
        return new FragmentNavigator(getActivity().getSupportFragmentManager(), R.id.nav_host_fragment_content_main);
    }

    @Override
    protected FragmentManageProductBinding getViewBinding() {
        return FragmentManageProductBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void initializeViewModel() {

    }

    @Override
    protected void initializeToolBar() {

    }

    @Override
    protected void initializeObject() {
        bottomSheetDialog = new BottomSheetDialog(getContext());
        registerKeyboardVisibilityListener(getActivity());
        setRecyclerView(viewBinding.recyclerView);
  /*      Bundle bundle = getArguments();
        if (bundle != null) {
            brandId = bundle.getString("brandId");
            subCategoryId = bundle.getString("SubCategoryId");

            subCategoryName = bundle.getString("SubCategoryName");
            brandName = bundle.getString("brandName");

        }*/


    }

    private void setRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(LayoutManagerUtils.getGridLayoutManagerVertical(getContext(), 2));
        recyclerView.setHasFixedSize(true);
        manageProductAdapter = new ManageProductAdapter(getContext());
        manageProductAdapter.addArrayList(homeArrayList);
        recyclerView.setAdapter(manageProductAdapter);
    }

    @Override
    protected void addTextChangedListener() {
        viewBinding.productEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equalsIgnoreCase("")) {
                    count = 1;
                    dataStoreHomeArrayList.clear();
                    getProduct(itemPerPageInProduct, String.valueOf(count), "application/json", "application/json",
                            sharedPreferencesHelper.getKeyToken());
                    showProgressDialog();
                } else {
                    count = 1;
                    int itemPerPageInt = Integer.parseInt(itemPerPageInProduct);
                    productSearch(String.valueOf(itemPerPageInt * totalPages), String.valueOf(count), editable.toString(), "application/json", "application/json",
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
        count = 1;
        dataStoreHomeArrayList.clear();
        getProduct(itemPerPageInProduct, String.valueOf(count), "application/json", "application/json",
                sharedPreferencesHelper.getKeyToken());
        showProgressDialog();
    }

    @Override
    protected void setOnClickListener() {

        viewBinding.selectSubCategoryAppCompatAutoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialog("SubCategory");
            }
        });


        viewBinding.selectBrandAppCompatAutoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheetDialog("Brand");
            }
        });


        viewBinding.selectCategoryAppCompatAutoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (categorySearchArrayList.size() != 0) {
                    SearchUtils.searchDialog(getActivity(), "Select Category", categorySearchArrayList, new SelectListener() {
                        @Override
                        public void onSelected(Search search, int position) {
                            viewBinding.selectCategoryAppCompatAutoCompleteTextView.setText(search.getItemName());
                            categoryId = search.getItemId();
                            count = 1;
                            System.out.println("=====categoryId=====" + categoryId);
                            int itemPerPageInt = Integer.parseInt(itemPerPageInProduct);
                            getProductFilter(categoryId, subCategoryId, brandId, String.valueOf(itemPerPageInt * totalPages), String.valueOf(count), "application/json", "application/json",
                                    sharedPreferencesHelper.getKeyToken());
                            showProgressDialog();

                        }
                    });
                }
            }
        });

        viewBinding.filterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (visible) {
                    visible = false;
                    viewBinding.linear.setVisibility(View.VISIBLE);
                } else {
                    visible = true;
                    viewBinding.linear.setVisibility(View.GONE);
                }
            }
        });

        viewBinding.clearTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewBinding.selectBrandAppCompatAutoCompleteTextView.setText("");
                viewBinding.selectSubCategoryAppCompatAutoCompleteTextView.setText("");
                viewBinding.selectCategoryAppCompatAutoCompleteTextView.setText("");
                subCategoryId = "";
                brandId = "";
                categoryId = "";
                count = 1;
                dataStoreHomeArrayList.clear();
                getProduct(itemPerPageInProduct, String.valueOf(count), "application/json", "application/json",
                        sharedPreferencesHelper.getKeyToken());
                showProgressDialog();
            }
        });

        viewBinding.nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    ++count;
                    if (count <= totalPages) {
                        if (!subCategoryId.equalsIgnoreCase("")) {

                        } else if (!brandId.equalsIgnoreCase("")) {

                        } else if (!categoryId.equalsIgnoreCase("")) {

                        } else {
                            viewBinding.idPBLoading.setVisibility(View.VISIBLE);
                            getProduct(itemPerPageInProduct, String.valueOf(count), "application/json", "application/json",
                                    sharedPreferencesHelper.getKeyToken());
                        }

                    }

                }
            }
        });

        manageProductAdapter.setOnRecyclerViewItemChildClick(new OnRecyclerViewItemChildClick<Product.Result>() {
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

        getProductBrands("application/json", "application/json",
                sharedPreferencesHelper.getKeyToken());
        showProgressDialog();
        getSubCategory("application/json", "application/json",
                sharedPreferencesHelper.getKeyToken());
        showProgressDialog();
        getCategories("application/json", "application/json",
                sharedPreferencesHelper.getKeyToken());
        showProgressDialog();
    }

    private void getProduct(String items_per_page,
                            String page_number, String accept, String authorisation, String token) {
        apiService = RetrofitClient.getRetrofitInstance2().create(ApiService.class);
        // Example API call
        Call<ApiResponseObject<Product>> call = apiService.getProduct(items_per_page, page_number, accept, authorisation, token);
        call.enqueue(new Callback<ApiResponseObject<Product>>() {
            @Override
            public void onResponse(Call<ApiResponseObject<Product>> call, Response<ApiResponseObject<Product>> response) {
                ApiResponseObject<Product> categories = response.body();
                hideProgressDialog();
                viewBinding.idPBLoading.setVisibility(View.GONE);
                int size = categories.getData().getResult().size();
                if (categories != null) {
                    if (size == 0) {
                        viewBinding.recyclerView.setVisibility(View.GONE);
                        viewBinding.errorImageView.setVisibility(View.VISIBLE);
                        viewBinding.errorTextView.setVisibility(View.VISIBLE);
                    } else {
                        totalPages = categories.getData().getTotalPages();
                        viewBinding.recyclerView.setVisibility(View.VISIBLE);
                        viewBinding.errorImageView.setVisibility(View.GONE);
                        viewBinding.errorTextView.setVisibility(View.GONE);
                        homeArrayList = categories.getData().getResult();
                        dataStoreHomeArrayList.addAll(homeArrayList);
                        Log.e("", "dataStoreHomeArrayList==" + dataStoreHomeArrayList.size());
                        Log.e("", "homeArrayList==" + homeArrayList.size());
                        manageProductAdapter.clearAllItem();
                        manageProductAdapter.replaceArrayList(dataStoreHomeArrayList);
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
                    brandTypeArraylist = categories.getData().getResultArrayList();
                    brandTypeSearchArrayList.clear();
                    for (ProductBrands.Result employees1 : brandTypeArraylist) {
                        brandTypeSearchArrayList.add(new Search(employees1.getId(), employees1.getBrandName()));
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

    private void getSubCategory(String accept, String authorisation, String token) {
        apiService = RetrofitClient.getRetrofitInstance2().create(ApiService.class);
        // Example API call
        Call<ApiResponseObject<SubCategory>> call = apiService.getSubCategories(accept, authorisation, token);
        call.enqueue(new Callback<ApiResponseObject<SubCategory>>() {
            @Override
            public void onResponse(Call<ApiResponseObject<SubCategory>> call, Response<ApiResponseObject<SubCategory>> response) {
                ApiResponseObject<SubCategory> categories = response.body();
                hideProgressDialog();
                if (categories != null) {
                    int size = categories.getData().getResultArrayList().size();
                    vehicleTypeArraylist = categories.getData().getResultArrayList();
                    vehicleTypeSearchArrayList.clear();
                    for (SubCategory.Result employees1 : vehicleTypeArraylist) {
                        vehicleTypeSearchArrayList.add(new Search(employees1.getId(), employees1.getSubCategoryName()));
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

    private void getProductFilter(String category_id,
                                  String sub_category_id,
                                  String brand_id,
                                  String items_per_page,
                                  String page_number,
                                  String accept, String authorisation, String token) {
        apiService = RetrofitClient.getRetrofitInstance2().create(ApiService.class);
        // Example API call
        Call<ApiResponseObject<Product>> call = apiService.productFilterWithBrandId(category_id, sub_category_id, brand_id, items_per_page, page_number, accept, authorisation, token);
        call.enqueue(new Callback<ApiResponseObject<Product>>() {
            @Override
            public void onResponse(Call<ApiResponseObject<Product>> call, Response<ApiResponseObject<Product>> response) {
                ApiResponseObject<Product> categories = response.body();
                hideProgressDialog();
                if (categories != null) {
                    int size = categories.getData().getResult().size();
                    viewBinding.idPBLoading.setVisibility(View.GONE);
                    if (categories != null) {
                        if (size == 0) {
                            viewBinding.recyclerView.setVisibility(View.GONE);
                            viewBinding.errorImageView.setVisibility(View.VISIBLE);
                            viewBinding.errorTextView.setVisibility(View.VISIBLE);
                        } else {
                            //  totalPages = categories.getData().getTotalPages();
                            viewBinding.recyclerView.setVisibility(View.VISIBLE);
                            viewBinding.errorImageView.setVisibility(View.GONE);
                            viewBinding.errorTextView.setVisibility(View.GONE);
                            homeArrayList = categories.getData().getResult();
                            Log.e("", "homeArrayList==" + homeArrayList.size());
                            manageProductAdapter.clearAllItem();
                            manageProductAdapter.replaceArrayList(homeArrayList);
                        }
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


    private void productSearch(String items_per_page,
                               String page_number, String search_brand, String accept, String authorisation, String token) {
        apiService = RetrofitClient.getRetrofitInstance2().create(ApiService.class);
        // Example API call
        Call<ApiResponseObject<Product>> call = apiService.productSearch(items_per_page, page_number, search_brand, accept, authorisation, token);
        call.enqueue(new Callback<ApiResponseObject<Product>>() {
            @Override
            public void onResponse(Call<ApiResponseObject<Product>> call, Response<ApiResponseObject<Product>> response) {
                ApiResponseObject<Product> categories = response.body();
                hideProgressDialog();
                int size = categories.getData().getResult().size();
                if (categories != null) {
                    if (size == 0) {
                        viewBinding.recyclerView.setVisibility(View.GONE);
                        viewBinding.errorImageView.setVisibility(View.VISIBLE);
                        viewBinding.errorTextView.setVisibility(View.VISIBLE);
                    } else {
                        // totalPages = categories.getData().getTotalPages();
                        viewBinding.recyclerView.setVisibility(View.VISIBLE);
                        viewBinding.errorImageView.setVisibility(View.GONE);
                        viewBinding.errorTextView.setVisibility(View.GONE);
                        homeArrayList = categories.getData().getResult();
                        Log.e("", "homeArrayList==" + homeArrayList.size());
                        manageProductAdapter.clearAllItem();
                        manageProductAdapter.replaceArrayList(homeArrayList);
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
                    int size = categories.getData().size();
                    categoryArraylist = categories.getData();
                    categorySearchArrayList.clear();
                    for (Category employees1 : categoryArraylist) {
                        categorySearchArrayList.add(new Search(employees1.getId(), employees1.getCategory_name()));
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


    private void showBottomSheetDialog(String from) {
        bottomSheetDialog.setContentView(R.layout.dialog_select_brand_sub_category);
        bottomSheetDialog.getBehavior().setState(BottomSheetBehavior.STATE_EXPANDED);

        TextView subCategoryTextView = bottomSheetDialog.findViewById(R.id.subCategoryTextView);
        EditText searchEditText = bottomSheetDialog.findViewById(R.id.searchEditText);
        ImageView cancelImageView = bottomSheetDialog.findViewById(R.id.cancelImageView);
        RecyclerView seedsRecyclerView = bottomSheetDialog.findViewById(R.id.seedsRecyclerView);
        ImageView errorImageViewSeeds = bottomSheetDialog.findViewById(R.id.errorImageViewSeeds);
        TextView errorTextViewSeeds = bottomSheetDialog.findViewById(R.id.errorTextViewSeeds);

        cancelImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equalsIgnoreCase("")) {
                    seedsRecyclerView.setVisibility(View.GONE);
                    errorImageViewSeeds.setVisibility(View.VISIBLE);
                    errorTextViewSeeds.setVisibility(View.VISIBLE);
                } else {
                    seedsRecyclerView.setVisibility(View.VISIBLE);
                    errorImageViewSeeds.setVisibility(View.GONE);
                    errorTextViewSeeds.setVisibility(View.GONE);

                    if (from.equalsIgnoreCase("Brand")) {
                        subProductBrandsSearch(editable.toString(), "application/json", "application/json",
                                sharedPreferencesHelper.getKeyToken(), seedsRecyclerView, errorImageViewSeeds, errorTextViewSeeds);
                    } else if (from.equalsIgnoreCase("SubCategory")) {
                        subCategorySearch(editable.toString(), "application/json", "application/json",
                                sharedPreferencesHelper.getKeyToken(), seedsRecyclerView, errorImageViewSeeds, errorTextViewSeeds);
                    }
                }
            }
        });


        if (from.equalsIgnoreCase("Brand")) {
            setBrandRecyclerView(seedsRecyclerView);
            subCategoryTextView.setText("Select Brand");
            searchEditText.setHint("Search Brand");
        } else if (from.equalsIgnoreCase("SubCategory")) {
            setSubCategoryRecyclerView(seedsRecyclerView);
            subCategoryTextView.setText("Select SubCategory");
            searchEditText.setHint("Search SubCategory");
        }
        searchEditText.setText("");
        seedsRecyclerView.setVisibility(View.GONE);
        errorImageViewSeeds.setVisibility(View.VISIBLE);
        errorTextViewSeeds.setVisibility(View.VISIBLE);


        if (from.equalsIgnoreCase("Brand")) {
            categoryRecyclerViewAdapter.setOnRecyclerViewItemChildClick(new OnRecyclerViewItemChildClick<ProductBrands.Result>() {
                @Override
                public void OnItemChildClick(View viewChild, ProductBrands.Result market, int position) {
                    switch (viewChild.getId()) {
                        case R.id.searchDialogRowLinearLayout:
                            brandId = market.getId();
                            brandName = market.getBrandName();
                            bottomSheetDialog.dismiss();
                            count = 1;
                            System.out.println("=====brandId=====" + brandId);
                            int itemPerPageInt = Integer.parseInt(itemPerPageInProduct);
                            getProductFilter(categoryId, subCategoryId, brandId, String.valueOf(itemPerPageInt * totalPages), String.valueOf(count), "application/json", "application/json",
                                    sharedPreferencesHelper.getKeyToken());
                            showProgressDialog();
                            viewBinding.selectBrandAppCompatAutoCompleteTextView.setText(brandName);
                            break;
                        default:
                            break;

                    }
                }
            });
        } else if (from.equalsIgnoreCase("SubCategory")) {
            selectSubCategoryAdapter.setOnRecyclerViewItemChildClick(new OnRecyclerViewItemChildClick<SubCategory.Result>() {
                @Override
                public void OnItemChildClick(View viewChild, SubCategory.Result market, int position) {
                    switch (viewChild.getId()) {
                        case R.id.searchDialogRowLinearLayout:
                            subCategoryId = market.getId();
                            subCategoryName = market.getSubCategoryName();
                            bottomSheetDialog.dismiss();
                            viewBinding.selectSubCategoryAppCompatAutoCompleteTextView.setText(subCategoryName);
                            count = 1;
                            System.out.println("=====subCategoryId=====" + subCategoryId);
                            int itemPerPageInt = Integer.parseInt(itemPerPageInProduct);
                            getProductFilter(categoryId, subCategoryId, brandId, String.valueOf(itemPerPageInt * totalPages), String.valueOf(count), "application/json", "application/json",
                                    sharedPreferencesHelper.getKeyToken());
                            showProgressDialog();
                            break;
                        default:
                            break;

                    }
                }
            });

        }

        bottomSheetDialog.show();


    }


    private void setBrandRecyclerView(RecyclerView seedsRecyclerView) {
        seedsRecyclerView.setHasFixedSize(true);
        seedsRecyclerView.setLayoutManager(LayoutManagerUtils.getLinearLayoutManagerVertical(getActivity()));
        categoryRecyclerViewAdapter = new SelectBrandAdapter(getActivity());
        categoryRecyclerViewAdapter.addArrayList(brandArrayList);
        seedsRecyclerView.setAdapter(categoryRecyclerViewAdapter);
    }

    private void setSubCategoryRecyclerView(RecyclerView seedsRecyclerView) {
        seedsRecyclerView.setHasFixedSize(true);
        seedsRecyclerView.setLayoutManager(LayoutManagerUtils.getLinearLayoutManagerVertical(getActivity()));
        selectSubCategoryAdapter = new SelectSubCategoryAdapter(getActivity());
        selectSubCategoryAdapter.addArrayList(subCategoryArrayList);
        seedsRecyclerView.setAdapter(selectSubCategoryAdapter);
    }


    private void subCategorySearch(String search_sub_category, String accept, String authorisation, String token,
                                   RecyclerView seedsRecyclerView, ImageView errorImageViewSeeds, TextView errorTextViewSeeds) {
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
                        seedsRecyclerView.setVisibility(View.GONE);
                        errorImageViewSeeds.setVisibility(View.VISIBLE);
                        errorTextViewSeeds.setVisibility(View.VISIBLE);
                    } else {
                        seedsRecyclerView.setVisibility(View.VISIBLE);
                        errorImageViewSeeds.setVisibility(View.GONE);
                        errorTextViewSeeds.setVisibility(View.GONE);
                        subCategoryArrayList = categories.getData().getResultArrayList();
                        selectSubCategoryAdapter.clearAllItem();
                        selectSubCategoryAdapter.replaceArrayList(subCategoryArrayList);
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

    private void subProductBrandsSearch(String search_brand, String accept, String authorisation, String token,
                                        RecyclerView seedsRecyclerView, ImageView errorImageViewSeeds, TextView errorTextViewSeeds) {

        apiService = RetrofitClient.getRetrofitInstance2().create(ApiService.class);
        // Example API call
        Call<ApiResponseObject<ProductBrands>> call = apiService.subProductBrandsSearch(search_brand, accept, authorisation, token);
        call.enqueue(new Callback<ApiResponseObject<ProductBrands>>() {
            @Override
            public void onResponse(Call<ApiResponseObject<ProductBrands>> call, Response<ApiResponseObject<ProductBrands>> response) {
                ApiResponseObject<ProductBrands> categories = response.body();
                hideProgressDialog();
                if (categories != null) {
                    int size = categories.getData().getResultArrayList().size();
                    if (size == 0) {
                        seedsRecyclerView.setVisibility(View.GONE);
                        errorImageViewSeeds.setVisibility(View.VISIBLE);
                        errorTextViewSeeds.setVisibility(View.VISIBLE);
                    } else {
                        seedsRecyclerView.setVisibility(View.VISIBLE);
                        errorImageViewSeeds.setVisibility(View.GONE);
                        errorTextViewSeeds.setVisibility(View.GONE);
                        brandArrayList = categories.getData().getResultArrayList();
                        categoryRecyclerViewAdapter.clearAllItem();
                        categoryRecyclerViewAdapter.replaceArrayList(brandArrayList);
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