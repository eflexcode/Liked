package com.eflexsoft.liked;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.ByteArrayOutputStream;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

//    CircleImageView proPicChat;
//    CircleImageView onLine;
//    TextView name;

    String imageUrl;

    Intent intent;
    String id;

    MessageViewModel viewModel;
    Dialog dialog;

//    MaterialEditText messageText;
//    ImageView send;
//
//    SwipeRefreshLayout swipeRefreshLayout;
//    RecyclerView recyclerView;

    private boolean isCamera;

    private String chatId;

    ChatAdapter chatAdapter;

    ActivityMessageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_message);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(R.drawable.no_p);
        requestOptions.placeholder(R.drawable.no_p);

//        proPicChat = findViewById(R.id.pro_pic_message);
//        onLine = findViewById(R.id.id_online);
//        name = findViewById(R.id.name_chat);
//        swipeRefreshLayout = findViewById(R.id.swipe);
//        recyclerView = findViewById(R.id.chat_recycler);

        binding.toolb.setNavigationIcon(R.drawable.back);
        binding.toolb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);

        chatAdapter = new ChatAdapter(MessageActivity.this);

        binding.chatRecycler.setLayoutManager(linearLayoutManager);
        binding.chatRecycler.setAdapter(chatAdapter);

        viewModel = ViewModelProviders.of(this).get(MessageViewModel.class);

        intent = getIntent();
//        messageText = findViewById(R.id.message_text);
//        send = findViewById(R.id.send_message);

        id = intent.getStringExtra("id");
        chatId = intent.getStringExtra("chatId");

        binding.messageText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()) {
                    binding.sendMessage.setImageResource(R.drawable.send_no);
                } else {
                    binding.sendMessage.setImageResource(R.drawable.send);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        try {
//            viewModel.getChat(chatId, id);
//        } catch (NullPointerException e) {
//            Toast.makeText(this, "no chat history found", Toast.LENGTH_SHORT).show();
//        }

        Button camera;
        Button gallery;

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.chose_upload_pic);
        camera = dialog.findViewById(R.id.camera);
        gallery = dialog.findViewById(R.id.gallery);

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 9);
                dialog.dismiss();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 10);
                dialog.dismiss();
            }
        });

        binding.chatRecycler.setItemAnimator(new DefaultItemAnimator());
        binding.swipe.setColorSchemeResources(R.color.colorPrimary);
//        viewModel.doSendId(id);
//        viewModel.observeUserDetails().observe(this, new Observer<User>() {
//            @Override
//            public void onChanged(User user) {
//
//                binding.nameChat.setText(user.getName());
//
//                Glide.with(MessageActivity.this).load(user.getProfilePictureUrl())
//                        .apply(requestOptions).into(binding.proPicMessage);
//
//                imageUrl = user.getProfilePictureUrl();
//
//                try {
//                    if (user.getIsOnline().equals("yes")) {
//                        binding.idOnline.setImageResource(R.color.on_line);
//                    } else {
//                        binding.idOnline.setImageResource(R.color.off_line);
//                    }
//                } catch (NullPointerException e) {
//
//                }
//
//            }
//        });

//        binding.swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                try {
//                    viewModel.getChatMore(chatId, id);
//                    binding.swipe.setRefreshing(false);
//                } catch (NullPointerException e) {
//                    Toast.makeText(MessageActivity.this, "no chat history found", Toast.LENGTH_SHORT).show();
//
//                }
//            }
//        });
//
//        viewModel.observeGetChats().observe(this, new Observer<List<Chat>>() {
//            @Override
//            public void onChanged(List<Chat> chats) {
//
//                chatAdapter.setChats(chats);
//                binding.chatRecycler.scrollToPosition(chats.size() - 1);
//            }
//        });
//
//        viewModel.updateIsSeen(chatId);

    }

    public void finish(View view) {
        finish();
    }

    public void showPickImage(View view) {
        dialog.show();
    }

    public void sendMessage(View view) {

        String message = binding.messageText.getText().toString();

//        if (!message.trim().isEmpty()) {
//            viewModel.sendMessage(id, FirebaseAuth.getInstance().getUid(), message, chatId);
//            binding.messageText.setText("");
//        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 9 && resultCode == RESULT_OK && data.getData() != null) {
            isCamera = false;
//            viewModel.sendImageGallery(id, FirebaseAuth.getInstance().getUid(), data.getData(), chatId);
            Toast.makeText(this, "sending", Toast.LENGTH_SHORT).show();
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
}