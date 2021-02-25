package com.eflexsoft.liked.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
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
import com.eflexsoft.liked.databinding.DicoverItemLayoutBinding;
import com.eflexsoft.liked.databinding.FindLoveItemBinding;
import com.eflexsoft.liked.model.MessageList;
import com.eflexsoft.liked.model.User;
import com.eflexsoft.liked.viewholder.DiscoverUserViewHolder;
import com.eflexsoft.liked.viewholder.FindLoveUserViewHolder;
import com.eflexsoft.liked.viewmodel.DiscoverViewModel;
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

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class DiscoverAdapter extends FirebaseRecyclerPagingAdapter<User, FindLoveUserViewHolder> {

    //    List<User> userList = new ArrayList<>();
    Context context;

    private static int Is_Loading_More;

    int clickCount = 0;
    boolean isLiked = false;

    public boolean isLoading;

    DiscoverViewModel viewModel;

    String myGender;
    private int lastPosition = -1;

    int duration = 500;
    private boolean on_attach = true;

    /**
     * Construct a new FirestorePagingAdapter from the given {@link DatabasePagingOptions}.
     *
     * @param options
     */
    public DiscoverAdapter(@NonNull DatabasePagingOptions options, Context context, String myGender) {
        super(options);
        this.context = context;
        this.myGender = myGender;
    }


    @NonNull
    @Override
    public FindLoveUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        FindLoveItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.find_love_item, parent, false);

        return new FindLoveUserViewHolder(binding);
    }

//    @Override
//    public void onBindViewHolder(@NonNull DiscoverUserViewHolder holder, int position) {
//
//        User user = userList.get(position);
//
//        viewModel = ViewModelProviders.of((FragmentActivity) context).get(DiscoverViewModel.class);
//
//        holder.name.setText(user.getName() + " ");
//        holder.about.setText(user.getAbout());
//        holder.age.setText(user.getAge());
//        holder.address.setText(user.getAddress());
//
//        RequestOptions requestOptions = new RequestOptions();
//        requestOptions.error(R.drawable.no_p);
//        requestOptions.placeholder(R.drawable.no_p);
//
//        Glide.with(context).load(user.getProfilePictureUrl())
//                .apply(requestOptions)
//                .addListener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        holder.progressBar.setVisibility(View.GONE);
//                        return false;
//
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        holder.progressBar.setVisibility(View.GONE);
//                        return false;
//                    }
//                }).into(holder.proPic);
//
//        if (user.isLiked()){
//            holder.heart_count.setVisibility(View.VISIBLE);
//        }
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clickCount = clickCount + 1;
//                if (clickCount == 2) {
//                    clickCount = 0;
//                    if (holder.heart_count.getVisibility() != View.VISIBLE) {
//                        holder.heart.setVisibility(View.VISIBLE);
//                        holder.heart.animate().scaleY(1.5f).scaleX(1.5f).setDuration(450);
//                        Handler handler = new Handler();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                holder.heart.animate().scaleY(0f).scaleX(0f).setDuration(450);
//                            }
//                        }, 600);
//
////                    holder.heart.setVisibility(View.GONE);
//                      user.setLiked(true);
//                      notifyItemChanged(position);
//
//                        Query query = FirebaseDatabase.getInstance().getReference("ChatId").child(FirebaseAuth.getInstance().getUid()).child(user.getId());
//
//                        query.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                                MessageList messageList = snapshot.getValue(MessageList.class);
//
//                                if (snapshot.exists()) {
//
//                                    viewModel.sendHiMessage(FirebaseAuth.getInstance().getUid(), user.getId(), messageList.getChatId());
//                                } else {
//                                    viewModel.sendHiMessage(FirebaseAuth.getInstance().getUid(), user.getId(), user.getId() + FirebaseAuth.getInstance().getUid());
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
//
//                        Toast.makeText(context, "love message sent", Toast.LENGTH_SHORT).show();
//
//
//                    }
//                }
//            }
//        });
//    }

    public void addUserFistLoad(List<User> users) {

//        userList = users;


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

//        this.userList  = users;
//        notifyDataSetChanged();

    }


    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                on_attach = false;
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    protected void onBindViewHolder(@NonNull FindLoveUserViewHolder viewHolder, int position, @NonNull User model) {

        viewModel = ViewModelProviders.of((FragmentActivity) context).get(DiscoverViewModel.class);

        viewHolder.binding.age.setText(model.getAge());
        viewHolder.binding.name.setText(model.getName());

        setAnimation(viewHolder.itemView, position);

////        holder.name.setText(user.getName() + " ");
////        holder.about.setText(user.getAbout());
////        holder.age.setText(user.getAge());
////        holder.address.setText(user.getAddress());
//
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(R.drawable.no_p);
        requestOptions.placeholder(R.drawable.no_p);

        RequestOptions requestOptionsDis = new RequestOptions();
        requestOptionsDis.error(R.drawable.no_p);
        requestOptionsDis.placeholder(R.color.grey);
//        viewHolder.binding.setUser(model);
//        if (model.getGender().equals(myGender)) {
//            viewHolder.itemView.setVisibility(View.GONE);
//
//            ViewGroup.LayoutParams layoutParams = viewHolder.binding.c.getLayoutParams();
//            layoutParams.height = 0;
//            layoutParams.width = 0;
//            viewHolder.binding.c.setLayoutParams(layoutParams);
//        }
//
////        Toast.makeText(context, myGender, Toast.LENGTH_SHORT).show();
//
        Glide.with(context).load(model.getProfilePictureUrl())
                .apply(requestOptions)
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;

                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).into(viewHolder.binding.proPicMe);

        Glide.with(context).load(model.getDisplayImage1())
                .apply(requestOptionsDis)
                .addListener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;

                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).into(viewHolder.binding.userDisPic1);


        if (model.getDisplayImage1() == null) {
            Glide.with(context).load(model.getProfilePictureUrl())
                    .apply(requestOptionsDis)
                    .addListener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;

                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).into(viewHolder.binding.userDisPic1);
        }

        try {
            if (model.getIsOnline().equals("yes")) {
                viewHolder.binding.online.setImageResource(R.color.on_line);
            } else {
                viewHolder.binding.online.setImageResource(R.color.off_line);
            }
        } catch (Exception e) {

        }


//        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clickCount = clickCount + 1;
//                if (clickCount == 2) {
//                    clickCount = 0;
//                    if (viewHolder.binding.heartImageCount.getVisibility() != View.VISIBLE) {
//                        viewHolder.binding.heartImage.setVisibility(View.VISIBLE);
//                        viewHolder.binding.heartImage.animate().scaleY(1.5f).scaleX(1.5f).setDuration(450);
//                        Handler handler = new Handler();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                viewHolder.binding.heartImage.animate().scaleY(0f).scaleX(0f).setDuration(450);
//                            }
//                        }, 600);
//
////                    holder.heart.setVisibility(View.GONE);
//                        model.setLiked(true);
//                        notifyItemChanged(position);
//
//                        Query query = FirebaseDatabase.getInstance().getReference("ChatId").child(FirebaseAuth.getInstance().getUid()).child(model.getId());
//
//                        query.addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                                MessageList messageList = snapshot.getValue(MessageList.class);
//
//                                if (snapshot.exists()) {
//
//                                    viewModel.sendHiMessage(FirebaseAuth.getInstance().getUid(), model.getId(), messageList.getChatId());
//                                } else {
//                                    viewModel.sendHiMessage(FirebaseAuth.getInstance().getUid(), model.getId(), model.getId() + FirebaseAuth.getInstance().getUid());
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//
//                            }
//                        });
//
//                        Toast.makeText(context, "Love message sent", Toast.LENGTH_SHORT).show();
//
//                    }
//                }
//            }
//        });
    }

    @Override
    protected void onLoadingStateChanged(@NonNull LoadingState state) {
        viewModel = ViewModelProviders.of((FragmentActivity) context).get(DiscoverViewModel.class);
        if (state == LoadingState.LOADING_INITIAL) {
            viewModel.isPageLoadedMutableLiveData.setValue(true);
        }

    }


    private void setAnimation(View viewToAnimate, int position) {
        if (!on_attach) {
            position = -1;
        }

        boolean isNotFirstItem = position == -1;

        position++;

        AnimatorSet animationSet = new AnimatorSet();

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(viewToAnimate, "alpha", 0f, 0.5f, 1f);

        ObjectAnimator.ofFloat(viewToAnimate, "alpha", 0f).start();

        objectAnimator.setStartDelay(isNotFirstItem ? duration / 2 : (position * duration / 3));

        objectAnimator.setDuration(500);

        animationSet.play(objectAnimator);

        objectAnimator.start();

    }
}
