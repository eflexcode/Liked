package com.eflexsoft.liked.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eflexsoft.liked.R;
import com.eflexsoft.liked.adapter.ChatAdapter;
import com.eflexsoft.liked.adapter.DiscoverAdapter;
import com.eflexsoft.liked.adapter.FindLoveAdapter;
import com.eflexsoft.liked.databinding.FragmentDiscoverBinding;
import com.eflexsoft.liked.model.MessageList;
import com.eflexsoft.liked.model.User;
import com.eflexsoft.liked.viewholder.DiscoverUserViewHolder;
import com.eflexsoft.liked.viewmodel.DiscoverViewModel;
import com.eflexsoft.liked.viewmodel.LoadItemsViewModel;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import www.sanju.zoomrecyclerlayout.ZoomRecyclerLayout;

public class DiscoverFragment extends Fragment {

    DiscoverViewModel viewModel;
    DiscoverAdapter adapter;
    List<User> userArrayList = new ArrayList<>();
    String myGender;
    FragmentDiscoverBinding binding;
    FindLoveAdapter findLoveAdapter;
    LoadItemsViewModel loadItemsViewModel;

    double lon;
    double lat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_discover, container, false);


        HashMap<String, Object> map = new HashMap<>();

        map.put("name", "names");
        map.put("gender", "Male");
        map.put("age", "23");
        map.put("timeStamp", System.currentTimeMillis());
        map.put("isOnline", "yes");
        map.put("email", "email");
        map.put("about", "Walkers! Thanks for listening. If you want to hear my newest single and the last video in the World");
        map.put("profilePictureUrl", "https://firebasestorage.googleapis.com/v0/b/liked-a0f31.appspot.com/o/profileImages%2FgalleryUpload1614252689678jpg?alt=media&token=92fb2c72-b900-49b8-b21c-e402ea8342ac");
        map.put("displayImage1", "https://firebasestorage.googleapis.com/v0/b/liked-a0f31.appspot.com/o/displayImages%2Fgallery%2F1614452031620jpg?alt=media&token=81955ccf-7d82-4742-8bee-32ec9e3225a4");
        map.put("displayImage2", "https://firebasestorage.googleapis.com/v0/b/liked-a0f31.appspot.com/o/displayImages%2Fgallery%2F1614254059107jpg?alt=media&token=ce771ffc-1ef7-458e-9e67-b17ce82ce2bc");
        map.put("displayImage3", "https://firebasestorage.googleapis.com/v0/b/liked-a0f31.appspot.com/o/displayImages%2Fgallery%2F1614254059019jpg?alt=media&token=adce9e5b-f10e-4b59-bc05-98494ea862fd");

        int d = 8;

        binding.toolb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                for (int i = 0; i <= 2; i++) {
//
//                    String  id = "qwer2214jhvhsadhgfd6q3"+d+6;
//
//                    map.put("id", id);
//
//                    DocumentReference documentReference = FirebaseFirestore.getInstance()
//                            .collection("Users")
//                            .document();
//                    documentReference.set(map);
//                }

                final int[] count = {0};

                CollectionReference collection = FirebaseFirestore.getInstance().collection("Users");

                collection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            count[0]++;
                        }
                        Log.d(TAG, "onSuccess: pppppppppppppppppppppppppppppppppppppppppppppp" + count[0]);
                    }
                });

            }
        });

        try {
            initRecycleView();
        } catch (Exception e) {

        }


//        Query chatQuery = FirebaseDatabase.getInstance().getReference("ChatId").child(FirebaseAuth.getInstance().getUid());
//
//        List<MessageList> messageLists = new ArrayList<>();
//
//        chatQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                messageLists.clear();
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//
//                    MessageList messageList = dataSnapshot.getValue(MessageList.class);
//                    messageLists.add(messageList);
//
//                }
//
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        binding.reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.proBar.setVisibility(View.VISIBLE);
                try {
                    initRecycleView();
                } catch (Exception e) {

                }

            }
        });

//        ZoomRecyclerLayout zoomRecyclerLayout = new ZoomRecyclerLayout(getContext(), ZoomRecyclerLayout.VERTICAL, false);


        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(binding.discoverRecycler);

//        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("id").startAt(afterId).limitToFirst(5);

        viewModel = ViewModelProviders.of(getActivity()).get(DiscoverViewModel.class);
        loadItemsViewModel = new ViewModelProvider(getActivity()).get(LoadItemsViewModel.class);
//        viewModel.observerLoadFirst().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
//            @Override
//            public void onChanged(List<User> users) {
//
//                List<User> userList = new ArrayList<>(users);
//
//                for (User user : users) {
//                    for (MessageList messageList : messageLists) {
//                        if (user.getId().equals(messageList.getId())) {
//                            userList.remove(user);
//
//                        }
//                    }
//                }
//                adapter.addUserFistLoad(userList);
//                bar.setVisibility(View.GONE);
//                userArrayList.addAll(users);
//                recyclerView.setVisibility(View.VISIBLE);
//                initialPageSize = userList.size();
//
//            }
//        });

//        viewModel.observerLoadNext().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
//            @Override
//            public void onChanged(List<User> users) {
//
//                List<User> userList = new ArrayList<>(users);
//
//                for (User user : users) {
//                    for (MessageList messageList : messageLists) {
//                        if (user.getId().equals(messageList.getId())) {
//                            userList.remove(user);
//
//                        }
//                    }
//                }

//
//                adapter.addMoreUserLoad(userList);
//                userArrayList.addAll(userList);
////                Toast.makeText(getContext(), " scroll down or ignore", Toast.LENGTH_SHORT).show();
//            }
//        });

        loadItemsViewModel.getFirstUsersList().observe(getActivity(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                binding.proBar.setVisibility(View.GONE);
                try {
                    findLoveAdapter = new FindLoveAdapter(users, getContext(), lon, lat);
                    binding.discoverRecycler.setAdapter(findLoveAdapter);
                    findLoveAdapter.notifyDataSetChanged();
                } catch (Exception e) {

                }

            }
        });

        viewModel.getIsPageLoadedMutableLiveData().observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    binding.proBar.setVisibility(View.GONE);
                } else {
                    binding.proBar.setVisibility(View.VISIBLE);
                }
            }
        });

        return binding.getRoot();
    }

    private void initRecycleView() {
        try {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
            binding.discoverRecycler.setHasFixedSize(true);
            binding.discoverRecycler.setLayoutManager(gridLayoutManager);

//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
//                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users")
                    .document(FirebaseAuth.getInstance().getUid());

            final boolean[] isNotLoaded = {true};

//        Toast.makeText(getContext(), FirebaseAuth.getInstance().getUid(), Toast.LENGTH_LONG).show();

            Log.d(TAG, "initRecycleView: " + FirebaseAuth.getInstance().getUid());

            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    User user = documentSnapshot.toObject(User.class);

                    String mGender = null;

                    String otherGender = null;

                    try {

                        mGender = user.getGender();

//                String vGender = documentSnapshot.get("gender").toString();

                        if (mGender.equals("Male")) {

                            otherGender = "Female";

                        } else {
                            otherGender = "Male";
                        }
//                        Toast.makeText(getContext(), otherGender, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Unable to get user gender", Toast.LENGTH_SHORT).show();
                    }

//                Query query = FirebaseFirestore.getInstance().collection("Users").whereEqualTo("gender", otherGender).orderBy("timeStamp", Query.Direction.DESCENDING);//.orderByValue();//.equalTo("Female");
//
//                PagedList.Config config = new PagedList.Config.Builder()
//                        .setEnablePlaceholders(false)
//                        .setMaxSize(20)
//                        .setPageSize(10)
//                        .setInitialLoadSizeHint(10)
//                        .setPrefetchDistance(5)
//                        .build();
//
//                FirestorePagingOptions<User> pagingOptions = new FirestorePagingOptions.Builder<User>()
//                        .setLifecycleOwner(getActivity())
//                        .setQuery(query, config, User.class)
//                        .build();

//                double lon;
//                double lat;
//
//                if (user.getLatitude() != 0 && user.getLongitude() != 0) {
//                    lat = user.getLatitude();
//                    lon = user.getLongitude();
//                }
//                adapter = new DiscoverAdapter(pagingOptions, getContext(), mGender, user.getLongitude(), user.getLatitude());
                    lon = user.getLongitude();
                    lat = user.getLatitude();

                    loadItemsViewModel.firstLoad(getContext(), otherGender);

                }
            });
        } catch (Exception e) {

        }

    }

    public void removeFromAdapter(int position) {

        ChatAdapter chatAdapter = new ChatAdapter(getContext());
//        chatAdapter.re

//        adapter.re

    }

    private static final String TAG = "DiscoverFragment";
}