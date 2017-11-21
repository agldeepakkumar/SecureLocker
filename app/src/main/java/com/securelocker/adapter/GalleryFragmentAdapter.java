package com.securelocker.adapter;


import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.securelocker.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class GalleryFragmentAdapter extends RecyclerView.Adapter<GalleryFragmentAdapter.MyViewHolder> {


    private boolean isLongPressed;
    private ArrayList<String> imagePathList;
    private Context context;

    public GalleryFragmentAdapter(Context context, ArrayList<String> imagePathList){
        this.imagePathList = imagePathList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String filePath = imagePathList.get(position);
        File file = new File(filePath);
        if(file.exists()) {
            Uri imageUri = Uri.fromFile(file);
            try {
                Picasso.with(context).load(imageUri).into(holder.imgPic);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return imagePathList.size();
    }

    public void updateList(ArrayList<String> imagePathList) {
        this.imagePathList = imagePathList;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        ImageView imgPic;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
            imgPic = (ImageView) itemView.findViewById(R.id.imgPicture);
        }

        @Override
        public boolean onLongClick(View view) {
            if(isLongPressed) return true;
            else {
                isLongPressed = true;
                return true;
            }
        }

        @Override
        public void onClick(View view) {

        }
    }
}
