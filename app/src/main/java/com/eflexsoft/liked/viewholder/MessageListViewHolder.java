package com.eflexsoft.liked.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eflexsoft.liked.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageListViewHolder extends RecyclerView.ViewHolder {

    public CircleImageView proPic;
    public CircleImageView isOnline;
    public TextView name;
    public TextView lastMessage;

    public MessageListViewHolder(@NonNull View itemView) {
        super(itemView);

        proPic = itemView.findViewById(R.id.pro_pic_message);
        isOnline = itemView.findViewById(R.id.id_online);
        name = itemView.findViewById(R.id.name_message);
        lastMessage = itemView.findViewById(R.id.last);

    }
}
