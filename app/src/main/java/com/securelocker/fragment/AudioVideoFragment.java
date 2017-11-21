package com.securelocker.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.securelocker.R;
import com.securelocker.adapter.MyPagerAdapter;

public class AudioVideoFragment extends Fragment {

    private PagerSlidingTabStrip tabs;
    ViewPager mViewPager;
    MyPagerAdapter tabPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_audio_video, container, false);
        tabs = (PagerSlidingTabStrip) rootView.findViewById(R.id.tabs);
        mViewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        tabPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(tabPagerAdapter);
        tabPagerAdapter.notifyDataSetChanged();
        tabs.setShouldExpand(true); //Works
        tabs.setViewPager(mViewPager);

        setListeners();

        return rootView;
    }

    private void setListeners() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
