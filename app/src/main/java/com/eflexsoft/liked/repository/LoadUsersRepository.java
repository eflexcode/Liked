package com.eflexsoft.liked.repository;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.eflexsoft.liked.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class LoadUsersRepository {

    private static LoadUsersRepository loadUsersRepository;

    public MutableLiveData<List<User>> listMutableLiveData = new MutableLiveData<>();

    private LoadUsersRepository() {

    }

    public static LoadUsersRepository getInstance() {

        if (loadUsersRepository == null) {
            loadUsersRepository = new LoadUsersRepository();
        }

        return loadUsersRepository;

    }

    public void firstLoad(Context context, String usersToGetGender) {

        List<User> userList = new ArrayList<>();
        int firstLoadLimit = 20;

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        Query query = firebaseFirestore.collection("Users")
                .whereEqualTo("gender", usersToGetGender).orderBy("timeStamp", Query.Direction.DESCENDING).limit(firstLoadLimit);

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                    User user = documentSnapshot.toObject(User.class);
//                    String myId = FirebaseAuth.getInstance().getUid();
//                    DocumentReference myReference = firebaseFirestore.collection("Users")
//                            .document(myId).collection("Likes").document(user.getId());
//
//                    myReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                        @Override
//                        public void onSuccess(DocumentSnapshot documentSnapshot) {
//                            if (!documentSnapshot.exists()) {
                                userList.add(user);

//                            }
//                        }
//                    });

                }

                listMutableLiveData.setValue(userList);
            }
        });

    }

}
