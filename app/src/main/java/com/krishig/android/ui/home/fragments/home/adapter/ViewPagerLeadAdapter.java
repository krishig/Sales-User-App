package com.krishig.android.ui.home.fragments.home.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.krishig.android.ui.home.fragments.home.fragments.FirstFragment;
import com.krishig.android.ui.home.fragments.home.fragments.SecondFragment;


public class ViewPagerLeadAdapter extends FragmentStateAdapter {
    public ViewPagerLeadAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);

    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new FirstFragment();
        } else if (position == 1) {
            return new SecondFragment();
        }
        return new FirstFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

