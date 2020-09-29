package com.eflexsoft.liked.repository;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.eflexsoft.liked.model.MessageList;
import com.eflexsoft.liked.model.User;
import com.eflexsoft.liked.viewmodel.ProfileViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DiscoverRepository {

    Context context;
    public MutableLiveData<List<User>> firstLoadMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<List<User>> nextLoadMutableLiveData = new MutableLiveData<>();

    String myGender;

    public DiscoverRepository(Context context) {
        this.context = context;
    }

    public void doFirstLoad() {

        List<MessageList> messageLists = new ArrayList<>();

        Query chatQuery = FirebaseDatabase.getInstance().getReference("ChatId").child(FirebaseAuth.getInstance().getUid());

        chatQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageLists.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    MessageList messageList = dataSnapshot.getValue(MessageList.class);
                    messageLists.add(messageList);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Query query = FirebaseDatabase.getInstance().getReference("Users").limitToFirst(20);

        List<User> userList = new ArrayList<>();
        Set<User> userSet = new HashSet<>();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        User fuser = snapshot.getValue(User.class);
                        myGender = fuser.getGender();

                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                userList.clear();
                                userSet.clear();
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                    try {
                                        User user = dataSnapshot.getValue(User.class);
                                        if (!user.getGender().equals(fuser.getGender())) {

                                            userSet.add(user);

                                        }


                                    } catch (Exception e) {

                                    }
                                }

                                userList.addAll(userSet);

                                firstLoadMutableLiveData.setValue(userList);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }
        }, 10000);


    }

    public void doLoadNext(String afterId) {

        Query chatQuery = FirebaseDatabase.getInstance().getReference("ChatId").child(FirebaseAuth.getInstance().getUid());

        List<MessageList> messageLists = new ArrayList<>();

        chatQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageLists.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    MessageList messageList = dataSnapshot.getValue(MessageList.class);
                    messageLists.add(messageList);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("id").startAt(afterId).limitToFirst(20);

        List<User> userList = new ArrayList<>();
        Set<User> userSet = new HashSet<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User fuser = snapshot.getValue(User.class);
                myGender = fuser.getGender();

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            try {
                                User user = dataSnapshot.getValue(User.class);
                                if (!user.getGender().equals(fuser.getGender())) {
                                    if (messageLists.size() > 0) {
                                        for (MessageList messageList : messageLists) {
                                            if (messageList.getId().equals(user.getId())) {
                                                userSet.add(user);

                                            }
                                        }
                                    } else {
                                        Toast.makeText(context, "still loading...", Toast.LENGTH_SHORT).show();
                                    }
                                }


                            } catch (Exception e) {

                            }
                        }
                        userList.addAll(userSet);

                        nextLoadMutableLiveData.setValue(userList);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendHiMessage(String senderId, String receiverId, String chatId) {

        Query query = FirebaseDatabase.getInstance().getReference("ChatId").child(FirebaseAuth.getInstance().getUid());

        DatabaseReference chatReference = FirebaseDatabase.getInstance()
                .getReference("ChatList");

        DatabaseReference chatIdMeReference = FirebaseDatabase.getInstance()
                .getReference("ChatId").child(senderId).child(receiverId);

        Map<String, Object> mapChatMe = new HashMap<>();
        mapChatMe.put("id", receiverId);
        mapChatMe.put("chatId", chatId);
        mapChatMe.put("lastMessage", "If you were a movie, I'd watch you over and over again.");

        chatIdMeReference.setValue(mapChatMe);

        DatabaseReference chatIdYouReference = FirebaseDatabase.getInstance()
                .getReference("ChatId").child(receiverId).child(senderId);

        Map<String, Object> mapChatYou = new HashMap<>();
        mapChatYou.put("id", senderId);
        mapChatYou.put("chatId", chatId);
        mapChatYou.put("lastMessage", "If you were a movie, I'd watch you over and over again.");

        chatIdYouReference.setValue(mapChatYou);

        Calendar calendar = Calendar.getInstance();
        String date = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        Map<String, Object> map = new HashMap<>();
        map.put("receiverId", receiverId);
        map.put("senderId", senderId);
        map.put("message", "If you were a movie, I'd watch you over and over again.");
        map.put("date", date);
        map.put("isSeen", false);
        map.put("imageUrl", "null");

        chatReference.child(chatId).push().setValue(map);

    }

}