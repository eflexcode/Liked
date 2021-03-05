package com.eflexsoft.liked;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager2.widget.ViewPager2;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.eflexsoft.liked.adapter.UserItemImageSlideAdapter;
import com.eflexsoft.liked.databinding.ActivityUserDetailBinding;
import com.eflexsoft.liked.model.SlidePostImageItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class UserDetailActivity extends AppCompatActivity {

    ActivityUserDetailBinding binding;

    String name;
    String age;
    String about;
    String location;
    String profileImageUrl;
    String dis1;
    String dis2;
    String dis3;
    String isOnline;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_user_detail);


        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_detail);
//
//        AnimatorSet animatorSet = new AnimatorSet();
//
//        binding.proPicMe.setTranslationY(binding.proPicMe.getY() + 200);
//
////        ObjectAnimator.ofFloat(binding.proPicMe, "translationY", -30)
////                .start();
//
//        ObjectAnimator objectAnimator = ObjectAnimator
//                .ofFloat(binding.proPicMe, "translationY",
//                        binding.proPicMe.getY() + 200,
//                        binding.proPicMe.getY() + 250,
//                        binding.proPicMe.getY() + 230,
//                        binding.proPicMe.getY() + 220,
//                        binding.proPicMe.getY() + 210, 0);
//
//        objectAnimator.setStartDelay(300);
//        objectAnimator.setDuration(3000);
//        animatorSet.play(objectAnimator);
//        objectAnimator.start();

//        ObjectAnimator
//                .ofFloat(binding.proPicMe, "translationY", 0, 100, -100, 0)
//                .setDuration(500)
//                .start();

        Intent intent = getIntent();
//        ObjectAnimator
//                .ofFloat(binding.nextArrow, "translationX", 0, 15, -15, 15, -15, 5, -5, 3, -3, 0)
//                .setDuration(3000)
//                .start();
        id = intent.getStringExtra("id");
        name = intent.getStringExtra("name");
        age = intent.getStringExtra("age");
        about = intent.getStringExtra("about");
        location = intent.getStringExtra("location");
        isOnline = intent.getStringExtra("isOnline");
        profileImageUrl = intent.getStringExtra("profileImageUrl");
        dis1 = intent.getStringExtra("dis1");
        dis2 = intent.getStringExtra("dis2");
        dis3 = intent.getStringExtra("dis3");

        binding.name.setText(name.trim()+",");
        binding.about.setText(about);
        binding.location.setText(location);
        binding.age.setText(age);

        if (isOnline.equals("yes")) {
            binding.online.setImageResource(R.color.on_line);
        } else {
            binding.online.setImageResource(R.color.off_line);
        }

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(R.drawable.no_p);
        requestOptions.placeholder(R.drawable.no_p);

        Glide.with(this).load(profileImageUrl)
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
                }).into(binding.proPicMe);

        binding.proPicMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(UserDetailActivity.this, ImageFullViewActivity.class);
                intent1.putExtra("url", profileImageUrl);

                Pair<View, String> pair = Pair.create(binding.proPicMe, binding.proPicMe.getTransitionName());

                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(UserDetailActivity.this, pair);

                startActivity(intent1, activityOptionsCompat.toBundle());

            }
        });

        List<SlidePostImageItem> url = new ArrayList<>();
        url.add(new SlidePostImageItem(dis1));
        url.add(new SlidePostImageItem(dis2));
        url.add(new SlidePostImageItem(dis3));

        UserItemImageSlideAdapter adapter = new UserItemImageSlideAdapter(url, this);

        binding.postImages.setAdapter(adapter);
//        binding.postImages.
        binding.allImage.setText("3");

        binding.postImages.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                binding.currentImage.setText(String.valueOf(position + 1));

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


        binding.starButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                String myId = FirebaseAuth.getInstance().getUid();
                String otherUserId = id;

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

            }

            @Override
            public void unLiked(LikeButton likeButton) {
                String myId = FirebaseAuth.getInstance().getUid();
                String otherUserId = id;

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

    public void goBack(View view) {
        onBackPressed();
    }
}