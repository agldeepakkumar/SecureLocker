package com.securelocker.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.securelocker.R;
import com.securelocker.activities.MainActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public  class GallerySecureFragment extends Fragment {

    private FloatingActionButton fab;
    private MainActivity activity;
    private View rootView;

    public GallerySecureFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView  = inflater.inflate(R.layout.fragment_gallery_secure, container, false);
        initView();
        return rootView;
    }

    private void initView() {
    }

}
