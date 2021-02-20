package com.eflexsoft.liked.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.eflexsoft.liked.R;
import com.eflexsoft.liked.model.MessageList;
import com.eflexsoft.liked.model.User;
import com.eflexsoft.liked.viewmodel.SearchViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchFragment extends Fragment {

    EditText searchEdit;
    Handler handler;
    ProgressBar pageLoading;
    RelativeLayout detailsRelative;
    CircleImageView circleImageView;
    TextView name_and_age;
    TextView about;
    TextView address;
    TextView gender;
    ProgressBar imageLoading;
    ImageView searchError;
    TextView searchErrorText;
    SearchViewModel viewModel;
    Button button;
    String theId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        viewModel = ViewModelProviders.of(this).get(SearchViewModel.class);


        button = view.findViewById(R.id.sentMsg);
        searchEdit = view.findViewById(R.id.search_for_user_with_id);
        handler = new Handler();
        pageLoading = view.findViewById(R.id.pageLoading);
        detailsRelative = view.findViewById(R.id.detailsRelative);
        circleImageView = view.findViewById(R.id.pro_pic_message);
        name_and_age = view.findViewById(R.id.id_name_plus_age);
        about = view.findViewById(R.id.about_id);
        address = view.findViewById(R.id.id_address);
        gender = view.findViewById(R.id.gender_id);
        imageLoading = view.findViewById(R.id.imageIsLoading);
        searchError = view.findViewById(R.id.search_error);
        searchErrorText = view.findViewById(R.id.search_error_text);

        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                theId = s.toString();
                pageLoading.setVisibility(View.VISIBLE);
                detailsRelative.setVisibility(View.GONE);

                searchErrorText.setVisibility(View.GONE);
                searchError.setVisibility(View.GONE);

                if (!theId.trim().isEmpty()) {
                    doTimeDelay(theId);
                } else {
                    pageLoading.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (theId.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                    Toast.makeText(getContext(), "cannot chat with your self", Toast.LENGTH_SHORT).show();
                } else {
                    Query query = FirebaseDatabase.getInstance().getReference("ChatId").child(FirebaseAuth.getInstance().getUid()).child(theId);

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            MessageList messageList = snapshot.getValue(MessageList.class);

//                            if (messageList.getChatId() != null) {
//                                viewModel.sendHiMessage(FirebaseAuth.getInstance().getUid(), theId, messageList.getChatId());
//                            } else {
//                                viewModel.sendHiMessage(FirebaseAuth.getInstance().getUid(), theId, theId + FirebaseAuth.getInstance().getUid());
//                            }

                            if (snapshot.exists()) {

                                viewModel.sendHiMessage(FirebaseAuth.getInstance().getUid(), theId, messageList.getChatId());
                            } else {
                                viewModel.sendHiMessage(FirebaseAuth.getInstance().getUid(), theId, theId + FirebaseAuth.getInstance().getUid());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    Toast.makeText(getContext(), "hi message sent", Toast.LENGTH_SHORT).show();
                }
            }
        });

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(R.drawable.no_p);
        requestOptions.placeholder(R.drawable.no_p);

        viewModel.observeUserDetails().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                pageLoading.setVisibility(View.GONE);
                detailsRelative.setVisibility(View.VISIBLE);

                try {
                    searchErrorText.setVisibility(View.GONE);
                    searchError.setVisibility(View.GONE);
                    name_and_age.setText(user.getName() + "," + user.getAge());
//                    address.setText(user.getLocation());
                    about.setText(user.getAbout());
                    gender.setText(user.getGender());

                    Glide.with(getActivity()).load(user.getProfilePictureUrl())
                            .apply(requestOptions)
                            .addListener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    imageLoading.setVisibility(View.GONE);
                                    return false;

                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    imageLoading.setVisibility(View.GONE);
                                    return false;
                                }
                            }).into(circleImageView);

                } catch (Exception e) {
                    searchErrorText.setVisibility(View.VISIBLE);
                    searchError.setVisibility(View.VISIBLE);
                    detailsRelative.setVisibility(View.GONE);
                }
            }
        });

        return view;
    }

    private void doTimeDelay(String theId) {

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                doUseIdToGetDetails(theId);
            }
        }, 3000);

    }

    private void doUseIdToGetDetails(String theId) {
        viewModel.doSendId(theId);

//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
//                .child(theId);
//
//        RequestOptions requestOptions = new RequestOptions();
//        requestOptions.error(R.drawable.no_p);
//        requestOptions.placeholder(R.drawable.no_p);
//
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                User user = snapshot.getValue(User.class);
//                pageLoading.setVisibility(View.GONE);
//                detailsRelative.setVisibility(View.VISIBLE);
//
//                try {
//                    name_and_age.setText(user.getName() + "," + user.getAge());
//                    address.setText(user.getAddress());
//                    about.setText(user.getAbout());
//                    gender.setText(user.getGender());
//
//                    Glide.with(getActivity()).load(user.getProfilePictureUrl())
//                            .apply(requestOptions)
//                            .addListener(new RequestListener<Drawable>() {
//                                @Override
//                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                                    imageLoading.setVisibility(View.GONE);
//                                    return false;
//
//                                }
//
//                                @Override
//                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                                    imageLoading.setVisibility(View.GONE);
//                                    return false;
//                                }
//                            }).into(circleImageView);
//
//                } catch (Exception e) {
//                    searchErrorText.setVisibility(View.VISIBLE);
//                    searchError.setVisibility(View.VISIBLE);
//                    detailsRelative.setVisibility(View.GONE);
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//                pageLoading.setVisibility(View.GONE);
//                searchErrorText.setVisibility(View.VISIBLE);
//                searchError.setVisibility(View.VISIBLE);
//            }
//        });

    }
}