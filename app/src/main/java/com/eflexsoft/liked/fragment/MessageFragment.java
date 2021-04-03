package com.eflexsoft.liked.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eflexsoft.liked.MessageActivity;
import com.eflexsoft.liked.R;
import com.eflexsoft.liked.adapter.MessageAdapter;
import com.eflexsoft.liked.databinding.FragmentMessageBinding;
import com.eflexsoft.liked.model.Like;
import com.eflexsoft.liked.model.MessageList;
import com.eflexsoft.liked.model.User;
import com.eflexsoft.liked.viewholder.MessageListViewHolder;
import com.eflexsoft.liked.viewmodel.MessageViewModel;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;
import com.firebase.ui.database.paging.LoadingState;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.google.GoogleEmojiProvider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class MessageFragment extends Fragment {

    FragmentMessageBinding binding;
    MessageViewModel viewModel;
    MessageAdapter messageAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_message, container, false);
        EmojiManager.install(new GoogleEmojiProvider());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        binding.messageRecycler.setLayoutManager(linearLayoutManager);

        viewModel = ViewModelProviders.of(this).get(MessageViewModel.class);
        viewModel.observeGetChats().observe(getActivity(), new Observer<List<Like>>() {
            @Override
            public void onChanged(List<Like> likes) {

                if (likes.size() <= 0){
                    binding.noMessage.setVisibility(View.VISIBLE);
                }else {
                    binding.noMessage.setVisibility(View.GONE);
                }

                binding.messageLoading.setVisibility(View.GONE);
                messageAdapter = new MessageAdapter(likes, getContext());
                binding.messageRecycler.setAdapter(messageAdapter);

            }
        });

//
//        recyclerView = view.findViewById(R.id.message_recycler);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        progressBar = view.findViewById(R.id.message_loading);
//
//        PagedList.Config builder = new PagedList.Config.Builder()
//                .setInitialLoadSizeHint(20)
//                .setPrefetchDistance(10)
//                .setPageSize(20)
//                .setEnablePlaceholders(false)
//                .build();
//
//        Query query = FirebaseDatabase.getInstance().getReference("ChatId").child(FirebaseAuth.getInstance().getUid());
//
//        DatabasePagingOptions<MessageList> options = new DatabasePagingOptions.Builder<MessageList>()
//                .setLifecycleOwner(this)
//                .setQuery(query, builder, MessageList.class)
//                .build();
//
//        RequestOptions requestOptions = new RequestOptions();
//        requestOptions.error(R.drawable.no_p);
//        requestOptions.placeholder(R.drawable.no_p);
//
//        FirebaseRecyclerPagingAdapter<MessageList, MessageListViewHolder> adapter = new FirebaseRecyclerPagingAdapter<MessageList, MessageListViewHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull MessageListViewHolder viewHolder, int position, @NonNull MessageList model) {
//                viewHolder.lastMessage.setText(model.getLastMessage());
//
//                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        startActivity(new Intent(getContext(), MessageActivity.class).putExtra("id", model.getId())
//                                .putExtra("chatId", model.getChatId()));
//                    }
//                });
//
//                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
//                        .child(model.getId());
//
//                databaseReference.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                        try {
//                            User user = snapshot.getValue(User.class);
//                            viewHolder.name.setText(user.getName());
//                            Glide.with(getActivity()).load(user.getProfilePictureUrl())
//                                    .apply(requestOptions)
//                                    .into(viewHolder.proPic);
//
//                            try {
//                                if (user.getIsOnline().equals("yes")) {
//                                    viewHolder.isOnline.setImageResource(R.color.on_line);
//                                } else {
//                                    viewHolder.isOnline.setImageResource(R.color.off_line);
//                                }
//                            } catch (NullPointerException e) {
//
//                            }
//                        } catch (Exception e) {
//
//                        }
//
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//
//            }
//
//            @Override
//            protected void onLoadingStateChanged(@NonNull LoadingState state) {
//
//                progressBar.setVisibility(View.GONE);
//                switch (state) {
//
//                }
//
//            }
//
//            @NonNull
//            @Override
//            public MessageListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_layout, parent, false);
//                return new MessageListViewHolder(view1);
//
//            }
//
//        };
//
//
//        recyclerView.setAdapter(adapter);

        List<String> love = new ArrayList<>();
        love.add("I have always thought that a person can experience happiness once in a lifetime, but with you, I realized that happiness for me is every minute, every second, every day that I spend with you.");
        love.add("With you, I realized what it means to live life to the fullest and to enjoy every breath");
        love.add("When I’m with you, the only thing I want to do is to hold you tight, keep you warm and never let you go!");
        love.add("Meeting you was fate, becoming your friend was a choice, but falling in love with you was beyond my control.");
        love.add("They say love hurts, but I’m ready to take that risk if I’m going to be with you.");
        love.add("I love seeing you happy and my biggest reward is seeing you smile.");
        love.add("They say you only fall in love once, but every time I look at you, I fall in love all over again.");
        love.add("I’m having one of those days that make me realize how lost I’d be without you.");
        love.add("Words aren’t enough to tell you how wonderful you are. I love you.");
        love.add(" I love you, As I have never loved another or ever will again, I love you with all that I am, and all that I will ever be.");
        love.add("You deserve all of me. You deserve my morning, night and noon. You deserve my present and future.");
        love.add("Just when I thought of giving up to the fate that true love doesn’t exist, you came and showed me the best of it.");
        love.add("My gratitude for having met you is surpassed only by my amazement at the joy you bring to my life.");
        love.add(" No matter what, you will always be my lady, my life, my everything.");
        love.add("Our relationship is the best thing that’s ever happened to me. I love everything about you, inside and out.");

//        for (int i=0;i<=100;i++){
//
//
//
//        String myId = FirebaseAuth.getInstance().getUid();
//        String otherUserId ="CT64EtwbQVckutnK2ClxjEfXY2F2";
//
//        String messageId = String.valueOf(System.currentTimeMillis());
//
//        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
//
//        DocumentReference myReference = firebaseFirestore.collection("Users")
//                .document(myId).collection("Likes").document(otherUserId);
//
//        Map<String, String> myMap = new HashMap<>();
//        myMap.put("userId", otherUserId);
//        myMap.put("messageId", messageId);
//
//        myReference.set(myMap);
//
//        DocumentReference otherReference = firebaseFirestore.collection("Users")
//                .document(otherUserId).collection("Likes").document(myId);
//
//        Map<String, String> otherMap = new HashMap<>();
//        otherMap.put("userId", myId);
//        otherMap.put("messageId", messageId);
//
//        otherReference.set(otherMap);
//
//        CollectionReference message = firebaseFirestore.collection("Messages").document(messageId).collection(messageId);
//
//        long e = System.currentTimeMillis();
//
//        String chatId = String.valueOf(i);
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ENGLISH);
//
//        String time = dateFormat.format(Calendar.getInstance().getTime());
//
//        Map<String, Object> messageMap = new HashMap<>();
//        messageMap.put("firstId", myId);
//        messageMap.put("secondId", otherUserId);
//        messageMap.put("firstIdSeen",false);
//        messageMap.put("secondIdSeen",false);
//        messageMap.put("message", love.get(new Random().nextInt(14)));
//        messageMap.put("imageUrl", "no image");
//        messageMap.put("videoUrl", "no video");
//        messageMap.put("date",time);
//        messageMap.put("chatId", e);
//
//        message.document(chatId).set(messageMap);
//
//
//        }
        return binding.getRoot();

    }
}