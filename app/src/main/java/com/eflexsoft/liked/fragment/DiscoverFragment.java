package com.eflexsoft.liked.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.eflexsoft.liked.R;
import com.eflexsoft.liked.adapter.DiscoverAdapter;
import com.eflexsoft.liked.databinding.FragmentDiscoverBinding;
import com.eflexsoft.liked.model.MessageList;
import com.eflexsoft.liked.model.User;
import com.eflexsoft.liked.viewholder.DiscoverUserViewHolder;
import com.eflexsoft.liked.viewmodel.DiscoverViewModel;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.firebase.ui.database.paging.DefaultSnapshotDiffCallback;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;
import com.firebase.ui.database.paging.LoadingState;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import www.sanju.zoomrecyclerlayout.ZoomRecyclerLayout;

public class DiscoverFragment extends Fragment {

//    RecyclerView recyclerView;
    DiscoverViewModel viewModel;
    DiscoverAdapter adapter;
//    ProgressBar bar;
    int initialPageSize;
    int pageCount;
    int visibleItemCount;
    List<User> userArrayList = new ArrayList<>();
    String myGender;
    FragmentDiscoverBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_discover,container,false);

        initRecycleView();

        Query chatQuery = FirebaseDatabase.getInstance().getReference("ChatId").child(FirebaseAuth.getInstance().getUid());

        List<MessageList> messageLists = new ArrayList<>();

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

        binding.reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.proBar.setVisibility(View.VISIBLE);
                initRecycleView();
            }
        });

//        ZoomRecyclerLayout zoomRecyclerLayout = new ZoomRecyclerLayout(getContext(), ZoomRecyclerLayout.VERTICAL, false);



        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(binding.discoverRecycler);


//        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("id").startAt(afterId).limitToFirst(5);




//        recyclerView.setVisibility(View.GONE);
        viewModel = ViewModelProviders.of(getActivity()).get(DiscoverViewModel.class);
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

        viewModel.observerLoadNext().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {

                List<User> userList = new ArrayList<>(users);

                for (User user : users) {
                    for (MessageList messageList : messageLists) {
                        if (user.getId().equals(messageList.getId())) {
                            userList.remove(user);

                        }
                    }
                }


                adapter.addMoreUserLoad(userList);
                userArrayList.addAll(userList);
//                Toast.makeText(getContext(), " scroll down or ignore", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getIsPageLoadedMutableLiveData().observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    binding.proBar.setVisibility(View.GONE);
                }else {
                    binding.proBar.setVisibility(View.VISIBLE);
                }
            }
        });

//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//
//                if (!recyclerView.canScrollVertically(1)) {
//                    User user = userArrayList.get(userArrayList.size() - 1);
////                    adapter.addMoreUserLoad(user.getId(),myGender);
//                    viewModel.doLoadNext(user.getId());
//                    Toast.makeText(getContext(), "loading next page", Toast.LENGTH_SHORT).show();
////                    viewModel.isPageAtLastMutableLiveData.setValue(true);
//
//                } else {
//                    viewModel.isPageAtLastMutableLiveData.setValue(false);
//                }
//
//            }
//
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//
//                visibleItemCount = zoomRecyclerLayout.getInitialPrefetchItemCount();
//
//            }
//        });

//        Query query = FirebaseDatabase.getInstance().getReference("Users");
////

//        RequestOptions requestOptions = new RequestOptions();
//        requestOptions.error(R.drawable.no_p);
//        requestOptions.placeholder(R.drawable.no_p);
//
//        FirebaseRecyclerPagingAdapter<User, DiscoverUserViewHolder> recyclerPagingAdapter = new FirebaseRecyclerPagingAdapter<User, DiscoverUserViewHolder>(pagingOptions) {
//            @Override
//            protected void onBindViewHolder(@NonNull DiscoverUserViewHolder viewHolder, int position, @NonNull User model) {
//                viewHolder.name.setText(model.getName() + ",");
//                viewHolder.about.setText(model.getAbout());
//                viewHolder.age.setText(model.getAge());
//                viewHolder.address.setText(model.getAddress());
//
//                Glide.with(getActivity()).load(model.getProfilePictureUrl())
//                        .apply(requestOptions)
//                        .addListener(new RequestListener<Drawable>() {
//                            @Override
//                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                                viewHolder.progressBar.setVisibility(View.GONE);
//                                return false;
//
//                            }
//
//                            @Override
//                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                                viewHolder.progressBar.setVisibility(View.GONE);
//                                return false;
//                            }
//                        }).into(viewHolder.proPic);
//
//            }
//
//            @Override
//            protected void onLoadingStateChanged(@NonNull LoadingState state) {
//
//            }
//
//            @NonNull
//            @Override
//            public DiscoverUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.dicover_item_layout, parent, false);
//
//                return new DiscoverUserViewHolder(view1);
//            }
//        };
        return binding.getRoot();
    }

    private void initRecycleView(){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        binding.discoverRecycler.setHasFixedSize(true);
        binding.discoverRecycler.setLayoutManager(gridLayoutManager);


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                User fuser = snapshot.getValue(User.class);
                myGender = fuser.getGender();

                PagedList.Config config = new PagedList.Config.Builder()
                        .setEnablePlaceholders(false)
                        .setInitialLoadSizeHint(10)
                        .setPageSize(10)
                        .setPrefetchDistance(5)
                        .build();

                Query query = FirebaseDatabase.getInstance().getReference("Users");//.equalTo("Female","gender");//.equalTo(myGender, "gender").orderByChild("id");

                DatabasePagingOptions<User> pagingOptions = new DatabasePagingOptions.Builder<User>()
                        .setQuery(query, config, User.class)
                        .setLifecycleOwner(getActivity())
                        .build();

                adapter = new DiscoverAdapter(pagingOptions, getContext(), myGender);
                binding.discoverRecycler.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}