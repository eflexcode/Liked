package com.eflexsoft.liked.fragment;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Handler;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.eflexsoft.liked.EditProfileActivity;
import com.eflexsoft.liked.LoginActivity;
import com.eflexsoft.liked.R;
import com.eflexsoft.liked.model.User;
import com.eflexsoft.liked.viewmodel.ProfileViewModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    AppBarLayout appBarLayout;
    TextView profileText;
    ImageView proPicMe;
    ProgressBar progressBar;
    FloatingActionButton floatingActionButton;
    FloatingActionButton editProfileFloatingActionButton;

    ProfileViewModel viewModel;

    TextView id;
    TextView name;
    TextView about;
    TextView address;
    TextView gender;
    TextView age;
    TextView email;
    TextView phoneNumber;

    Dialog dialog;
    ProgressDialog progressDialog;
    Switch aSwitch;

    ImageView moreImage;

    String nameId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        appBarLayout = view.findViewById(R.id.app);
        profileText = view.findViewById(R.id.profileText);
        proPicMe = view.findViewById(R.id.pro_pic_me);
        progressBar = view.findViewById(R.id.imageIsLoading);
        floatingActionButton = view.findViewById(R.id.fabPickUploadType);
        editProfileFloatingActionButton = view.findViewById(R.id.fabEdit);
        id = view.findViewById(R.id.profileId);
        name = view.findViewById(R.id.userName);
        about = view.findViewById(R.id.aboutUser);
        address = view.findViewById(R.id.userAddress);
        gender = view.findViewById(R.id.useGender);
        age = view.findViewById(R.id.userAge);
        email = view.findViewById(R.id.userEmail);
        phoneNumber = view.findViewById(R.id.userPhoneNum);
        aSwitch = view.findViewById(R.id.setIsOnline);
        moreImage = view.findViewById(R.id.moreButton);

        registerForContextMenu(moreImage);

        Toolbar toolbar = view.findViewById(R.id.profile_toolbar);


        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Uploading");
        progressDialog.setCancelable(false);
        viewModel = ViewModelProviders.of(getActivity()).get(ProfileViewModel.class);

        Button camera;
        Button gallery;

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.chose_upload_pic);
        camera = dialog.findViewById(R.id.camera);
        gallery = dialog.findViewById(R.id.gallery);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });

        editProfileFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                intent.putExtra("name", name.getText().toString());
                intent.putExtra("age", age.getText().toString());
                intent.putExtra("address", address.getText().toString());
                intent.putExtra("aboutMe", about.getText().toString());
                intent.putExtra("gender", gender.getText().toString());
                startActivity(intent, ActivityOptions.makeCustomAnimation(getContext(), android.R.anim.slide_in_left, android.R.anim.slide_out_right).toBundle());

            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 9);
                dialog.dismiss();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 10);
                dialog.dismiss();
            }
        });

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    viewModel.setIsOnline("yes");
                } else {
                    viewModel.setIsOnline("no");
                }
            }
        });

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(R.drawable.no_p);
        requestOptions.placeholder(R.drawable.no_p);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset != 0) {
                    profileText.setVisibility(View.VISIBLE);
                    moreImage.setVisibility(View.VISIBLE);
                } else {
                    profileText.setVisibility(View.GONE);
                    moreImage.setVisibility(View.GONE);
                }
            }
        });

        viewModel.observeUserDetails().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {

                Glide.with(getActivity()).load(user.getProfilePictureUrl())
                        .apply(requestOptions)
                        .addListener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;

                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        }).into(proPicMe);

                name.setText(user.getName());
                nameId = user.getName();
                age.setText(String.valueOf(user.getAge()));
                about.setText(user.getAbout());
                id.setText(user.getId());
//                address.setText(user.getLocation());
                gender.setText(user.getGender());

                if (user.getEmail() != null) {
                    email.setText(user.getEmail());
                } else {
                    email.setText("No email address found");
                }

                if (user.getPhoneNumber() != null) {
                    phoneNumber.setText(user.getPhoneNumber());
                } else {
                    phoneNumber.setText("No phone number found");
                }

                Handler handler = new Handler();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            if (user.getIsOnline().equals("yes")) {
                                aSwitch.setText("Online");
                            } else {
                                aSwitch.setText("Offline");
                            }
                        } catch (NullPointerException e) {

                        }

                    }
                }, 3000);


            }

        });

        viewModel.booleanLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    progressDialog.show();
                } else {
                    progressDialog.dismiss();
                }
            }
        });
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 9 && resultCode == RESULT_OK) {
            viewModel.uploadImageUri(data.getData());
            progressDialog.show();
        }

        if (requestCode == 10 && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            Bitmap bitmap = (Bitmap) bundle.get("data");
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

            byte[] bytes = byteArrayOutputStream.toByteArray();
            viewModel.uploadImageByte(bytes);
            progressDialog.show();
        }

    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getActivity().getMenuInflater().inflate(R.menu.profile_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                return true;
            case R.id.shareId:
                doShareUId();
                return true;
            default:
                return super.onContextItemSelected(item);

        }

    }

    private void doShareUId() {

        String subject = "send my liked user id";
        String text = "hi i'm " + nameId + " this is my liked ID please search and send a hi message\n\n" + FirebaseAuth.getInstance().getUid();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(Intent.createChooser(intent, "share your id with :"));

    }
}