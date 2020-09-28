package com.eflexsoft.liked.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eflexsoft.liked.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatViewHolder extends RecyclerView.ViewHolder {

    public RelativeLayout messageLayout;
    public TextView message;
    public TextView date;
    public TextView sent;
    public ImageView imageView;
//    public CircleImageView proPicChat;

    public ChatViewHolder(@NonNull View itemView) {
        super(itemView);

        messageLayout = itemView.findViewById(R.id.sentText);
        message = itemView.findViewById(R.id.message_text_chat);
        date = itemView.findViewById(R.id.sentDate);
        sent = itemView.findViewById(R.id.isSent);
//        proPicChat = itemView.findViewById(R.id.proPicChat);

        imageView = itemView.findViewById(R.id.sentImage);

    }
}
