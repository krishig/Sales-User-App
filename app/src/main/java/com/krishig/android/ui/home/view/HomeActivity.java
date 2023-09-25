package com.krishig.android.ui.home.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.ViewCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.krishig.android.Navigator.activity.ActivityNavigator;
import com.krishig.android.Navigator.fragment.FragmentNavigator;
import com.krishig.android.R;
import com.krishig.android.data.local.sharedpreferences.SharedPreferencesHelper;
import com.krishig.android.databinding.ActivityHomeBinding;
import com.krishig.android.ui.ManageCustomer.view.ManageCustomerActivity;
import com.krishig.android.ui.base.BaseActivity;
import com.krishig.android.ui.home.fragments.home.view.HomeFragment;
import com.krishig.android.ui.home.fragments.manageOrders.view.ManageOrdersFragment;
import com.krishig.android.ui.home.fragments.manageProduct.view.ManageProductFragment;
import com.krishig.android.ui.home.fragments.manageProfile.ManageProfileFragment;
import com.krishig.android.ui.login.view.LoginActivity;
import com.library.utilities.activity.ActivityUtils;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeActivity extends BaseActivity<ActivityHomeBinding> {
    AppBarConfiguration appBarConfiguration;
    ActionBarDrawerToggle mActionBarDrawerToggle;

    @Inject
    SharedPreferencesHelper sharedPreferencesHelper;

    @Override
    protected ActivityHomeBinding getViewBinding() {
        return ActivityHomeBinding.inflate(getLayoutInflater());
    }

    @Override
    protected ActivityNavigator getActivityNavigator() {
        return new ActivityNavigator(HomeActivity.this);
    }

    @Override
    protected FragmentNavigator getFragmentNavigator() {
        return new FragmentNavigator(getSupportFragmentManager(), R.id.nav_host_fragment_content_main);
    }

    @Override
    protected void initializeViewModel() {

    }

    @Override
    protected void initializeToolBar() {
        setSupportActionBar(viewBinding.toolbar);

    }

    @Override
    protected void initializeObject() {
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.nav_manage_orders, R.id.navigation_manage_product, R.id.nav_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(viewBinding.bottomNav, navController);
       // configureBottomNavigationView(viewBinding.bottomNav);

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
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
        viewBinding.goodMorningLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ManageCustomerActivity.class);
                startActivity(intent);
            }
        });
        viewBinding.settingMaterialCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogConfirmExit(HomeActivity.this);
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

    }

    @Override
    public void hideProgressDialog() {

    }

    private void alertDialogConfirmExit(Activity activity) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);

        alertDialogBuilder.setIcon(R.drawable.ic_black_question_mark);
        alertDialogBuilder.setTitle("Confirm Exit");
        alertDialogBuilder.setMessage("Are you sure you want to Exit?");


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
                sharedPreferencesHelper.setCustomerName(null);
                sharedPreferencesHelper.setCustomerCartId(null);
                sharedPreferencesHelper.setCustomerID(null);
                sharedPreferencesHelper.setCustomerMobileNumber(null);
                sharedPreferencesHelper.setRemember(false);
                Intent intent = ActivityUtils.launchActivityWithClearBackStack(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
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

    private void configureBottomNavigationView(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        fragmentNavigator.popAll();
                        HomeFragment homeFragment = new HomeFragment();
                        fragmentNavigator.push(homeFragment, false, false);
                        return true;
                    case R.id.nav_add_to_bag:
                        fragmentNavigator.popAll();
                        ManageOrdersFragment manageOrdersFragment = new ManageOrdersFragment();
                        fragmentNavigator.push(manageOrdersFragment, false, false);
                        return true;
                    case R.id.navigation_manage_product:
                        fragmentNavigator.popAll();
                        ManageProductFragment manageProductFragment = new ManageProductFragment();
                        fragmentNavigator.push(manageProductFragment, false, false);
                        return true;
                    case R.id.nav_profile:
                        fragmentNavigator.popAll();
                        ManageProfileFragment manageProfileFragment = new ManageProfileFragment();
                        fragmentNavigator.push(manageProfileFragment, false, false);
                        return true;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sharedPreferencesHelper.getCustomerMobileNumber() != null) {
            viewBinding.nameTextView.setText(sharedPreferencesHelper.getCustomerName());
            viewBinding.mobileNumber.setText(sharedPreferencesHelper.getCustomerMobileNumber());
            viewBinding.mobileNumber.setVisibility(View.VISIBLE);
        } else {
            viewBinding.nameTextView.setText("Select Customer");
            viewBinding.mobileNumber.setVisibility(View.GONE);
        }
    }

    public void setFooterCheck(boolean check) {
        Menu naveMenu2 = viewBinding.bottomNav.getMenu();
        if (check) {
           // naveMenu2.findItem(R.id.nav_add_to_bag).setVisible(false);
        } else {
            //naveMenu2.findItem(R.id.nav_add_to_bag).setVisible(true);
        }
    }

    public void showBottomNavigation() {
        viewBinding.bottomNav.setVisibility(View.VISIBLE);
    }

    public void hideBottomNavigation() {
        viewBinding.bottomNav.setVisibility(View.GONE);
    }
}