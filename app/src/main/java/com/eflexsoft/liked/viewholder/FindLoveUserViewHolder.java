package com.eflexsoft.liked.viewholder;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eflexsoft.liked.databinding.FindLoveItemBinding;

public class FindLoveUserViewHolder extends RecyclerView.ViewHolder {

   public FindLoveItemBinding binding;

    public FindLoveUserViewHolder(FindLoveItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }


}
