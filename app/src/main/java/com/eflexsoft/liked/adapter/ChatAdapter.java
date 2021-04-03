package com.eflexsoft.liked.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.eflexsoft.liked.ImageFullViewActivity;
import com.eflexsoft.liked.R;
import com.eflexsoft.liked.model.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {


    private static final int meLayout = 7;
    private static final int youLayout = 8;

    Context context;
    String youImageUrl;
    List<Chat> list = new ArrayList<>();
    boolean isFirstAdd = true;
    Handler handler = new Handler();

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

        if (chat.getFirstId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
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
        if (!chat.getImageUrl().equals("no image")) {
            holder.message.setVisibility(View.GONE);
            holder.date.setVisibility(View.GONE);

            holder.date2.setVisibility(View.VISIBLE);
            holder.sent2.setVisibility(View.VISIBLE);

            holder.relativeLayout.setVisibility(View.VISIBLE);
            Glide.with(context).load(chat.getImageUrl()).into(holder.imageView);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ENGLISH);

            PrettyTime prettyTime = new PrettyTime(Locale.getDefault());

            try {
                if (chat.getDate() != null) {
                    Date date = dateFormat.parse(chat.getDate());
                    holder.date2.setText(prettyTime.format(date));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (position == list.size() - 1 && chat.getFirstId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

                if (!chat.getSecondId().equals(FirebaseAuth.getInstance().getUid())) {

                    if (chat.isSecondIdSeen()) {

                        holder.sent2.setImageResource(R.drawable.ic_double_check);
                    }
                }
                holder.sent2.setVisibility(View.VISIBLE);

            } else {
                holder.sent2.setVisibility(View.GONE);
            }

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ImageFullViewActivity.class);
                    intent.putExtra("url", chat.getImageUrl());

                    Pair<View, String> pair = Pair.create(holder.imageView, holder.imageView.getTransitionName());

                    ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, pair);
                    context.startActivity(intent, activityOptionsCompat.toBundle());
                }
            });

        } else {

//            holder.messageLayout.setVisibility(View.VISIBLE);
            holder.relativeLayout.setVisibility(View.GONE);
            holder.date2.setVisibility(View.GONE);
            holder.sent2.setVisibility(View.GONE);
            holder.message.setVisibility(View.VISIBLE);
            holder.date.setVisibility(View.VISIBLE);

            holder.message.setText(chat.getMessage());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ENGLISH);

            PrettyTime prettyTime = new PrettyTime(Locale.getDefault());

            try {
                if (chat.getDate() != null) {
                    Date date = dateFormat.parse(chat.getDate());
                    holder.date.setText(prettyTime.format(date));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }


            if (position == list.size() - 1 && chat.getFirstId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

                if (!chat.getSecondId().equals(FirebaseAuth.getInstance().getUid())) {
                    if (chat.isSecondIdSeen()) {

                        holder.sent.setImageResource(R.drawable.ic_double_check);
                    }
                }

                holder.sent.setVisibility(View.VISIBLE);

            } else {
                holder.sent.setVisibility(View.GONE);
            }

        }

    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {

        public TextView message;
        public TextView date;
        public ImageView sent;
        public ImageView imageView;
        public TextView date2;
        public ImageView sent2;
        public RelativeLayout relativeLayout;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);

//            messageLayout = itemView.findViewById(R.id.sentText);
            message = itemView.findViewById(R.id.message_text_chat);
            date = itemView.findViewById(R.id.sentDateAgo);
            sent = itemView.findViewById(R.id.isSent);
//        proPicChat = itemView.findViewById(R.id.proPicChat);

            imageView = itemView.findViewById(R.id.sentImage);
            date2 = itemView.findViewById(R.id.sentDateAgo2);
            sent2 = itemView.findViewById(R.id.isSent2);
            relativeLayout = itemView.findViewById(R.id.img);

        }
    }

    public void setChatsMore(List<Chat> chats) {

        this.list = chats;
        notifyDataSetChanged();

    }

    public void setChats(List<Chat> chats) {

        this.list = chats;

        notifyDataSetChanged();


//        notifyDataSetChanged();


    }
}
