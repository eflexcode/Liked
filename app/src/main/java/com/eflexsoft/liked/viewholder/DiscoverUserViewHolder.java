package com.eflexsoft.liked.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eflexsoft.liked.R;
import com.eflexsoft.liked.databinding.DicoverItemLayoutBinding;

public class DiscoverUserViewHolder extends RecyclerView.ViewHolder {

    public DicoverItemLayoutBinding binding;

    public DiscoverUserViewHolder(@NonNull DicoverItemLayoutBinding binding) {
        super(binding.getRoot());

        this.binding = binding;

    }
}
