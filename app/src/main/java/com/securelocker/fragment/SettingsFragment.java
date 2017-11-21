package com.securelocker.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.omadahealth.lollipin.lib.managers.AppLock;
import com.securelocker.R;
import com.securelocker.activities.CustomPinActivity;

public class SettingsFragment extends Fragment implements View.OnClickListener {

    LinearLayout update_pwd_layout, faq_layout, feedback_layout, about_us_layout, terms_condition_layout, share_layout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        update_pwd_layout = (LinearLayout) rootView.findViewById(R.id.update_pwd_layout);
        faq_layout = (LinearLayout) rootView.findViewById(R.id.faq_layout);
        feedback_layout = (LinearLayout) rootView.findViewById(R.id.feedback_layout);
        about_us_layout = (LinearLayout) rootView.findViewById(R.id.about_us_layout);
        terms_condition_layout = (LinearLayout) rootView.findViewById(R.id.terms_condition_layout);
        share_layout = (LinearLayout) rootView.findViewById(R.id.share_layout);

        setListeners();

        return rootView;
    }

    private void setListeners() {
        update_pwd_layout.setOnClickListener(this);
        faq_layout.setOnClickListener(this);
        feedback_layout.setOnClickListener(this);
        about_us_layout.setOnClickListener(this);
        terms_condition_layout.setOnClickListener(this);
        share_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_pwd_layout:
                Intent intent = new Intent(getActivity(), CustomPinActivity.class);
                intent.putExtra(AppLock.EXTRA_TYPE, AppLock.CHANGE_PIN);
                startActivity(intent);
                break;

            case R.id.faq_layout:
                break;

            case R.id.feedback_layout:
                break;

            case R.id.about_us_layout:
                break;

            case R.id.terms_condition_layout:
                break;

            case R.id.share_layout:
                break;

            default:
                break;
        }
    }
}
