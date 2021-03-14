package com.eflexsoft.liked.repository;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.eflexsoft.liked.MainActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class EditProfileRepository {

    Context context;

    public MutableLiveData<Boolean> isSuccessMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> isFailureMutableLiveData = new MutableLiveData<>();

    public EditProfileRepository(Context context) {
        this.context = context;
    }

    public String getMineType(Uri uri) {
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    public void updateProfile(final String names, double lon, double lat, final String gender, final String age, final String about,
                              Uri proPic, Uri dis1, Uri dis2, Uri dis3) {
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getUid());
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

        DocumentReference documentReference = FirebaseFirestore.getInstance()
                .collection("Users")
                .document(firebaseAuth.getUid());


        if (proPic != null) {

            StorageReference storageReference = firebaseStorage.getReference("profileImages");
            StorageReference gallery = storageReference.child("galleryUpload" + System.currentTimeMillis() + getMineType(proPic));
            UploadTask uploadTask = gallery.putFile(proPic);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return gallery.getDownloadUrl();
                }
            }).addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("profilePictureUrl", uri.toString());

                    documentReference.update(map);
                }
            });

        }

        if (dis1 != null) {

            StorageReference storageReference = firebaseStorage.getReference("displayImages/gallery");
            StorageReference gallery = storageReference.child(System.currentTimeMillis() + getMineType(dis1));

            UploadTask uploadTask = gallery.putFile(dis1);
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

                        HashMap<String, Object> map = new HashMap<>();
                        map.put("displayImage1", downloadUri);

                        documentReference.update(map);
                    }
                }
            });
        }
        if (dis2 != null) {

            StorageReference storageReference = firebaseStorage.getReference("displayImages/gallery");
            StorageReference gallery = storageReference.child(System.currentTimeMillis() + getMineType(dis2));

            UploadTask uploadTask = gallery.putFile(dis2);
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

                        HashMap<String, Object> map = new HashMap<>();
                        map.put("displayImage2", downloadUri);

                        documentReference.update(map);
                    }
                }
            });

        }

        if (dis3 != null) {

            StorageReference storageReference = firebaseStorage.getReference("displayImages/gallery");
            StorageReference gallery = storageReference.child(System.currentTimeMillis() + getMineType(dis3));

            UploadTask uploadTask = gallery.putFile(dis3);
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

                        HashMap<String, Object> map = new HashMap<>();
                        map.put("displayImage3", downloadUri);

                        documentReference.update(map);
                    }
                }
            });

        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("name", names);
        map.put("gender", gender);
        map.put("age", age);
        map.put("about", about);

        if (lon != 0 && lat != 0) {
            map.put("latitude", lat);
            map.put("longitude", lon);
        }

        documentReference.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                isSuccessMutableLiveData.setValue(true);
            }
        });

//        databaseReference.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                isSuccessMutableLiveData.setValue(true);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                isFailureMutableLiveData.setValue(true);
//                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });

    }

}
