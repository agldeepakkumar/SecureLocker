package com.securelocker.adapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.securelocker.R;
import com.securelocker.activities.ViewPictureActivity;
import com.securelocker.model.GalleryFragmentItem;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import static com.securelocker.fragment.GalleryInsecureFragment2.isSelectOptionClicked;


public class GalleryFragmentAdapter extends RecyclerView.Adapter<GalleryFragmentAdapter.MyViewHolder> {


    private boolean isLongPressed;
    private ArrayList<GalleryFragmentItem> imagePathList;
    private Context context;

    public GalleryFragmentAdapter(Context context, ArrayList<GalleryFragmentItem> imagePathList) {
        this.imagePathList = imagePathList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_fragment_adapter_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        GalleryFragmentItem item = imagePathList.get(position);
        String filePath = item.getImagePath();
        File file = new File(filePath);
        if (file.exists()) {
            Uri imageUri = Uri.fromFile(file);
            try {
                Picasso.with(context).load(imageUri).into(holder.imgPic);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (item.getEncryptionValue() == 1) {
            holder.rlBackgroundBlur.setBackgroundResource(R.drawable.ic_encrypt);
            if (item.isSelected()) {
//                holder.rlBackgroundBlur.setBackgroundColor(context.getResources().getColor(R.color.selected_img_bg));
                holder.imgTick.setVisibility(View.VISIBLE);
            } else {
//            holder.rlBackgroundBlur.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
                holder.imgTick.setVisibility(View.GONE);
            }
        } else {
            if (item.isSelected()) {
                holder.rlBackgroundBlur.setBackgroundColor(context.getResources().getColor(R.color.selected_img_bg));
                holder.imgTick.setVisibility(View.VISIBLE);
            } else {
                holder.rlBackgroundBlur.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
                holder.imgTick.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public int getItemCount() {
        return imagePathList.size();
    }

    public void updateList(ArrayList<GalleryFragmentItem> imagePathList) {
        this.imagePathList = imagePathList;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        ImageView imgPic, imgTick;
        FrameLayout flContainer;
        RelativeLayout rlBackgroundBlur;

        public MyViewHolder(View itemView) {
            super(itemView);
//            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
            imgPic = (ImageView) itemView.findViewById(R.id.imgPicture);
            imgTick = (ImageView) itemView.findViewById(R.id.imgTick);
            flContainer = (FrameLayout) itemView.findViewById(R.id.flContainer);
            rlBackgroundBlur = (RelativeLayout) itemView.findViewById(R.id.rlBackgroundBlur);
        }

        @Override
        public boolean onLongClick(View view) {
            if (isLongPressed) return true;
            else {
                isLongPressed = true;
                int itemPosition = getAdapterPosition();
                GalleryFragmentItem item = imagePathList.get(itemPosition);
                if (item.isSelected()) {
                    item.setSelected(false);
                } else item.setSelected(true);
                notifyDataSetChanged();
                return true;
            }
        }

        @Override
        public void onClick(View view) {
            int itemPosition = getAdapterPosition();
            GalleryFragmentItem item = imagePathList.get(itemPosition);
            if (isSelectOptionClicked) {
                if (item.isSelected()) {
                    item.setSelected(false);
                } else item.setSelected(true);
                int countSelected = 0;
                for (GalleryFragmentItem fragmentItem : imagePathList) {
                    if (fragmentItem.isSelected()) {
                        countSelected++;
                    }
                }
                if (countSelected == 0) isSelectOptionClicked = false;
                notifyDataSetChanged();
            } else {
                if (item.getEncryptionValue() == 1) {
                   /* Intent intent = new Intent(context, PasscodeActivity.class);
                    intent.putExtra("imagePath", item.getImagePath());
                    context.startActivity(intent);*/
                    Toast.makeText(context, "This image is encrypted.", Toast.LENGTH_SHORT).show();
                } else openFullImageScreen(item.getImagePath());
            }

        }
    }

    private void openFullImageScreen(String imagePath) {
        Intent intent = new Intent(context, ViewPictureActivity.class);
        intent.putExtra("path", imagePath);
        context.startActivity(intent);
    }
}
