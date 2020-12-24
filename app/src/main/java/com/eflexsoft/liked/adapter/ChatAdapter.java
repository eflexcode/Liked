package com.eflexsoft.liked.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.eflexsoft.liked.R;
import com.eflexsoft.liked.model.Chat;
import com.eflexsoft.liked.model.User;
import com.eflexsoft.liked.viewholder.ChatViewHolder;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {


    private static final int meLayout = 7;
    private static final int youLayout = 8;

    Context context;
    String youImageUrl;
    List<Chat> list = new ArrayList<>();
    boolean isFirstAdd = true;

    public ChatAdapter(Context context) {
        this.context = context;
        this.youImageUrl = youImageUrl;
    }

//    static DiffUtil.ItemCallback<Chat> callback = new DiffUtil.ItemCallback<Chat>() {
//        @Override
//        public boolean areItemsTheSame(@NonNull Chat oldItem, @NonNull Chat newItem) {
//            return oldItem.getIsSeen() == newItem.getIsSeen()
//                    && oldItem.getDate().equals(newItem.getDate())
//                    && oldItem.getImageUrl().equals(newItem.getImageUrl())
//                    && oldItem.getReceiverId().equals(newItem.getReceiverId())
//                    && oldItem.getSenderId().equals(newItem.getSenderId())
//                    && oldItem.getMessage().equals(newItem.getMessage());
//        }
//
//        @Override
//        public boolean areContentsTheSame(@NonNull Chat oldItem, @NonNull Chat newItem) {
//            return oldItem.equals(newItem);
//        }
//    };

    @Override
    public int getItemViewType(int position) {
        Chat chat = list.get(position);

        if (chat.getSenderId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            return 7;
        } else {
            return 9;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if (viewType == 7) {
            view = LayoutInflater.from(context).inflate(R.layout.me_layout, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.you_layout, parent, false);
        }
        return new ChatViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
//        Glide.with(context).load(youImageUrl).into(holder.proPicChat);
        Chat chat = list.get(position);
        if (!chat.getImageUrl().equals("null")) {
//            holder.messageLayout.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.VISIBLE);
            holder.message.setVisibility(View.GONE);
            holder.date.setVisibility(View.GONE);
            Glide.with(context).load(chat.getImageUrl()).into(holder.imageView);

        } else {

//            holder.messageLayout.setVisibility(View.VISIBLE);
            holder.imageView.setVisibility(View.GONE);
            holder.message.setVisibility(View.VISIBLE);
            holder.date.setVisibility(View.VISIBLE);

            holder.message.setText(chat.getMessage());
            holder.date.setText(chat.getDate());

            if (position == list.size() - 1 && chat.getSenderId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

                if (chat.getIsSeen()) {
                    holder.sent.setText("seen");
                } else {
                    holder.sent.setText("sent");
                }

                holder.sent.setVisibility(View.VISIBLE);
            } else {
                holder.sent.setVisibility(View.GONE);
            }

        }

    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {

        //        public RelativeLayout messageLayout;
        public TextView message;
        public TextView date;
        public TextView sent;
        public ImageView imageView;
//    public CircleImageView proPicChat;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);

//            messageLayout = itemView.findViewById(R.id.sentText);
            message = itemView.findViewById(R.id.message_text_chat);
            date = itemView.findViewById(R.id.sentDate);
            sent = itemView.findViewById(R.id.isSent);
//        proPicChat = itemView.findViewById(R.id.proPicChat);

            imageView = itemView.findViewById(R.id.sentImage);

        }
    }

    public void setChats(List<Chat> chats) {

        this.list = chats;

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
        notifyItemInserted(list.size());
//            }
//        }, 2000);
    }
}
