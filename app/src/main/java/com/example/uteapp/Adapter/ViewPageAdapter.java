package com.example.uteapp.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.uteapp.Fragment.HomeFragment;
import com.example.uteapp.Fragment.ProfileFragment;
import com.example.uteapp.Fragment.QRFragment;
import com.example.uteapp.Fragment.RoomFragment;
import com.example.uteapp.Fragment.SearchFragment;


public class ViewPageAdapter extends FragmentStatePagerAdapter {
    public ViewPageAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public ViewPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return new SearchFragment();
            case 2:
                return new RoomFragment();
            case 3:
                return new ProfileFragment();
            case 4:
                return new QRFragment();
            default: return new HomeFragment();
        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}
