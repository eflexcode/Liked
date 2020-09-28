package com.eflexsoft.liked.repository;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.arch.core.internal.SafeIterableMap;
import androidx.lifecycle.MutableLiveData;

import com.eflexsoft.liked.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CreateAccountRepository {

    Context context;
    public MutableLiveData<Boolean> booleanMutableLiveData = new MutableLiveData<>();

    public CreateAccountRepository(Context context) {
        this.context = context;
    }

    public void createAccountEmailPassword(final String names, final String Address, final String gender, final String age, final String about, final String email, String password) {

        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("id", firebaseAuth.getCurrentUser().getUid());
                    map.put("name", names);
                    map.put("address", Address);
                    map.put("gender", gender);
                    map.put("age", age);
                    map.put("about", about);
                    map.put("isOnline","no");
                    map.put("email", email);
                    map.put("profilePictureUrl", "default");

                    databaseReference.child(firebaseAuth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(context, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
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

    public void createAccountCredential(AuthCredential authCredential, final String names, final String Address, final String gender, final String age, final String about) {

        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("id", firebaseAuth.getCurrentUser().getUid());
                    map.put("name", names);
                    map.put("address", Address);
                    map.put("gender", gender);
                    map.put("age", age);
                    map.put("about", about);
                    map.put("email", firebaseAuth.getCurrentUser().getEmail());
                    map.put("phoneNumber", firebaseAuth.getCurrentUser().getPhoneNumber());
                    map.put("profilePictureUrl", "default");

                    databaseReference.child(firebaseAuth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(context, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                context.startActivity(intent);
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
        });

    }
}