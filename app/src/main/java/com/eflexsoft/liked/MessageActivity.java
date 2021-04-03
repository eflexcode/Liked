package com.eflexsoft.liked;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.eflexsoft.liked.adapter.ChatAdapter;
import com.eflexsoft.liked.databinding.ActivityMainBinding;
import com.eflexsoft.liked.databinding.ActivityMessageBinding;
import com.eflexsoft.liked.model.Chat;
import com.eflexsoft.liked.model.User;
import com.eflexsoft.liked.viewholder.ChatViewHolder;
import com.eflexsoft.liked.viewmodel.MessageViewModel;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;
import com.firebase.ui.database.paging.LoadingState;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.vanniktech.emoji.EmojiManager;
import com.vanniktech.emoji.EmojiPopup;
import com.vanniktech.emoji.google.GoogleEmojiProvider;

import java.io.ByteArrayOutputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {


    String imageUrl;

    Intent intent;
    String otherId;

    MessageViewModel viewModel;
    Dialog dialog;

    private boolean isCamera;

    private String messageId;

    ChatAdapter chatAdapter;

    ActivityMessageBinding binding;

    EmojiPopup emojiPopup;

    boolean isEmoji = false;

    ImageView uploadImageView;
    Dialog dialogUpload;
    Uri Uri;

    double lon;
    double lat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EmojiManager.install(new GoogleEmojiProvider());

        binding = DataBindingUtil.setContentView(this, R.layout.activity_message);

        emojiPopup = EmojiPopup.Builder.fromRootView(binding.roorView).build(binding.messageText);

//        binding.cover.setBlur(7);

//        RequestOptions requestOptions = new RequestOptions();
//        requestOptions.error(R.drawable.no_p);
//        requestOptions.placeholder(R.drawable.no_p);
//
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        chatAdapter = new ChatAdapter(MessageActivity.this);

        binding.chatRecycler.setItemAnimator(new DefaultItemAnimator());
        binding.chatRecycler.setLayoutManager(linearLayoutManager);
        binding.chatRecycler.setAdapter(chatAdapter);

        viewModel = ViewModelProviders.of(this).get(MessageViewModel.class);

        binding.swipe.setColorSchemeResources(R.color.colorAccent);
        binding.swipe.setRefreshing(true);

        intent = getIntent();

        otherId = intent.getStringExtra("otherId");
        messageId = intent.getStringExtra("messageId");

        viewModel.getMessage(messageId);

        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.what_302);

        binding.messageText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    binding.sendMessage.setImageResource(R.drawable.ic_direct);
                    binding.galleryMessage.setVisibility(View.VISIBLE);
                } else {
                    binding.sendMessage.setImageResource(R.drawable.ic_direct2);
                    binding.galleryMessage.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.messageText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        binding.messageText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        binding.messageText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });

        binding.toolb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users")
                .document(FirebaseAuth.getInstance().getUid());

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);

                lon = user.getLongitude();
                lat = user.getLatitude();


            }
        });

        DocumentReference documentReference2 = FirebaseFirestore.getInstance().collection("Users")
                .document(otherId);

        documentReference2.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                try {
                    User user = value.toObject(User.class);

                    binding.nameChat.setText(user.getName());

                    if (user.getIsOnline().equals("yes")) {
                        binding.idOnline.setImageResource(R.color.on_line);
                    } else {
                        binding.idOnline.setImageResource(R.color.off_line);
                    }

                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.error(R.drawable.no_p);
                    requestOptions.placeholder(R.drawable.no_p);

                    Glide.with(MessageActivity.this).load(user.getProfilePictureUrl())
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
                            }).into(binding.proPicMessage);

                    binding.proPicMessage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MessageActivity.this, UserDetailActivity.class);
                            intent.putExtra("id", user.getId());
                            intent.putExtra("name", user.getName());
                            intent.putExtra("age", user.getAge());
                            intent.putExtra("about", user.getAbout());
                            intent.putExtra("profileImageUrl", user.getProfilePictureUrl());
                            intent.putExtra("isOnline", user.getIsOnline());

                            Location myLocation = new Location("LocationA");
                            myLocation.setLatitude(lat);
                            myLocation.setLongitude(lon);

                            Location otherUserLocation = new Location("LocationB");
                            otherUserLocation.setLongitude(user.getLongitude());
                            otherUserLocation.setLatitude(user.getLatitude());

                            double distance = myLocation.distanceTo(otherUserLocation);

                            DecimalFormat decimalFormat = new DecimalFormat("#.##");
                            decimalFormat.setRoundingMode(RoundingMode.CEILING);

                            String thelocation = decimalFormat.format(distance * 0.000621371192) + " mi";

                            if (user.getLongitude() != 0 && user.getLatitude() != 0 && lat != 0 && lat != 0) {
                                intent.putExtra("location", thelocation);
                            } else {
                                intent.putExtra("location", "unknown mi");
                            }

                            intent.putExtra("dis1", user.getDisplayImage1());
                            intent.putExtra("dis2", user.getDisplayImage2());
                            intent.putExtra("dis3", user.getDisplayImage3());

                            Pair<View, String> pair = Pair.create(binding.proPicMessage, binding.proPicMessage.getTransitionName());

                            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(MessageActivity.this, pair);

                            startActivity(intent);

                        }
                    });

                } catch (Exception exception) {
                    exception.printStackTrace();
                }

            }
        });

//
////        try {
////            viewModel.getChat(chatId, id);
////        } catch (NullPointerException e) {
////            Toast.makeText(this, "no chat history found", Toast.LENGTH_SHORT).show();
////        }
//
//        Button camera;
//        Button gallery;
//
//        dialog = new Dialog(this);
//        dialog.setContentView(R.layout.chose_upload_pic);
//        camera = dialog.findViewById(R.id.camera);
//        gallery = dialog.findViewById(R.id.gallery);
//
//        gallery.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {

//                dialog.dismiss();
//            }
//        });
//
//        camera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent, 10);
//                dialog.dismiss();
//            }
//        });
//

//        binding.swipe.setColorSchemeResources(R.color.colorPrimary);
////        viewModel.doSendId(id);
////        viewModel.observeUserDetails().observe(this, new Observer<User>() {
////            @Override
////            public void onChanged(User user) {
////
////                binding.nameChat.setText(user.getName());
////
////                Glide.with(MessageActivity.this).load(user.getProfilePictureUrl())
////                        .apply(requestOptions).into(binding.proPicMessage);
////
////                imageUrl = user.getProfilePictureUrl();
////
////                try {
////                    if (user.getIsOnline().equals("yes")) {
////                        binding.idOnline.setImageResource(R.color.on_line);
////                    } else {
////                        binding.idOnline.setImageResource(R.color.off_line);
////                    }
////                } catch (NullPointerException e) {
////
////                }
////
////            }
////        });
//
        binding.swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    viewModel.getMessageMore(messageId);
                    binding.swipe.setRefreshing(false);
                } catch (NullPointerException e) {
                    Toast.makeText(MessageActivity.this, "no chat history found", Toast.LENGTH_SHORT).show();

                }
            }
        });

        viewModel.observeGetMessage().observe(this, new Observer<List<Chat>>() {
            @Override
            public void onChanged(List<Chat> chats) {

                mediaPlayer.start();
                chatAdapter.setChats(chats);
                binding.swipe.setRefreshing(false);
                binding.chatRecycler.scrollToPosition(chats.size() - 1);

            }
        });

        final int[] scrollPosition = {50};
        final boolean[] isFirstScroll = {false};

        viewModel.observeGetMoreMessage().observe(this, new Observer<List<Chat>>() {
            @Override
            public void onChanged(List<Chat> chats) {

                if (isFirstScroll[0]) {
                    scrollPosition[0] += 50;
                }

                mediaPlayer.start();
                chatAdapter.setChatsMore(chats);
                binding.swipe.setRefreshing(false);
                binding.chatRecycler.scrollToPosition(chats.size() - scrollPosition[0]);
                isFirstScroll[0] = true;
            }
        });

//     viewModel.updateIsSeen(chatId);

        dialogUpload = new Dialog(this);
        dialogUpload.setCancelable(false);
        dialogUpload.setContentView(R.layout.change_pro_pic_layout);

        uploadImageView = dialogUpload.findViewById(R.id.pro_pic_me);
        TextView upload = dialogUpload.findViewById(R.id.upload);
        TextView cancel = dialogUpload.findViewById(R.id.cancel);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                viewModel.sendImageGallery(Uri, messageId, FirebaseAuth.getInstance().getUid(), otherId);
                Toast.makeText(MessageActivity.this, "Sending image", Toast.LENGTH_SHORT).show();
                dialogUpload.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogUpload.dismiss();
            }
        });
        Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Chat> chats = new ArrayList<>();

                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                CollectionReference message = firebaseFirestore.collection("Messages").document(messageId).collection(messageId);
                message.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MessageActivity.this, "Updating chat", Toast.LENGTH_SHORT).show();
                            }
                        });

                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {

                            Chat chat = documentSnapshot.toObject(Chat.class);
                            chats.add(chat);

                        }

                        WriteBatch writeBatch = firebaseFirestore.batch();

                        for (Chat chat : chats) {

                            if (chat.getFirstId().equals(FirebaseAuth.getInstance().getUid())) {

                                DocumentReference message = firebaseFirestore.collection("Messages").document(messageId).collection(messageId).document(String.valueOf(chat.getChatId()));

                                Map<String, Object> map = new HashMap<>();
                                map.put("firstIdSeen", true);

                                message.update(map);

                            } else if (chat.getSecondId().equals(FirebaseAuth.getInstance().getUid())) {

                                DocumentReference message = firebaseFirestore.collection("Messages").document(messageId).collection(messageId).document(String.valueOf(chat.getChatId()));
                                Map<String, Object> map = new HashMap<>();
                                map.put("secondIdSeen", true);

                                message.update(map);

                            }

                        }

                    }
                });
            }
        });

        thread.start();

    }

    public void finish(View view) {
        finish();
    }

    public void showPickImage(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    12);
        } else {


            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, 9);
        }
    }

    public void sendMessage(View view) {

        String message = binding.messageText.getText().toString();

        if (!message.trim().isEmpty()) {

            String myId = FirebaseAuth.getInstance().getUid();
            viewModel.sendMessage(myId, otherId, message, messageId);
            binding.messageText.setText("");

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 9 && resultCode == RESULT_OK && data.getData() != null) {
            isCamera = false;
            Uri = data.getData();
            uploadImageView.setImageURI(Uri);
            dialogUpload.show();
//            viewModel.sendImageGallery(id, FirebaseAuth.getInstance().getUid(), data.getData(), chatId);
//            Toast.makeText(this, "sending", Toast.LENGTH_SHORT).show();
        }

        if (requestCode == 10 && resultCode == RESULT_OK) {

            Bundle bundle = data.getExtras();

            Bitmap bitmap = (Bitmap) bundle.get("data");

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

            byte[] bytes = byteArrayOutputStream.toByteArray();

//            viewModel.sendImageCamera(id, FirebaseAuth.getInstance().getUid(), bytes, chatId);
            Toast.makeText(this, "sending", Toast.LENGTH_SHORT).show();
            isCamera = true;

        }
    }

    public void openEmojiKeyboard(View view) {

        if (isEmoji) {
            emojiPopup.dismiss();
            binding.emojiMessage.setImageResource(R.drawable.ic_smile);
            isEmoji = false;
        } else {
            emojiPopup.show();
            binding.emojiMessage.setImageResource(R.drawable.ic_keyboard);
            isEmoji = true;
        }

    }
}