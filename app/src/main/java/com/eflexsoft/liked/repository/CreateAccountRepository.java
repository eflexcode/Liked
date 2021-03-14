package com.eflexsoft.liked.repository;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.arch.core.internal.SafeIterableMap;
import androidx.lifecycle.MutableLiveData;

import com.eflexsoft.liked.MainActivity;
import com.eflexsoft.liked.signup.CreateProfileActivity;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateAccountRepository {

    Context context;
    public MutableLiveData<Boolean> booleanMutableLiveData = new MutableLiveData<>();

    public CreateAccountRepository(Context context) {
        this.context = context;
    }

    public String getMineType(Uri uri) {
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void createAccountEmailPassword(final String names, final String gender, final String age, final String email, String password) {

        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("id", firebaseAuth.getCurrentUser().getUid());
                    map.put("name", names);
                    map.put("gender", gender);
                    map.put("age", age);
                    map.put("timeStamp", System.currentTimeMillis());
                    map.put("isOnline", "yes");
                    map.put("email", email);
                    map.put("profilePictureUrl", "default");

                    DocumentReference documentReference = FirebaseFirestore.getInstance()
                            .collection("Users")
                            .document(firebaseAuth.getUid());

                    documentReference.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent intent = new Intent(context, CreateProfileActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);
                            booleanMutableLiveData.setValue(false);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            booleanMutableLiveData.setValue(false);
                        }
                    });

//                    databaseReference.child(firebaseAuth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful()) {
//                                Intent intent = new Intent(context, CreateProfileActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                context.startActivity(intent);
//                                booleanMutableLiveData.setValue(false);
//                            }
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//                            booleanMutableLiveData.setValue(false);
//                        }
//                    });
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

    public void createAccountCredential(AuthCredential authCredential, final String names, final String gender, final String age) {

        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("id", firebaseAuth.getCurrentUser().getUid());
                    map.put("name", names);
                    map.put("gender", gender);
                    map.put("age", age);
                    map.put("timeStamp", System.currentTimeMillis());
                    map.put("email", firebaseAuth.getCurrentUser().getEmail());
                    map.put("phoneNumber", firebaseAuth.getCurrentUser().getPhoneNumber());
                    map.put("profilePictureUrl", "default");

                    DocumentReference documentReference = FirebaseFirestore.getInstance()
                            .collection("Users")
                            .document(firebaseAuth.getUid());

                    documentReference.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent intent = new Intent(context, CreateProfileActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);
                            booleanMutableLiveData.setValue(false);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            booleanMutableLiveData.setValue(false);
                        }
                    });


//                    databaseReference.child(firebaseAuth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful()) {
//                                Intent intent = new Intent(context, CreateProfileActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                context.startActivity(intent);
//                                booleanMutableLiveData.setValue(false);
//                            }
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//                            booleanMutableLiveData.setValue(false);
//                        }
//                    });
                }
            }
        });

    }

    public void publishProfile(Uri profileUrl, String about, List<Uri> displayImages, double log, double lat) {

        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference("profileImages");

        Map<String, Object> map = new HashMap<>();
        map.put("about", about);
        map.put("latitude", lat);
        map.put("longitude", log);


        DocumentReference documentReference = FirebaseFirestore.getInstance()
                .collection("Users")
                .document(firebaseAuth.getUid());

        documentReference.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                StorageReference gallery = storageReference.child("galleryUpload" + System.currentTimeMillis() + getMineType(profileUrl));
                UploadTask uploadTask = gallery.putFile(profileUrl);
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
//                            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
//                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                            HashMap<String, Object> map = new HashMap<>();
                            map.put("profilePictureUrl", downloadUri);

                            documentReference.update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        final int[] upCount = {0};

                                        for (Uri uri : displayImages) {
                                            StorageReference storageReference = firebaseStorage.getReference("displayImages/gallery");
                                            StorageReference gallery = storageReference.child(System.currentTimeMillis() + getMineType(uri));

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
//                                                        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
//                                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                                        upCount[0] += 1;

                                                        HashMap<String, Object> map = new HashMap<>();
                                                        map.put("displayImage" + upCount[0], downloadUri);

                                                        documentReference.update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    if (upCount[0] == 3) {
                                                                        Intent intent = new Intent(context, MainActivity.class);
                                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                        context.startActivity(intent);
                                                                        booleanMutableLiveData.setValue(false);
                                                                    }
                                                                }
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {

                                                                booleanMutableLiveData.setValue(false);
                                                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    booleanMutableLiveData.setValue(false);
                                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
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
//
//        databaseReference.child(firebaseAuth.getCurrentUser().
//
//                    getUid()).
//
//                    updateChildren(map).
//
//                    addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete (@NonNull Task < Void > task) {
//                            if (task.isSuccessful()) {
//
//                                StorageReference gallery = storageReference.child("galleryUpload" + System.currentTimeMillis() + getMineType(profileUrl));
//                                UploadTask uploadTask = gallery.putFile(profileUrl);
//                                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                                    @Override
//                                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                                        if (!task.isSuccessful()) {
//                                            throw task.getException();
//                                        }
//
//                                        return gallery.getDownloadUrl();
//                                    }
//                                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Uri> task) {
//                                        if (task.isSuccessful()) {
//
//                                            String downloadUri = task.getResult().toString();
//                                            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
//                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//
//                                            HashMap<String, Object> map = new HashMap<>();
//                                            map.put("profilePictureUrl", downloadUri);
//
//                                            databaseReference.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                @Override
//                                                public void onComplete(@NonNull Task<Void> task) {
//                                                    if (task.isSuccessful()) {
//
//                                                        final int[] upCount = {0};
//
//                                                        for (Uri uri : displayImages) {
//                                                            StorageReference storageReference = firebaseStorage.getReference("displayImages/gallery");
//                                                            StorageReference gallery = storageReference.child(System.currentTimeMillis() + getMineType(profileUrl));
//
//                                                            UploadTask uploadTask = gallery.putFile(uri);
//                                                            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//                                                                @Override
//                                                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                                                                    if (!task.isSuccessful()) {
//                                                                        throw task.getException();
//                                                                    }
//
//                                                                    return gallery.getDownloadUrl();
//                                                                }
//                                                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//                                                                @Override
//                                                                public void onComplete(@NonNull Task<Uri> task) {
//                                                                    if (task.isSuccessful()) {
//
//                                                                        String downloadUri = task.getResult().toString();
//                                                                        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
//                                                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//
//                                                                        upCount[0] += 1;
//
//                                                                        HashMap<String, Object> map = new HashMap<>();
//                                                                        map.put("displayImage" + upCount[0], downloadUri);
//
//                                                                        databaseReference.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                                            @Override
//                                                                            public void onComplete(@NonNull Task<Void> task) {
//                                                                                if (task.isSuccessful()) {
//                                                                                    if (upCount[0] == 3) {
//                                                                                        Intent intent = new Intent(context, MainActivity.class);
//                                                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                                                                        context.startActivity(intent);
//                                                                                        booleanMutableLiveData.setValue(false);
//                                                                                    }
//                                                                                }
//                                                                            }
//                                                                        }).addOnFailureListener(new OnFailureListener() {
//                                                                            @Override
//                                                                            public void onFailure(@NonNull Exception e) {
//
//                                                                                booleanMutableLiveData.setValue(false);
//                                                                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//                                                                            }
//                                                                        });
//                                                                    }
//                                                                }
//                                                            }).addOnFailureListener(new OnFailureListener() {
//                                                                @Override
//                                                                public void onFailure(@NonNull Exception e) {
//                                                                    booleanMutableLiveData.setValue(false);
//                                                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//                                                                }
//                                                            });
//                                                        }
//                                                    }
//                                                }
//                                            }).addOnFailureListener(new OnFailureListener() {
//                                                @Override
//                                                public void onFailure(@NonNull Exception e) {
//                                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//                                                    booleanMutableLiveData.setValue(false);
//                                                }
//                                            });
//                                        }
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//                                        booleanMutableLiveData.setValue(false);
//                                    }
//
//                                });
//
//                            }
//                        }
//                    }).
//
//                    addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure (@NonNull Exception e){
//                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//                            booleanMutableLiveData.setValue(false);
//                        }
//                    });
        });
    }
}