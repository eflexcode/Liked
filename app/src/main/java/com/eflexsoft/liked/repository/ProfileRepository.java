package com.eflexsoft.liked.repository;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class ProfileRepository {

    Context context;
    public MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> booleanMutableLiveData = new MutableLiveData<>();


    public ProfileRepository(Context context) {
        this.context = context;
    }

    public void getUserProfileDetails() {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        DocumentReference documentReference = firebaseFirestore.collection("Users").document(FirebaseAuth.getInstance().getUid());
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                User user = value.toObject(User.class);
                userMutableLiveData.setValue(user);
            }
        });

    }

    public String getMineType(Uri uri) {
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void uploadImageByte(byte[] bytes) {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference("profileImages");


        StorageReference camera = storageReference.child("cameraUpload" + System.currentTimeMillis());
        UploadTask uploadTask = camera.putBytes(bytes);

        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return camera.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {

                    String downloadUri = task.getResult().toString();
                    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("profilePictureUrl", downloadUri);

                    databaseReference.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                booleanMutableLiveData.setValue(false);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            booleanMutableLiveData.setValue(false);
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                booleanMutableLiveData.setValue(false);
            }

        });

    }

    public void uploadImageUri(Uri uri) {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        StorageReference storageReference = firebaseStorage.getReference("profileImages");

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

                    String downloadUri = task.getResult().toString();
                    DocumentReference documentReference = FirebaseFirestore.getInstance()
                            .collection("Users")
                            .document(firebaseAuth.getUid());

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("profilePictureUrl", downloadUri);

                    documentReference.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            booleanMutableLiveData.setValue(false);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            booleanMutableLiveData.setValue(false);
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                booleanMutableLiveData.setValue(false);
            }

        });

    }

    public void setIsOnline(String status) {
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
//                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//
        Map<String, Object> map = new HashMap<>();
        map.put("isOnline", status);
//
//        databaseReference.updateChildren(map);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        DocumentReference documentReference = FirebaseFirestore.getInstance()
                .collection("Users")
                .document(firebaseAuth.getUid());

        documentReference.update(map);

    }


}
