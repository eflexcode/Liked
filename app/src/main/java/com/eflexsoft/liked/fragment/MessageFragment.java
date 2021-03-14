package com.eflexsoft.liked.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.eflexsoft.liked.MessageActivity;
import com.eflexsoft.liked.R;
import com.eflexsoft.liked.databinding.FragmentMessageBinding;
import com.eflexsoft.liked.model.MessageList;
import com.eflexsoft.liked.model.User;
import com.eflexsoft.liked.viewholder.MessageListViewHolder;
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

public class MessageFragment extends Fragment {

   FragmentMessageBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_message,container,false);

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

        return binding.getRoot();

    }
}