package com.eflexsoft.liked.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
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
import com.eflexsoft.liked.UserDetailActivity;
import com.eflexsoft.liked.databinding.DicoverItemLayoutBinding;
import com.eflexsoft.liked.databinding.FindLoveItemBinding;
import com.eflexsoft.liked.model.MessageList;
import com.eflexsoft.liked.model.User;
import com.eflexsoft.liked.viewholder.DiscoverUserViewHolder;
import com.eflexsoft.liked.viewholder.FindLoveUserViewHolder;
import com.eflexsoft.liked.viewmodel.DiscoverViewModel;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static com.firebase.ui.firestore.paging.LoadingState.LOADING_INITIAL;

public class DiscoverAdapter extends FirestorePagingAdapter<User, FindLoveUserViewHolder> {

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

    double lon;
    double lat;
    private static final String TAG = "DiscoverAdapter";

    /**
     * Construct a new FirestorePagingAdapter from the given {@link FirestorePagingOptions}.
     *
     * @param options
     */
    public DiscoverAdapter(@NonNull FirestorePagingOptions<User> options, Context context, String myGender, double lon, double lat) {
        super(options);
        this.context = context;
        this.myGender = myGender;
        this.lon = lon;
        this.lat = lat;
    }

    @NonNull
    @Override
    public FindLoveUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        FindLoveItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.find_love_item, parent, false);

        return new FindLoveUserViewHolder(binding);
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
//        if (model.getGender().equals(myGender)){
//            viewHolder.itemView.setVisibility(View.GONE);
//        }
//        Toast.makeText(context, myGender, Toast.LENGTH_SHORT).show();
        viewModel = ViewModelProviders.of((FragmentActivity) context).get(DiscoverViewModel.class);

        String myId = FirebaseAuth.getInstance().getUid();

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        DocumentReference myReference = firebaseFirestore.collection("Users")
                .document(myId).collection("Likes").document(model.getId());

        myReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.exists()) {


                } else {
//                    viewHolder.itemView.setVisibility(View.VISIBLE);
//                    notifyItemChanged(position);
                }
            }
        });

        viewHolder.binding.age.setText(model.getAge());
        viewHolder.binding.name.setText(model.getName());

        setAnimation(viewHolder.itemView, position);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(R.drawable.no_p);
        requestOptions.placeholder(R.drawable.no_p);

        RequestOptions requestOptionsDis = new RequestOptions();
        requestOptionsDis.error(R.drawable.no_p);
        requestOptionsDis.placeholder(R.color.grey);

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

        Location myLocation = new Location("LocationA");
        myLocation.setLatitude(lat);
        myLocation.setLongitude(lon);

        Location otherUserLocation = new Location("LocationB");
        otherUserLocation.setLongitude(model.getLongitude());
        otherUserLocation.setLatitude(model.getLatitude());

        double distance = myLocation.distanceTo(otherUserLocation);

        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        decimalFormat.setRoundingMode(RoundingMode.CEILING);

        String thelocation = decimalFormat.format(distance * 0.000621371192) + " mi";

        viewHolder.binding.location.setText(thelocation);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, UserDetailActivity.class);
                intent.putExtra("id", model.getId());
                intent.putExtra("name", model.getName());
                intent.putExtra("age", model.getAge());
                intent.putExtra("about", model.getAbout());
                intent.putExtra("profileImageUrl", model.getProfilePictureUrl());
                intent.putExtra("isOnline", model.getIsOnline());
                intent.putExtra("location", thelocation);
                intent.putExtra("dis1", model.getDisplayImage1());
                intent.putExtra("dis2", model.getDisplayImage2());
                intent.putExtra("dis3", model.getDisplayImage3());

                Pair<View, String> pair = Pair.create(viewHolder.binding.userDisPic1, viewHolder.binding.userDisPic1.getTransitionName());

                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, pair);

                context.startActivity(intent, activityOptionsCompat.toBundle());
//                ((FragmentActivity) context).overridePendingTransition(android.R.anim.bounce_interpolator,android.R.anim.bounce_interpolator);


            }
        });

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

        viewHolder.binding.starButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                String myId = FirebaseAuth.getInstance().getUid();
                String otherUserId = model.getId();

                String messageId = String.valueOf(System.currentTimeMillis());

                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                DocumentReference myReference = firebaseFirestore.collection("Users")
                        .document(myId).collection("Likes").document(otherUserId);

                Map<String, String> myMap = new HashMap<>();
                myMap.put("userId", otherUserId);
                myMap.put("messageId", messageId);

                myReference.set(myMap);

                DocumentReference otherReference = firebaseFirestore.collection("Users")
                        .document(otherUserId).collection("Likes").document(myId);

                Map<String, String> otherMap = new HashMap<>();
                otherMap.put("userId", myId);
                otherMap.put("messageId", messageId);

                otherReference.set(otherMap);

                CollectionReference message = firebaseFirestore.collection("Messages").document(messageId).collection(messageId);

                long i = System.currentTimeMillis();

                String chatId = String.valueOf(i);

                Map<String, Object> messageMap = new HashMap<>();
                messageMap.put("firstId", myId);
                messageMap.put("secondId", otherUserId);
                messageMap.put("message", love.get(new Random().nextInt(14)));
                messageMap.put("imageUrl", "no image");
                messageMap.put("videoUrl", "no video");
                messageMap.put("chatId", i);

                message.document(chatId).set(messageMap);

                Handler handler = new Handler();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        viewHolder.itemView.setVisibility(View.GONE);
//                        notifyItemRemoved(position);
                    }
                }, 10000);

            }

            @Override
            public void unLiked(LikeButton likeButton) {
                String myId = FirebaseAuth.getInstance().getUid();
                String otherUserId = model.getId();

                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();


                DocumentReference myReference = firebaseFirestore.collection("Users")
                        .document(myId).collection("Likes").document(otherUserId);

                DocumentReference otherReference = firebaseFirestore.collection("Users")
                        .document(otherUserId).collection("Likes").document(myId);

                myReference.delete();
                otherReference.delete();

            }
        });

    }

    @Override
    protected void onLoadingStateChanged(@NonNull LoadingState state) {
        super.onLoadingStateChanged(state);
        viewModel = ViewModelProviders.of((FragmentActivity) context).get(DiscoverViewModel.class);
        if (state == LOADING_INITIAL) {
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
