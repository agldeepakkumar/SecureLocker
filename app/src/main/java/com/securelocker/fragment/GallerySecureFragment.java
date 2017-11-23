package com.securelocker.fragment;


import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.securelocker.R;
import com.securelocker.activities.MainActivity;
import com.securelocker.adapter.GallerySecureFragmentAdapter;
import com.securelocker.database.MySqliteOpenHelper;
import com.securelocker.model.GalleryFragmentItem;
import com.securelocker.provider.GalleryImageDataProvider;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public  class GallerySecureFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private FloatingActionButton fab;
    private MainActivity activity;
    private View rootView;
    private RecyclerView rvGallery;
    private ArrayList<GalleryFragmentItem> listImagePath;
    private GallerySecureFragmentAdapter gallaryFragmentAdapter;
    public static boolean isSelectOptionClicked;

    public GallerySecureFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = (MainActivity) getActivity();
        activity.getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView  = inflater.inflate(R.layout.fragment_gallery_secure, container, false);
        initView();
        if(savedInstanceState == null || !savedInstanceState.containsKey("imagesList")) {
            listImagePath = new ArrayList<>();
        } else {
            Bundle bundle = savedInstanceState.getBundle("data");
            if(bundle != null ){
                listImagePath = (ArrayList<GalleryFragmentItem>) bundle.getSerializable("imageList");
            }
        }

        gallaryFragmentAdapter = new GallerySecureFragmentAdapter(getActivity(), listImagePath);
        rvGallery.setAdapter(gallaryFragmentAdapter);
        setHasOptionsMenu(true);
        return rootView;

    }

    private void initView() {
        rvGallery = (RecyclerView) rootView.findViewById(R.id.rvGallery);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        rvGallery.setLayoutManager(gridLayoutManager);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String selection = MySqliteOpenHelper.ENCRYPTION_VALUE + " = ? ";
        String[] selectionArgs = {"1"};
        String[] projection = {MySqliteOpenHelper.COLUMN_ID, MySqliteOpenHelper.PATH,MySqliteOpenHelper.ENCRYPTION_VALUE};
        CursorLoader cursorLoader = new CursorLoader(getActivity(),
                GalleryImageDataProvider.CONTENT_URI, projection, selection, selectionArgs, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null && cursor.getCount() > 0) {
            Log.e("Cursor Length", ""+cursor.getCount());
            if(listImagePath != null) {
                listImagePath.clear();
            } else {
                listImagePath = new ArrayList<>();
            }

            cursor.moveToPosition(-1);
            while (cursor.moveToNext()) {
                String imagePath = cursor.getString(cursor.getColumnIndex(MySqliteOpenHelper.PATH));
                String id = cursor.getString(cursor.getColumnIndex(MySqliteOpenHelper.COLUMN_ID));
                int encryptionValue = cursor.getInt(cursor.getColumnIndex(MySqliteOpenHelper.ENCRYPTION_VALUE));
                GalleryFragmentItem galleryFragmentItem = new GalleryFragmentItem(id, imagePath, false, encryptionValue);
                listImagePath.add(galleryFragmentItem);
            }
        }

        if(listImagePath.size() > 0 ) gallaryFragmentAdapter.updateList(listImagePath);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.menuEncrypt).setTitle("Decrypt");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuSelect:
                isSelectOptionClicked = true;
                break;
            case R.id.menuSelectAll:
                isSelectOptionClicked = true;
                for (GalleryFragmentItem fragmentItem : listImagePath) {
                    fragmentItem.setSelected(true);
                }
                gallaryFragmentAdapter.notifyDataSetChanged();
                break;
            case R.id.menuEncrypt:
                decrypt();
                break;
            case R.id.menuDelete:
                String selection = "";
                ArrayList<String> selectedImagePath = new ArrayList<>();

                for (int i = 0; i < listImagePath.size(); i++) {
                    GalleryFragmentItem fragmentItem = listImagePath.get(i);
                    if (fragmentItem.isSelected()) {
                        selectedImagePath.add(fragmentItem.getImagePath());
                    }
                }
                if (selectedImagePath.size() > 0) {
                    for (int j = 0; j < selectedImagePath.size(); j++) {
                        if (j == selectedImagePath.size() - 1) {
                            selection = selection + MySqliteOpenHelper.PATH + " = ? ";
                        } else {
                            selection = selection + MySqliteOpenHelper.PATH + " = ? OR ";
                        }
                    }
                    Log.d("SET", selection);
                    String[] whereArgs = selectedImagePath.toArray(new String[0]);
                    getActivity().getContentResolver().delete(GalleryImageDataProvider.CONTENT_URI, selection, whereArgs);
                    isSelectOptionClicked = false;
                } else
                    Toast.makeText(getActivity(), "Please select at least one image", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void decrypt() {
        String selection = "";
        ArrayList<String> selectedImagePath = new ArrayList<>();

        for (int i = 0; i < listImagePath.size(); i++) {
            GalleryFragmentItem fragmentItem = listImagePath.get(i);
            if (fragmentItem.isSelected()) {
                selectedImagePath.add(fragmentItem.getImagePath());
            }
        }
        if (selectedImagePath.size() > 0) {
            for (int j = 0; j < selectedImagePath.size(); j++) {
                if (j == selectedImagePath.size() - 1) {
                    selection = selection + MySqliteOpenHelper.PATH + " = ? ";
                } else {
                    selection = selection + MySqliteOpenHelper.PATH + " = ? OR ";
                }
            }
            Log.d("SET", selection);
            String[] whereArgs = selectedImagePath.toArray(new String[0]);
            ContentValues cv = new ContentValues();
            cv.put(MySqliteOpenHelper.ENCRYPTION_VALUE, 0);
            getActivity().getContentResolver().update(GalleryImageDataProvider.CONTENT_URI, cv, selection, whereArgs);
            isSelectOptionClicked = false;
        } else
            Toast.makeText(getActivity(), "Please select at least one image", Toast.LENGTH_SHORT).show();
    }
}
