package com.eflexsoft.liked.fragment;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
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
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.eflexsoft.liked.EditProfileActivity;
import com.eflexsoft.liked.ImageFullViewActivity;
import com.eflexsoft.liked.LoginActivity;
import com.eflexsoft.liked.R;
import com.eflexsoft.liked.databinding.FragmentProfileBinding;
import com.eflexsoft.liked.model.User;
import com.eflexsoft.liked.viewmodel.ProfileViewModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {


    ProfileViewModel viewModel;


    Dialog dialog;
    ProgressDialog progressDialog;
    String nameId;

    FragmentProfileBinding binding;

    Handler handler = new Handler();

    String address = "";

    String dis1Url;
    String dis2Url;
    String dis3Url;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);

//        binding.imageCollectionView.addImage(R.drawable.no_p);
//        binding.imageCollectionView.addImage(R.drawable.no_p);
//        binding.imageCollectionView.addImage(R.drawable.no_p);

//binding.iv.a

//        progressDialog = new ProgressDialog(getContext());
//        progressDialog.setMessage("Uploading");
//        progressDialog.setCancelable(false);
        viewModel = new ViewModelProvider(getActivity()).get(ProfileViewModel.class);

//        editProfileFloatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), EditProfileActivity.class);
//                intent.putExtra("name", name.getText().toString());
//                intent.putExtra("age", age.getText().toString());
//                intent.putExtra("address", address.getText().toString());
//                intent.putExtra("aboutMe", about.getText().toString());
//                intent.putExtra("gender", gender.getText().toString());
//                startActivity(intent, ActivityOptions.makeCustomAnimation(getContext(), android.R.anim.slide_in_left, android.R.anim.slide_out_right).toBundle());
//
//            }
//        });
//
//        gallery.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                intent.setType("image/*");
//                startActivityForResult(intent, 9);
//                dialog.dismiss();
//            }
//        });
//
//        camera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent, 10);
//                dialog.dismiss();
//            }
//        });
//
//        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                if (isChecked) {
//                    viewModel.setIsOnline("yes");
//                } else {
//                    viewModel.setIsOnline("no");
//                }
//            }
//        });
//
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(R.drawable.no_p);
        requestOptions.placeholder(R.drawable.no_p);
//
//        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                if (verticalOffset != 0) {
//                    profileText.setVisibility(View.VISIBLE);
//                    moreImage.setVisibility(View.VISIBLE);
//                } else {
//                    profileText.setVisibility(View.GONE);
//                    moreImage.setVisibility(View.GONE);
//                }
//            }
//        });


        viewModel.observeUserDetails().observe(getActivity(), new Observer<User>() {
            @Override
            public void onChanged(User user) {

                if (user != null) {

                    dis1Url = user.getDisplayImage1();
                    dis2Url = user.getDisplayImage2();
                    dis3Url = user.getDisplayImage3();

                    Glide.with(getActivity()).load(user.getProfilePictureUrl())
                            .apply(requestOptions)
                            .into(binding.proPicMe);

                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.error(R.color.grey);
                    requestOptions.placeholder(R.color.grey);

                    Glide.with(getActivity()).load(user.getDisplayImage1())
                            .apply(requestOptions)
                            .into(binding.dis1);

                    Glide.with(getActivity()).load(user.getDisplayImage2())
                            .apply(requestOptions)
                            .into(binding.dis2);

                    Glide.with(getActivity()).load(user.getDisplayImage3())
                            .apply(requestOptions)
                            .into(binding.dis3);

                    Glide.with(getActivity()).load(user.getDisplayImage1())
                            .apply(requestOptions)
                            .into(binding.displayImage1);

                    Glide.with(getActivity()).load(user.getDisplayImage2())
                            .apply(requestOptions)
                            .into(binding.displayImage2);

                    Glide.with(getActivity()).load(user.getDisplayImage3())
                            .apply(requestOptions)
                            .into(binding.displayImage3);

////                    nameId = user.getName();
//
                    binding.about.setText(user.getAbout());
                    binding.age.setText(String.valueOf(user.getAge()));
                    binding.name.setText(user.getName().trim() + ",");


                    if (user.getIsOnline().equals("yes")) {
                        binding.online.setChecked(true);
                        binding.online.setText("I'm online");
                    } else {
                        binding.online.setChecked(false);
                        binding.online.setText("I'm offline");
                    }

//
//                    Handler handler = new Handler();
//
//
//                    Glide.with(getActivity()).load(user.getDisplayImage1()).addListener(new RequestListener<Drawable>() {
//                        @Override
//                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                            binding.imageCollectionView.addImage(resource);
//                            return false;
//                        }
//                    }).apply(requestOptions);//.into(binding.displayImage1);
//                    Glide.with(getActivity()).load(user.getDisplayImage2()).addListener(new RequestListener<Drawable>() {
//                        @Override
//                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                            binding.imageCollectionView.addImage(resource);
//                            return false;
//                        }
//                    }).apply(requestOptions);//.into(binding.displayImage2);
//                    Glide.with(getActivity()).load(user.getDisplayImage3()).addListener(new RequestListener<Drawable>() {
//                        @Override
//                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                            return false;
//                        }
//
//                        @Override
//                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//
//                            binding.imageCollectionView.addImage(resource);
//
//                            return false;
//                        }
//                    }).apply(requestOptions);//.into(binding.displayImage3);


//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        try {
//                            if (user.getIsOnline().equals("yes")) {
//                                aSwitch.setText("Online");
//                            } else {
//                                aSwitch.setText("Offline");
//                            }
//                        } catch (NullPointerException e) {
//
//                        }
//
//                    }
//                }, 3000);

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            getUserAddressFromLonAndLat(user.getLongitude(), user.getLatitude());
                        }
                    });
                    thread.start();

                }
            }
        });

        binding.displayImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), ImageFullViewActivity.class);
                intent.putExtra("url", dis1Url);

                Pair<View, String> pair = Pair.create(binding.displayImage1, binding.displayImage1.getTransitionName());

                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), pair);
                startActivity(intent, activityOptionsCompat.toBundle());

            }
        });


        binding.displayImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), ImageFullViewActivity.class);
                intent.putExtra("url", dis2Url);

                Pair<View, String> pair = Pair.create(binding.displayImage1, binding.displayImage2.getTransitionName());

                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), pair);
                startActivity(intent, activityOptionsCompat.toBundle());
            }
        });

        binding.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(getContext(),v);
                popupMenu.inflate(R.menu.more);
                popupMenu.show();
            }
        });

        binding.displayImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), ImageFullViewActivity.class);
                intent.putExtra("url", dis3Url);

                Pair<View, String> pair = Pair.create(binding.displayImage1, binding.displayImage3.getTransitionName());

                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), pair);
                startActivity(intent, activityOptionsCompat.toBundle());
            }
        });

        binding.online.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if (isChecked) {

                    binding.online.setText("I'm online");
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            viewModel.setIsOnline("yes");
                        }
                    });
                    thread.start();

                } else {

                    binding.online.setText("I'm offline");
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            viewModel.setIsOnline("no");
                        }
                    });
                    thread.start();
                }
            }
        });

//        viewModel.booleanLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
//            @Override
//            public void onChanged(Boolean aBoolean) {
//                if (aBoolean) {
//                    progressDialog.show();
//                } else {
//                    progressDialog.dismiss();
//                }
//            }
//        });
        return binding.getRoot();
    }

    private void getUserAddressFromLonAndLat(double longitude, double latitude) {


        StringBuilder stringBuilder = new StringBuilder("");
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());


        try {

            List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);

            if (addressList != null) {

                Address theAddress = addressList.get(0);


                for (int i = 0; i <= theAddress.getMaxAddressLineIndex(); i++) {

                    stringBuilder.append(theAddress.getAddressLine(i)).append("\n");
                    address = stringBuilder.toString();
                }

            } else {
                address = "No address found";
            }


        } catch (Exception e) {
            Toast.makeText(getContext(), "Unable to get user location", Toast.LENGTH_SHORT).show();
        }

        handler.post(new Runnable() {
            @Override
            public void run() {
                binding.location.setText(address);
            }
        });

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