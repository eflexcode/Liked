package com.eflexsoft.liked.repository;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.eflexsoft.liked.model.Chat;
import com.eflexsoft.liked.model.Like;
import com.eflexsoft.liked.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class MessageRepository {

    Context context;
    //    public MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<List<Like>> likeMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<List<Chat>> chatMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<List<Chat>> chatMoreMutableLiveData = new MutableLiveData<>();
    List<Chat> chatList = new ArrayList<>();
//    List<Chat> newChatList = new ArrayList<>();

    List<Like> likes = new ArrayList<>();

    int loadSize = 50;

    public MessageRepository(Context context) {
        this.context = context;
    }

    //    public void getSearchedUserProfile(String id) {
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
//                .child(id);
//
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                try {
//                    User user = snapshot.getValue(User.class);
//                    userMutableLiveData.setValue(user);
//                } catch (Exception e) {
//
//                }
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }
//
//    private int loadSize;
//
    public String getMineType(Uri uri) {
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    //
//    public void sendImageCamera(String receiver, String senderId, byte[] bytes, String chatId) {
//        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
//        StorageReference storageReference = firebaseStorage.getReference("messageImages");
//
//
//        StorageReference camera = storageReference.child("cameraUpload" + System.currentTimeMillis());
//        UploadTask uploadTask = camera.putBytes(bytes);
//
//        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//            @Override
//            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                if (!task.isSuccessful()) {
//                    throw task.getException();
//                }
//
//                return camera.getDownloadUrl();
//            }
//        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//            @Override
//            public void onComplete(@NonNull Task<Uri> task) {
//                if (task.isSuccessful()) {
////                    String chatId = receiver + senderId;
//
//                    String downloadUri = task.getResult().toString();
//                    DatabaseReference chatReference = FirebaseDatabase.getInstance()
//                            .getReference("ChatList").child(chatId);
//
//                    DatabaseReference chatIdMeReference = FirebaseDatabase.getInstance()
//                            .getReference("ChatId").child(senderId).child(receiver);
//
//                    Map<String, Object> mapChatMe = new HashMap<>();
//                    mapChatMe.put("id", receiver);
//                    mapChatMe.put("chatId", chatId);
//                    mapChatMe.put("lastMessage", "sent image");
//
//                    chatIdMeReference.setValue(mapChatMe);
//
//                    DatabaseReference chatIdYouReference = FirebaseDatabase.getInstance()
//                            .getReference("ChatId").child(receiver).child(senderId);
//
//                    Map<String, Object> mapChatYou = new HashMap<>();
//                    mapChatYou.put("id", senderId);
//                    mapChatYou.put("chatId", chatId);
//                    mapChatYou.put("lastMessage", "sent image");
//
//                    chatIdYouReference.setValue(mapChatYou);
//
//                    Calendar calendar = Calendar.getInstance();
//                    String date = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
//
//                    Map<String, Object> map = new HashMap<>();
//                    map.put("receiverId", receiver);
//                    map.put("senderId", senderId);
//                    map.put("message", " sent image");
//                    map.put("date", date);
//                    map.put("isSeen", false);
//                    map.put("imageUrl", downloadUri);
//
//                    chatReference.push().setValue(map);
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//
//        });
//
//    }
//
    public void sendImageGallery(Uri uri, String messageId, String firstId, String secondId) {

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference("messageImages");

        StorageReference gallery = storageReference.child("galleryUpload" + System.currentTimeMillis() + getMineType(uri));
        UploadTask uploadTask = gallery.putFile(uri);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return gallery.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {

                    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                    CollectionReference messageCollection = firebaseFirestore.collection("Messages").document(messageId).collection(messageId);

                    long i = System.currentTimeMillis();

                    String chatId = String.valueOf(i);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ENGLISH);

                    String time = dateFormat.format(Calendar.getInstance().getTime());

                    Map<String, Object> messageMap = new HashMap<>();
                    messageMap.put("firstId", firstId);
                    messageMap.put("secondId", secondId);
                    messageMap.put("firstIdSeen", false);
                    messageMap.put("secondIdSeen", false);
                    messageMap.put("message", "An image was sent");
                    messageMap.put("imageUrl", task.getResult().toString());
                    messageMap.put("videoUrl", "no video");
                    messageMap.put("date", time);
                    messageMap.put("chatId", i);

                    messageCollection.document(chatId).set(messageMap);

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    //
    public void sendMessage(String firstId, String secondId, String message, String messageId) {

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        CollectionReference messageCollection = firebaseFirestore.collection("Messages").document(messageId).collection(messageId);

        long i = System.currentTimeMillis();

        String chatId = String.valueOf(i);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.ENGLISH);

        String time = dateFormat.format(Calendar.getInstance().getTime());

        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("firstId", firstId);
        messageMap.put("secondId", secondId);
        messageMap.put("firstIdSeen", false);
        messageMap.put("secondIdSeen", false);
        messageMap.put("message", message);
        messageMap.put("imageUrl", "no image");
        messageMap.put("videoUrl", "no video");
        messageMap.put("date", time);
        messageMap.put("chatId", i);

        messageCollection.document(chatId).set(messageMap);

    }

    public void getMessage(String messageId) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        Query message = firebaseFirestore.collection("Messages").document(messageId).collection(messageId).orderBy("chatId", Query.Direction.ASCENDING).limitToLast(50);

        message.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                chatList.clear();
                for (DocumentSnapshot documentSnapshot : value.getDocuments()) {

                    Chat chat = documentSnapshot.toObject(Chat.class);
                    chatList.add(chat);
                }
                chatMutableLiveData.setValue(chatList);
            }
        });

    }

    public void getMessageMore(String messageId) {


        loadSize = loadSize + 50;

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        Query message = firebaseFirestore.collection("Messages").document(messageId).collection(messageId).orderBy("chatId", Query.Direction.ASCENDING).limitToLast(loadSize);

        message.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                chatList.clear();
                for (DocumentSnapshot documentSnapshot : value.getDocuments()) {

                    Chat chat = documentSnapshot.toObject(Chat.class);
                    chatList.add(chat);
                }
                chatMoreMutableLiveData.setValue(chatList);
            }
        });

    }
//
//    public void updateIsSeen(String chatId) {
//        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//
//        Query query = firebaseDatabase.getReference("ChatList").child(chatId);
//
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    Chat chat = dataSnapshot.getValue(Chat.class);
//                    if (chat.getReceiverId().equals(firebaseAuth.getUid())) {
//
//                        Map<String, Object> map = new HashMap<>();
//                        map.put("isSeen", true);
//
//                        dataSnapshot.getRef().updateChildren(map);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//    }
//
//    public void getChatIdNewStyle(String chatId) {
//
//        loadSize = 100;
//
//        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//
//        Query query = firebaseDatabase.getReference("ChatList").child(chatId).limitToFirst(newLoadSize);
//
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                newChatList.clear();
//
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//
//                    Chat chat = dataSnapshot.getValue(Chat.class);
//                    newChatList.add(chat);
//
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//    }

    public void getFirstLikes() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String myId = firebaseAuth.getUid();

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        Query myReference = firebaseFirestore.collection("Users")
                .document(myId).collection("Likes").orderBy("messageId", Query.Direction.DESCENDING).limit(15);
        myReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                likes.clear();
                for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                    Like like = documentSnapshot.toObject(Like.class);
                    if (likes.contains(like)) {
                        likes.remove(like);
                    } else {
                        likes.add(like);
                    }
                }

                likeMutableLiveData.setValue(likes);
            }
        });

//        myReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//
//            }
//        });

    }

}
