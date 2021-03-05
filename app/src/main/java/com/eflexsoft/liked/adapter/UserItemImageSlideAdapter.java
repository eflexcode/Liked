package com.eflexsoft.liked.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.eflexsoft.liked.R;
import com.eflexsoft.liked.databinding.UserImageItemSlideImageItemBinding;
import com.eflexsoft.liked.model.SlidePostImageItem;
import com.eflexsoft.liked.viewholder.UserItemImageViewHolder;

import java.util.List;

public class UserItemImageSlideAdapter extends RecyclerView.Adapter<UserItemImageViewHolder> {

    List<SlidePostImageItem> urls;
    Context context;

    public UserItemImageSlideAdapter(List<SlidePostImageItem> urls, Context context) {
        this.urls = urls;
        this.context = context;
    }

    @NonNull
    @Override
    public UserItemImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        UserImageItemSlideImageItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.user_image_item_slide_image_item, parent, false);


        return new UserItemImageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserItemImageViewHolder holder, int position) {

        SlidePostImageItem slidePostImageItem = urls.get(position);
//        holder.binding.setUrl(slidePostImageItem);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(R.color.grey);
        requestOptions.placeholder(R.color.grey);

        Glide.with(context).load(slidePostImageItem.getUrl()).addListener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                holder.binding.proBarImg.setVisibility(View.GONE);
//                holder.binding.errorImg.setVisibility(View.VISIBLE);

                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                holder.binding.proBarImg.setVisibility(View.GONE);
//                holder.binding.errorImg.setVisibility(View.GONE);

                return false;
            }
        }).apply(requestOptions).into(holder.binding.img);


    }

    @Override
    public int getItemCount() {
        return urls.size();
    }
}
