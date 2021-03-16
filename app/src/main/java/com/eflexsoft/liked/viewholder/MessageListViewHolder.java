package com.eflexsoft.liked.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eflexsoft.liked.R;
import com.eflexsoft.liked.databinding.MessageLayoutBinding;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageListViewHolder extends RecyclerView.ViewHolder {

 public  MessageLayoutBinding binding;

    public MessageListViewHolder(MessageLayoutBinding binding) {
        super(binding.getRoot());

        this.binding = binding;

    }
}
