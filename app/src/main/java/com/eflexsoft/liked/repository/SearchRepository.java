package com.eflexsoft.liked.repository;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.eflexsoft.liked.model.MessageList;
import com.eflexsoft.liked.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SearchRepository {

    Context context;
    public MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();

    public SearchRepository(Context context) {
        this.context = context;
    }


    public void getSearchedUserProfile(String id){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(id);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                User user = snapshot.getValue(User.class);
                userMutableLiveData.setValue(user);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getUserInChatList(){
        Query query = FirebaseDatabase.getInstance().getReference("ChatId").child(FirebaseAuth.getInstance().getUid());



        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    MessageList messageList = dataSnapshot.getValue(MessageList.class);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void sendHiMessage(String senderId,String receiverId,String chatId){

        Query query = FirebaseDatabase.getInstance().getReference("ChatId").child(FirebaseAuth.getInstance().getUid());

//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
//
//                    MessageList messageList = dataSnapshot.getValue(MessageList.class);
//
//                    if (messageList.getId().equals(receiverId)){
//                        Toast.makeText(context, "User already in your chat list from liked", Toast.LENGTH_SHORT).show();
//                    }else {
                        DatabaseReference chatReference = FirebaseDatabase.getInstance()
                                .getReference("ChatList");

                        DatabaseReference chatIdMeReference = FirebaseDatabase.getInstance()
                                .getReference("ChatId").child(senderId).child(receiverId);

                        Map<String, Object> mapChatMe = new HashMap<>();
                        mapChatMe.put("id", receiverId);
                        mapChatMe.put("chatId", chatId);
                        mapChatMe.put("lastMessage", "hi");

                        chatIdMeReference.setValue(mapChatMe);

                        DatabaseReference chatIdYouReference = FirebaseDatabase.getInstance()
                                .getReference("ChatId").child(receiverId).child(senderId);

                        Map<String, Object> mapChatYou = new HashMap<>();
                        mapChatYou.put("id", senderId);
                        mapChatYou.put("chatId", chatId);
                        mapChatYou.put("lastMessage", "hi");

                        chatIdYouReference.setValue(mapChatYou);

                        Calendar calendar = Calendar.getInstance();
                        String date = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

                        Map<String, Object> map = new HashMap<>();
                        map.put("receiverId", receiverId);
                        map.put("senderId", senderId);
                        map.put("message", "hi");
                        map.put("date", date);
                        map.put("isSeen", false);
                        map.put("imageUrl", "null");

                        chatReference.child(chatId).push().setValue(map);

//                    }
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });


    }

}
