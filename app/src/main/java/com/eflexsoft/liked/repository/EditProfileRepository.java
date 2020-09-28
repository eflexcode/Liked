package com.eflexsoft.liked.repository;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class EditProfileRepository {

    Context context;

    public MutableLiveData<Boolean> isSuccessMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> isFailureMutableLiveData = new MutableLiveData<>();


    public EditProfileRepository(Context context) {
        this.context = context;
    }

    public void updateProfile(final String names, final String Address, final String gender, final String age, final String about) {
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getUid());

        HashMap<String, Object> map = new HashMap<>();
        map.put("id", firebaseAuth.getCurrentUser().getUid());
        map.put("name", names);
        map.put("address", Address);
        map.put("gender", gender);
        map.put("age", age);
        map.put("about", about);

        databaseReference.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                isSuccessMutableLiveData.setValue(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                isFailureMutableLiveData.setValue(true);
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
