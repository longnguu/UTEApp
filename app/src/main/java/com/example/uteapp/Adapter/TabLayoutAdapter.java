package com.example.uteapp.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class TabLayoutAdapter extends FragmentStateAdapter {

    private final String[] tabTitles;
    private final Fragment[] fragments;

    public TabLayoutAdapter(@NonNull FragmentActivity fragmentActivity, String[] tabTitles, Fragment[] fragments) {
        super(fragmentActivity);
        this.tabTitles = tabTitles;
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments[position];
    }

    @Override
    public int getItemCount() {
        return fragments.length;
    }

    public void setupWithTabLayout(@NonNull TabLayout tabLayout, @NonNull ViewPager2 viewPager2) {
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            tab.setText(tabTitles[position]);
        }).attach();
    }
}