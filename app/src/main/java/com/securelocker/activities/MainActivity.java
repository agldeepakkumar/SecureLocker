package com.securelocker.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.securelocker.BaseActivity;
import com.securelocker.R;
import com.securelocker.decoration.ActionItem;
import com.securelocker.decoration.QuickAction;
import com.securelocker.fragment.AudioVideoFragment;
import com.securelocker.fragment.DocumentFragment;
import com.securelocker.fragment.GalleryFragment;
import com.securelocker.fragment.NotesFragment;
import com.securelocker.fragment.SettingsFragment;

public class MainActivity extends BaseActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    Toolbar mToolbar;
    DrawerLayout mDrawerLayout;
    NavigationView navigationView;
    int tabPosition;
    TextView toolbarTitle;
    ImageButton moreButton, llGallery;
    RelativeLayout llSettings, llAudioVideo, llDocument, llNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbarTitle = (TextView) mToolbar.findViewById(R.id.title_textView);
        moreButton = (ImageButton) mToolbar.findViewById(R.id.more_options);

        initView();

        if(savedInstanceState == null || !savedInstanceState.containsKey("main_title")) {
            toolbarTitle.setText(getResources().getString(R.string.gallery_title));
            setCurrentTabFragment(2);
        } else {
            toolbarTitle.setText(savedInstanceState.getString("main_title"));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("main_title", toolbarTitle.getText().toString());
        super.onSaveInstanceState(outState);
    }

    private void initView() {
        llSettings = (RelativeLayout) findViewById(R.id.llSettings);
        llAudioVideo = (RelativeLayout) findViewById(R.id.llAudioVideo);
        llDocument = (RelativeLayout) findViewById(R.id.llDocument);
        llNotes = (RelativeLayout) findViewById(R.id.llNotes);
        llGallery = (ImageButton) findViewById(R.id.llGallery);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.getHeaderView(0);
        headerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }
            }
        });
        navigationView.setItemIconTintList(null);

        llSettings.setOnClickListener(this);
        llAudioVideo.setOnClickListener(this);
        llDocument.setOnClickListener(this);
        llNotes.setOnClickListener(this);
        llGallery.setOnClickListener(this);
        moreButton.setOnClickListener(this);
    }

    private void setCurrentTabFragment(int position) {
        tabPosition = position;
        switch (position) {
            case 0:
                replaceFragment(new AudioVideoFragment());
                break;
            case 1:
                replaceFragment(new DocumentFragment());
                break;
            case 2:
                replaceFragment(new GalleryFragment());
                break;
            case 3:
                replaceFragment(new NotesFragment());
                break;
            case 4:
                replaceFragment(new SettingsFragment());
                break;
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_container, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commitAllowingStateLoss();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.gallery_menu, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llSettings:
                toolbarTitle.setText(getResources().getString(R.string.settings));
                setCurrentTabFragment(4);
                break;

            case R.id.llAudioVideo:
                toolbarTitle.setText(getResources().getString(R.string.audio_video));
                setCurrentTabFragment(0);
                break;

            case R.id.llDocument:
                toolbarTitle.setText(getResources().getString(R.string.document));
                setCurrentTabFragment(1);
                break;

            case R.id.llNotes:
                toolbarTitle.setText(getResources().getString(R.string.notes));
                setCurrentTabFragment(3);
                break;

            case R.id.llGallery:
                toolbarTitle.setText(getResources().getString(R.string.gallery_title));
                setCurrentTabFragment(2);
                break;

            case R.id.more_options:
                showQuickAction(view, getResources().getStringArray(R.array.more_options));
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showQuickAction(final View view, final String[] items) {
        final QuickAction quickAction = new QuickAction(this, QuickAction.VERTICAL);
        for(int i=0; i<items.length; i++) {
            ActionItem actionItem = new ActionItem(items[i]);
            actionItem.setActionId(i);
            quickAction.addActionItem(actionItem);
            quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
                @Override
                public void onItemClick(QuickAction source, int pos, int actionId) {
                    if(source.getActionItem(pos).getTitle().equals(items[0])) {

                    } else if(source.getActionItem(pos).getTitle().equals(items[1])) {

                    } else {

                    }
                }
            });
        }
        quickAction.show(view);
        quickAction.setAnimStyle(QuickAction.ANIM_GROW_FROM_RIGHT);
    }
}
