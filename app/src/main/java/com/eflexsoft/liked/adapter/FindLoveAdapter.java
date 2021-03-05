package com.eflexsoft.liked.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.eflexsoft.liked.R;
import com.eflexsoft.liked.UserDetailActivity;
import com.eflexsoft.liked.databinding.FindLoveItemBinding;
import com.eflexsoft.liked.model.User;
import com.eflexsoft.liked.viewholder.FindLoveUserViewHolder;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class FindLoveAdapter extends RecyclerView.Adapter<FindLoveUserViewHolder> {

    List<User> userList = new ArrayList<>();
    Context context;
    //    String gender;
    private int lastPosition = -1;

    int duration = 400;
    private boolean on_attach = true;

    double lon;
    double lat;

    boolean canLoad = true;

    public FindLoveAdapter(List<User> userList, Context context, double lon, double lat) {
        this.userList = userList;
        this.context = context;
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
    public void onBindViewHolder(@NonNull FindLoveUserViewHolder viewHolder, int position) {

        try {


            User model = userList.get(position);
            String myId = FirebaseAuth.getInstance().getUid();

            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

            DocumentReference myReference = firebaseFirestore.collection("Users")
                    .document(myId).collection("Likes").document(model.getId());

            myReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {

                        userList.remove(model);
                        notifyItemRemoved(position);

//                        viewHolder.itemView.setVisibility(View.GONE);

                    }
                }
            });

            viewHolder.binding.age.setText(model.getAge());
            viewHolder.binding.name.setText(model.getName());

//            setAnimation(viewHolder.itemView, position);

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
//        Toast.makeText(context, lon+"  "+lat, Toast.LENGTH_LONG).show();

            if (model.getLongitude() != 0 && model.getLatitude() != 0 && lat != 0 && lat != 0) {
                viewHolder.binding.location.setText(thelocation);
            } else {
                viewHolder.binding.location.setText("unknown mi");
            }

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

                    if (model.getLongitude() != 0 && model.getLatitude() != 0 && lat != 0 && lat != 0) {
                        intent.putExtra("location", thelocation);
                    } else {
                        intent.putExtra("location", "unknown mi");
                    }

                    intent.putExtra("dis1", model.getDisplayImage1());
                    intent.putExtra("dis2", model.getDisplayImage2());
                    intent.putExtra("dis3", model.getDisplayImage3());

                    Pair<View, String> pair = Pair.create(viewHolder.binding.userDisPic1, viewHolder.binding.userDisPic1.getTransitionName());

                    ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context, pair);

                    context.startActivity(intent, activityOptionsCompat.toBundle());

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
                            userList.remove(model);
                            notifyItemRemoved(position);
                        }
                    }, 5000);

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

        } catch (Exception e) {

        }
    }

    //    onCi
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        try {
            super.onAttachedToRecyclerView(recyclerView);
        } catch (Exception e) {

        }

        try {
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    on_attach = false;
                    super.onScrollStateChanged(recyclerView, newState);

                    if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE && canLoad) {

                        canLoad = false;

                        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                        User user = userList.get(userList.size() - 1);

                        Query query = firebaseFirestore.collection("Users")
                                .whereEqualTo("gender", user.getGender()).orderBy("timeStamp", Query.Direction.DESCENDING).startAfter(user.getTimeStamp()).limit(20);

                        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                    User user = documentSnapshot.toObject(User.class);
                                    if (user != null) {
                                        userList.add(user);
                                    }

                                }

                                notifyDataSetChanged();
                                canLoad = true;
                            }
                        });

                    }

                }
            });

        } catch (Exception e) {
            Toast.makeText(context, "Unable to load more match", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
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

        objectAnimator.setStartDelay(isNotFirstItem ? duration / 2 : (position * duration / 2));

        objectAnimator.setDuration(400);

        animationSet.play(objectAnimator);

        objectAnimator.start();

    }

}
