package com.securelocker.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.securelocker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends Fragment {

    private PagerSlidingTabStrip tabs;
    private ViewPager mViewPager;
    private MyPagerAdapter tabPagerAdapter;

    public GalleryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabsSlide);
        mViewPager = (ViewPager) view.findViewById(R.id.viewPagerGallery);
        tabPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(tabPagerAdapter);
        tabPagerAdapter.notifyDataSetChanged();
        tabs.setShouldExpand(true); //Works
        tabs.setViewPager(mViewPager);
        return view;
    }

    class MyPagerAdapter extends FragmentPagerAdapter {
        Fragment fragment;
        final int PAGE_COUNT = 2;
        private String tabTitles[] = new String[] { "Secure", "Insecure" };

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
                case 0:
                    fragment = new GallerySecureFragment();
                    break;
                case 1:
                    fragment = new GalleryInsecureFragment2();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    }

}
