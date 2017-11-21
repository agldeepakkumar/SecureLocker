package com.securelocker.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.securelocker.R;
import com.securelocker.adapter.GalleryFragmentAdapter;
import com.securelocker.database.MySqliteOpenHelper;
import com.securelocker.provider.GalleryImageDataProvider;
import com.securelocker.util.Utils;

import java.io.File;
import java.util.ArrayList;

public class GalleryFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    RecyclerView rvGallery;
    FloatingActionButton fabAdd;
    private Uri srcImageUri = null;
    View parentView;
    private ArrayList<String> listImagePath;
    GalleryFragmentAdapter gallaryFragmentAdapter;
    public final int REQUEST_CAMERA = 0x1, CHOOSE_IMAGE_REQUEST = 0x2;
    public final int GALLERY_PERMISSION_CODE = 0x3, CAMERA_PERMISSION_CODE = 0x4;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getLoaderManager().initLoader(0, null, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        setRetainInstance(true);

        parentView = view.findViewById(R.id.parentView);
        rvGallery = (RecyclerView) view.findViewById(R.id.rvGallery);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        rvGallery.setLayoutManager(gridLayoutManager);
        fabAdd = (FloatingActionButton) view.findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(this);

        if(savedInstanceState == null || !savedInstanceState.containsKey("imagesList")) {
            listImagePath = new ArrayList<>();
        } else {
            listImagePath = savedInstanceState.getStringArrayList("imagesList");
        }

        gallaryFragmentAdapter = new GalleryFragmentAdapter(getActivity(), listImagePath);
        rvGallery.setAdapter(gallaryFragmentAdapter);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("imagesList", listImagePath);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabAdd:
                AlertDialog builder = new showFileChooserDialog(getActivity());
                builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                builder.getWindow().setGravity(Gravity.BOTTOM);
                builder.getWindow().getAttributes().windowAnimations = R.style.dialogAnimate;
                builder.setCanceledOnTouchOutside(false);
                builder.setCancelable(true);
                builder.show();
                break;

            default:
                break;
        }
    }

    private class showFileChooserDialog extends AlertDialog {
        protected showFileChooserDialog(final Context context) {
            super(context);
            LayoutInflater inflater = getLayoutInflater();
            final View dialogLayout = inflater.inflate(R.layout.custom_file_chooser_layout, (ViewGroup) getCurrentFocus());
            setView(dialogLayout);
            final TextView textViewCamera = (TextView) dialogLayout.findViewById(R.id.textview_camera);
            textViewCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    if(Utils.canAccessCamera(getActivity())) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                            try {
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
                                    srcImageUri = FileProvider.getUriForFile(getActivity(), "com.securelocker.provider",
                                            Utils.getOutputMediaFile());
                                } else {
                                    srcImageUri= Uri.fromFile(Utils.getOutputMediaFile());
                                }
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, srcImageUri);
                                startActivityForResult(intent, REQUEST_CAMERA);
                            } catch (ActivityNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION_CODE);
                    }
                }
            });
            final TextView textViewGallery = (TextView) dialogLayout.findViewById(R.id.textview_gallery);
            textViewGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    if(Utils.canAccessGallery(getActivity())) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), CHOOSE_IMAGE_REQUEST);
                    } else {
                        requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY_PERMISSION_CODE);
                    }
                }
            });

            final TextView textviewNegative = (TextView) dialogLayout.findViewById(R.id.textview_negative);
            textviewNegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_PERMISSION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the task you need to do.
                    if(Utils.canAccessCamera(getActivity())) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                            try {
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
                                    srcImageUri = FileProvider.getUriForFile(getActivity(), "com.securelocker.provider",
                                            Utils.getOutputMediaFile());
                                } else {
                                    srcImageUri= Uri.fromFile(Utils.getOutputMediaFile());
                                }
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, srcImageUri);
                                startActivityForResult(intent, REQUEST_CAMERA);
                            } catch (ActivityNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        requestPermissions(new String[]{Manifest.permission.CAMERA,
                                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                CAMERA_PERMISSION_CODE);
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    AlertDialog builder = new showPermissionAlert(getActivity(), getResources().getString(R.string.camera_permission),
                            new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION_CODE);
                    builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    builder.setCanceledOnTouchOutside(false);
                    builder.setCancelable(false);
                    builder.show();
                }
                return;
            }

            case GALLERY_PERMISSION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(Utils.canAccessGallery(getActivity())) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), CHOOSE_IMAGE_REQUEST);
                    } else {
                        requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY_PERMISSION_CODE);
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    AlertDialog builder = new showPermissionAlert(getActivity(), getResources().getString(R.string.gallery_permission), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            GALLERY_PERMISSION_CODE);
                    builder.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    builder.setCanceledOnTouchOutside(false);
                    builder.setCancelable(false);
                    builder.show();
                }
                return;
            }

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private class showPermissionAlert extends AlertDialog {
        protected showPermissionAlert(Context context, String message, final String[] permissions, final int request_code) {
            super(context);
            LayoutInflater inflater = getLayoutInflater();
            final View dialoglayout = inflater.inflate(R.layout.custom_alert_layout_single, (ViewGroup) getCurrentFocus());
            setView(dialoglayout);
            final TextView textviewTitle = (TextView) dialoglayout.findViewById(R.id.textview_title);
            textviewTitle.setText(getResources().getString(R.string.app_name));
            final TextView textviewMessage = (TextView) dialoglayout.findViewById(R.id.textview_text);
            textviewMessage.setText(message);
            final TextView textviewPositive = (TextView) dialoglayout.findViewById(R.id.textview_positive);
            textviewPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    requestPermissions(permissions, request_code);
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == CHOOSE_IMAGE_REQUEST && data != null && data.getData() != null) {
                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {
                if(srcImageUri != null) {
                    try {
                        String filePath = srcImageUri.getPath();
                        if(!new File(filePath).exists())
                            return;

                    } catch(Exception e) {
                        Snackbar.make(parentView, "File format not supported.", Snackbar.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                } else {
                    Snackbar.make(parentView, "File format not supported.", Snackbar.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        try {
            String selectedImagePath = Utils.getPath(getActivity(), data.getData());
            Log.d("GALLERY",selectedImagePath);
            ContentValues cv = new ContentValues();
            cv.put(MySqliteOpenHelper.PATH,selectedImagePath);
            getActivity().getContentResolver().insert(GalleryImageDataProvider.CONTENT_URI,cv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {MySqliteOpenHelper.COLUMN_ID, MySqliteOpenHelper.PATH};
        CursorLoader cursorLoader = new CursorLoader(getActivity(),
                GalleryImageDataProvider.CONTENT_URI, projection, null, null, null);
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

            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                String imagePath = cursor.getString(cursor.getColumnIndex(MySqliteOpenHelper.PATH));
                listImagePath.add(imagePath);
            }
        }

        if(listImagePath.size() > 0 ) gallaryFragmentAdapter.updateList(listImagePath);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
