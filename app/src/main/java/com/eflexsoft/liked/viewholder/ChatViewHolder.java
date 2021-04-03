package com.eflexsoft.liked.viewholder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eflexsoft.liked.R;

public class ChatViewHolder extends RecyclerView.ViewHolder {

    //    public RelativeLayout messageLayout;
    public TextView message;
    public TextView date;
    public ImageView sent;
    public TextView date2;
    public ImageView sent2;
    public ImageView imageView;
    //    public CircleImageView proPicChat;
    public RelativeLayout relativeLayout;


    public ChatViewHolder(@NonNull View itemView) {
        super(itemView);

//        messageLayout = itemView.findViewById(R.id.sentText);
        message = itemView.findViewById(R.id.message_text_chat);
        date = itemView.findViewById(R.id.sentDateAgo);
        sent = itemView.findViewById(R.id.isSent);
        date2 = itemView.findViewById(R.id.sentDateAgo2);
        sent2 = itemView.findViewById(R.id.isSent2);
//        proPicChat = itemView.findViewById(R.id.proPicChat);

        imageView = itemView.findViewById(R.id.sentImage);
        relativeLayout = itemView.findViewById(R.id.img);

    }
}
