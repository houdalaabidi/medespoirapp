package com.touchlink.medespoir.MVP.views.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.denzcoskun.imageslider.ImageSlider;
import com.google.android.material.tabs.TabLayout;
import com.touchlink.medespoir.MVP.views.ui.fragments.ArticlesFragment;
import com.touchlink.medespoir.MVP.views.ui.fragments.InterventionsFragment;
import com.touchlink.medespoir.MVP.views.ui.fragments.VideosFragment;

public class HomeAdapter extends FragmentPagerAdapter {
    int totalTabs;
    private ImageSlider imageSlider;
    public HomeAdapter(FragmentManager fm, int totalTabs, ImageSlider imageSlider) {
        super(fm);
        this.totalTabs = totalTabs;
        this.imageSlider = imageSlider ;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                 ArticlesFragment articlesFragment = new ArticlesFragment();
                 return articlesFragment;
            case 1:

                 InterventionsFragment interventionsFragment = new InterventionsFragment();

                 return interventionsFragment;
            case 2:
                VideosFragment videosFragment = new VideosFragment();
                 return videosFragment;
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return totalTabs;
    }
}
