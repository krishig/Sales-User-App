package com.krishig.android.ui.OrderSuccess;

import com.krishig.android.Navigator.activity.ActivityNavigator;
import com.krishig.android.Navigator.fragment.FragmentNavigator;
import com.krishig.android.databinding.ActivityOrderSuccessBinding;
import com.krishig.android.ui.base.BaseActivity;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class OrderSuccessActivity extends BaseActivity<ActivityOrderSuccessBinding> {


    @Override
    protected ActivityOrderSuccessBinding getViewBinding() {
        return ActivityOrderSuccessBinding.inflate(getLayoutInflater());
    }

    @Override
    protected ActivityNavigator getActivityNavigator() {
        return new ActivityNavigator(OrderSuccessActivity.this);
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
}