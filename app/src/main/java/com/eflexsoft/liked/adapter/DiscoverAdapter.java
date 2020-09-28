package com.eflexsoft.liked.adapter;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.eflexsoft.liked.R;
import com.eflexsoft.liked.model.MessageList;
import com.eflexsoft.liked.model.User;
import com.eflexsoft.liked.viewholder.DiscoverUserViewHolder;
import com.eflexsoft.liked.viewmodel.DiscoverViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class DiscoverAdapter extends RecyclerView.Adapter<DiscoverUserViewHolder> {

    List<User> userList = new ArrayList<>();
    Context context;

    private static int Is_Loading_More;

    int clickCount = 0;
    boolean isLiked = false;

    public boolean isLoading;

    DiscoverViewModel viewModel;

    public DiscoverAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public DiscoverUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.dicover_item_layout, parent, false);
        return new DiscoverUserViewHolder(view1);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscoverUserViewHolder holder, int position) {

        User user = userList.get(position);

        viewModel = ViewModelProviders.of((FragmentActivity) context).get(DiscoverViewModel.class);

        holder.name.setText(user.getName() + " ");
        holder.about.setText(user.getAbout());
        holder.age.setText(user.getAge());
        holder.address.setText(user.getAddress());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(R.drawable.no_p);
        requestOptions.placeholder(R.drawable.no_p);

        Glide.with(context).load(user.getProfilePictureUrl())
                .apply(requestOptions)
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;

                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                }).into(holder.proPic);

        if (user.isLiked()){
            holder.heart_count.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCount = clickCount + 1;
                if (clickCount == 2) {
                    clickCount = 0;
                    if (holder.heart_count.getVisibility() != View.VISIBLE) {
                        holder.heart.setVisibility(View.VISIBLE);
                        holder.heart.animate().scaleY(1.5f).scaleX(1.5f).setDuration(450);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                holder.heart.animate().scaleY(0f).scaleX(0f).setDuration(450);
                            }
                        }, 600);

//                    holder.heart.setVisibility(View.GONE);
                      user.setLiked(true);
                      notifyItemChanged(position);

                        Query query = FirebaseDatabase.getInstance().getReference("ChatId").child(FirebaseAuth.getInstance().getUid()).child(user.getId());

                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                MessageList messageList = snapshot.getValue(MessageList.class);

                                if (snapshot.exists()) {

                                    viewModel.sendHiMessage(FirebaseAuth.getInstance().getUid(), user.getId(), messageList.getChatId());
                                } else {
                                    viewModel.sendHiMessage(FirebaseAuth.getInstance().getUid(), user.getId(), user.getId() + FirebaseAuth.getInstance().getUid());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        Toast.makeText(context, "love message sent", Toast.LENGTH_SHORT).show();


                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {

//        return userList == null ? 0 : userList.size();
        return userList.size();
    }

    public void addUserFistLoad(List<User> users) {

        userList = users;


    }

//    public void addMoreUserLoad(String afterId, String myGender) {
//        int position = userList.size() - 1;
//
//        Set<User> userSet = new LinkedHashSet<>(userList);
//        userSet.addAll(users);
//        userList.clear();
//        userList.addAll(userSet);
//
//        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("id").startAt(afterId).limitToFirst(5);
//
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    User user = dataSnapshot.getValue(User.class);
//                    if (!user.getGender().equals(myGender)) {
//                        userList.add(user);
//                    }
//                    notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });

//        notifyDataSetChanged();
//    }

    public void addMoreUserLoad(List<User> users) {

        this.userList  = users;
        notifyDataSetChanged();

    }

}
