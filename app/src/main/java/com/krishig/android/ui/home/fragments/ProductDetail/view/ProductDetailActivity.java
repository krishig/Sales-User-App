package com.krishig.android.ui.home.fragments.ProductDetail.view;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.krishig.android.Navigator.activity.ActivityNavigator;
import com.krishig.android.Navigator.fragment.FragmentNavigator;
import com.krishig.android.R;
import com.krishig.android.data.local.sharedpreferences.SharedPreferencesHelper;
import com.krishig.android.data.remote.ApiResponseObject;
import com.krishig.android.data.remote.RequestUtils;
import com.krishig.android.data.remote.api.ApiService;
import com.krishig.android.data.remote.helper.RetrofitClient;
import com.krishig.android.databinding.ActivityProductDetailBinding;
import com.krishig.android.model.Product;
import com.krishig.android.model.ProductId;
import com.krishig.android.ui.AppConstants;
import com.krishig.android.ui.base.BaseActivity;
import com.krishig.android.ui.home.fragments.ProductDetail.adapter.ImageRecyclerViewAdapter;
import com.krishig.android.ui.home.fragments.home.viewmodel.SubCategoryModel;

import org.json.JSONObject;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class ProductDetailActivity extends BaseActivity<ActivityProductDetailBinding> {

    String productId = "";
    @Inject
    SharedPreferencesHelper sharedPreferencesHelper;

    SubCategoryModel viewModel;
    ImageRecyclerViewAdapter imageRecyclerViewAdapter;
    ArrayList<ProductId.Result.ProductImage> galleryImagesArrayList = new ArrayList<>();

    int count = 1;
    private ApiService apiService;

    @Override
    protected ActivityProductDetailBinding getViewBinding() {
        return ActivityProductDetailBinding.inflate(getLayoutInflater());
    }

    @Override
    protected ActivityNavigator getActivityNavigator() {
        return new ActivityNavigator(ProductDetailActivity.this);
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
        if (sharedPreferencesHelper.getCustomerCartId() == null) {
            viewBinding.editButton.setVisibility(View.GONE);
        } else {
            viewBinding.editButton.setVisibility(View.VISIBLE);
        }


        setRecyclerView();
        if (getIntent().getExtras() != null) {
            productId = getIntent().getExtras().getString(AppConstants.Extras.PRODUCT_ID);

            getProductFilterFertiliser(productId, "application/json", "application/json",
                    sharedPreferencesHelper.getKeyToken());
            showProgressDialog();

        }

        viewBinding.productImageView.setVisibility(View.VISIBLE);
        viewBinding.productImageView.setImageResource(R.drawable.image_product);

    }

    @Override
    protected void addTextChangedListener() {

    }

    @Override
    protected void setOnFocusChangeListener() {

    }

    private void setRecyclerView() {
        viewBinding.recyclerView.setHasFixedSize(true);
        viewBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        imageRecyclerViewAdapter = new ImageRecyclerViewAdapter(this);
        imageRecyclerViewAdapter.addArrayList(galleryImagesArrayList);
        viewBinding.recyclerView.setAdapter(imageRecyclerViewAdapter);
    }

    @Override
    protected void observeViewModel() {

        Observer<String> addCartUserError = new Observer<String>() {
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
        viewModel.addCartUserError().observe(this, addCartUserError);

        final Observer<Product> addCartUserSuccess = new Observer<Product>() {
            @Override
            public void onChanged(Product product) {
                hideProgressDialog();
                finish();
            }
        };
        viewModel.addCartUserSuccess().observe(this, addCartUserSuccess);


    }

    @Override
    protected void setOnClickListener() {
        viewBinding.icBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        viewBinding.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1.put("id", productId);
                    jsonObject.put("cartId", sharedPreferencesHelper.getCustomerCartId());
                    jsonObject.put("product", jsonObject1);
                    jsonObject.put("productQuantity", viewBinding.countTextView.getText().toString().trim());
                    RequestBody requestBody = RequestUtils.createRequestBodyForString(jsonObject.toString());

                    viewModel.addProductToCart(requestBody, "application/json", "application/json",
                            sharedPreferencesHelper.getKeyToken());
                    showProgressDialog();
                } catch (Exception exception) {
                    Log.e("", "" + exception);
                }
            }
        });

        viewBinding.addMaterialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ++count;
                viewBinding.countTextView.setText(String.valueOf(count));
            }
        });
        viewBinding.minusMaterialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!String.valueOf(count).equalsIgnoreCase("1")) {
                    --count;
                    viewBinding.countTextView.setText(String.valueOf(count));
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


    private void alertDialogConfirmExit(Activity activity, String id) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);

        alertDialogBuilder.setIcon(R.drawable.ic_black_question_mark);
        alertDialogBuilder.setTitle("Confirm Delete");
        alertDialogBuilder.setMessage("Are you sure you want to Delete?");


        alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        alertDialogBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        Button positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);

        positiveButton.setTextColor(Color.parseColor("#FFFFFF"));
        positiveButton.setBackgroundColor(Color.parseColor("#000000"));

        negativeButton.setTextColor(Color.parseColor("#FFFFFF"));
        negativeButton.setBackgroundColor(Color.parseColor("#000000"));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        params.setMargins(20, 0, 0, 0);
        positiveButton.setLayoutParams(params);
    }


    @Override
    protected void onResume() {
        super.onResume();
      /*  viewModel.getProductWithId(productId, "application/json", "application/json",
                sharedPreferencesHelper.getKeyToken());
        showProgressDialog();*/
    }

    private void getProductFilterFertiliser(String id, String accept, String authorisation, String token) {
        apiService = RetrofitClient.getRetrofitInstance2().create(ApiService.class);
        // Example API call
        Call<ApiResponseObject<ProductId>> call = apiService.getProductWithId(id, accept, authorisation, token);
        call.enqueue(new Callback<ApiResponseObject<ProductId>>() {
            @Override
            public void onResponse(Call<ApiResponseObject<ProductId>> call, Response<ApiResponseObject<ProductId>> response) {
                ApiResponseObject<ProductId> categories = response.body();
                hideProgressDialog();
                if (categories.getData() != null) {
                    System.out.println(categories.getData().getResult().getProductImage().size());
                    if (categories.getData().getResult().getProductImage().size() == 0) {
                        viewBinding.productImageView.setVisibility(View.VISIBLE);
                        viewBinding.productImageView.setImageResource(R.drawable.image_product);
                    } else {
                        viewBinding.productImageView.setVisibility(View.GONE);
                        galleryImagesArrayList = categories.getData().getResult().getProductImage();
                        imageRecyclerViewAdapter.clearAllItem();
                        imageRecyclerViewAdapter.addArrayList(galleryImagesArrayList);
                    }

                    viewBinding.titleTextView.setText(categories.getData().getResult().getProductName());
                    viewBinding.priceTextView.setText("Rs. " + String.valueOf(categories.getData().getResult().getPrice()));
                    viewBinding.descriptionTextView.setText(categories.getData().getResult().getProductDescription());
                    viewBinding.subCategoryTextView.setText(categories.getData().getResult().getSubCategoryName());
                    viewBinding.baseUnitTextView.setText(categories.getData().getResult().getBaseUnit());
                    viewBinding.discountPercentTextView.setText(categories.getData().getResult().getDiscount()+"%");
                    viewBinding.brandATextView.setText(categories.getData().getResult().getBrandName());

                    int amount = categories.getData().getResult().getPrice();
                    double percentage = categories.getData().getResult().getDiscount();
                    double result = (percentage / 100) * amount;
                    double disCountPrice=amount-result;
                    viewBinding.discountTextView.setText("Rs. " +String.valueOf(disCountPrice));

                }
            }

            @Override
            public void onFailure(Call<ApiResponseObject<ProductId>> call, Throwable t) {
                // Handle network or API call failure
                Log.e("", "onFailure  =" + call.toString());
                Log.e("", "onFailure  =" + t);

            }
        });
    }

}