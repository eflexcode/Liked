package com.eflexsoft.liked.viewholder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eflexsoft.liked.databinding.UserImageItemSlideImageItemBinding;

public class UserItemImageViewHolder extends RecyclerView.ViewHolder {

   public UserImageItemSlideImageItemBinding binding;

    public UserItemImageViewHolder(@NonNull UserImageItemSlideImageItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
