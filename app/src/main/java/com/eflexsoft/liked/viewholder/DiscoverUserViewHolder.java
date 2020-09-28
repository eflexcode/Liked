package com.eflexsoft.liked.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eflexsoft.liked.R;

public class DiscoverUserViewHolder extends RecyclerView.ViewHolder {

    public ImageView proPic;
    public TextView name;
    public TextView age;
    public TextView about;
    public TextView address;
    public ProgressBar progressBar;
    public ImageView heart;
    public ImageView heart_count;

    public DiscoverUserViewHolder(@NonNull View itemView) {
        super(itemView);

        proPic = itemView.findViewById(R.id.pro_pic_other_user);
        name = itemView.findViewById(R.id.name);
        age = itemView.findViewById(R.id.age);
        about = itemView.findViewById(R.id.about);
        address = itemView.findViewById(R.id.address);
        progressBar = itemView.findViewById(R.id.imageIsLoading);
        heart = itemView.findViewById(R.id.heart_image);
        heart_count = itemView.findViewById(R.id.heart_image_Count);
    }
}
