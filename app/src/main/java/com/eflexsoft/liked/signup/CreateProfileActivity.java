package com.eflexsoft.liked.signup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.eflexsoft.liked.LoginActivity;
import com.eflexsoft.liked.R;
import com.eflexsoft.liked.databinding.ActivityCreateProfileBinding;
import com.eflexsoft.liked.fragment.ImageFragmentBottomSheet;
import com.eflexsoft.liked.fragment.SingInFragmentBottomSheet;
import com.eflexsoft.liked.viewmodel.CreateAccountViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class CreateProfileActivity extends AppCompatActivity {

    ActivityCreateProfileBinding binding;
    AlertDialog alertDialog;

    int galleryProfile = 45;
    int galleryDis1 = 46;
    int galleryDis2 = 47;
    int galleryDis3 = 48;

    int cameraProfile = 55;
    int cameraDis1 = 56;
    int cameraDis2 = 57;
    int cameraDis3 = 58;

    Uri profileUri;
    Uri dis1Uri;
    Uri dis2Uri;
    Uri dis3Uri;

    boolean profileSet;
    boolean dis1Set;
    boolean dis2Set;
    boolean dis3Set;
    boolean aboutText;

    CreateAccountViewModel viewModel;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationManager locationManager;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_create_profile);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_profile);

        viewModel = new ViewModelProvider(this).get(CreateAccountViewModel.class);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 89);

        }

        alertDialog = new AlertDialog.Builder(this)
                .setTitle("Continue with")
                .setMessage("Select where you want your image from")
                .setCancelable(false)
                .setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
//                        startActivityForResult(intent, gallery);

                    }
                }).setNegativeButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();


        viewModel.getCameraMutableLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (ActivityCompat.checkSelfPermission(CreateProfileActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {

                    ActivityCompat.requestPermissions(CreateProfileActivity.this, new String[]{Manifest.permission.CAMERA}, 13);
                } else {
                    if (s.equals("profile")) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, cameraProfile);
                    } else if (s.equals("dis1")) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, cameraDis1);
                    } else if (s.equals("dis2")) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, cameraDis2);
                    } else {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, cameraDis3);
                    }
                }
            }
        });

        viewModel.getGalleryMutableLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {

                if (ActivityCompat.checkSelfPermission(CreateProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED &&
                        ActivityCompat.checkSelfPermission(CreateProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

                    ActivityCompat.requestPermissions(CreateProfileActivity.this, new String[]{
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            12);
                } else {


                    if (s.equals("profile")) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, galleryProfile);

                    } else if (s.equals("dis1")) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, galleryDis1);

                    } else if (s.equals("dis2")) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, galleryDis2);

                    } else {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, galleryDis3);

                    }
                }
            }
        });

        binding.chandeProPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(CreateProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED &&
                        ActivityCompat.checkSelfPermission(CreateProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

                    ActivityCompat.requestPermissions(CreateProfileActivity.this, new String[]{
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            12);
                } else {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, galleryProfile);
                }
//                ImageFragmentBottomSheet bottomSheet = new ImageFragmentBottomSheet("profile");
//                bottomSheet.show(getSupportFragmentManager(), "profile");
            }
        });

        binding.e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(CreateProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED &&
                        ActivityCompat.checkSelfPermission(CreateProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

                    ActivityCompat.requestPermissions(CreateProfileActivity.this, new String[]{
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            12);
                } else {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, galleryProfile);
                }

//                ImageFragmentBottomSheet bottomSheet = new ImageFragmentBottomSheet("profile");
//                bottomSheet.show(getSupportFragmentManager(), "profile");
            }
        });

        binding.about.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().trim().isEmpty()) {
                    aboutText = false;
                } else {
                    aboutText = true;
                }
                updateButtonStatus();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Publishing");
        progressDialog.setCancelable(false);

        viewModel.booleanLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {

                    progressDialog.show();

                } else {
                    progressDialog.dismiss();
                }
            }
        });

//        binding.displayImage1Change.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                alertDialog.show();
//                ImageFragmentBottomSheet bottomSheet = new ImageFragmentBottomSheet("dis1");
//                bottomSheet.show(getSupportFragmentManager(), "dis1");
//            }
//        });
//        binding.displayImage2Change.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                alertDialog.show();
//                ImageFragmentBottomSheet bottomSheet = new ImageFragmentBottomSheet("dis2");
//                bottomSheet.show(getSupportFragmentManager(), "dis2");
//            }
//        });
//        binding.displayImage3Change.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                alertDialog.show();
//                ImageFragmentBottomSheet bottomSheet = new ImageFragmentBottomSheet("dis3");
//                bottomSheet.show(getSupportFragmentManager(), "dis3");
//            }
//        });

    }

    public void dis3Image(View view) {

        if (ActivityCompat.checkSelfPermission(CreateProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED &&
                ActivityCompat.checkSelfPermission(CreateProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(CreateProfileActivity.this, new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    12);
        } else {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, galleryDis3);
        }

//        ImageFragmentBottomSheet bottomSheet = new ImageFragmentBottomSheet("dis3");
//        bottomSheet.show(getSupportFragmentManager(), "dis3");
    }

    public void dis2Image(View view) {

        if (ActivityCompat.checkSelfPermission(CreateProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED &&
                ActivityCompat.checkSelfPermission(CreateProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(CreateProfileActivity.this, new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    12);
        } else {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, galleryDis2);
        }

//        ImageFragmentBottomSheet bottomSheet = new ImageFragmentBottomSheet("dis2");
//        bottomSheet.show(getSupportFragmentManager(), "dis2");
    }

    public void dis1Image(View view) {

        if (ActivityCompat.checkSelfPermission(CreateProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED &&
                ActivityCompat.checkSelfPermission(CreateProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(CreateProfileActivity.this, new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    12);
        } else {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, galleryDis1);
        }

//        ImageFragmentBottomSheet bottomSheet = new ImageFragmentBottomSheet("dis1");
//        bottomSheet.show(getSupportFragmentManager(), "dis1");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // for camera

//        if (resultCode == RESULT_OK) {
//
//            if (requestCode == cameraProfile) {
//
//            } else if (requestCode == cameraDis1) {
//
//            } else if (requestCode == cameraDis2) {
//
//            } else if (requestCode == cameraDis3) {
//
//            }
//
//        }

        if (resultCode == RESULT_OK) {

            if (requestCode == galleryProfile) {

                profileUri = data.getData();
                binding.proPicMe.setImageURI(profileUri);
                profileSet = true;
                updateButtonStatus();

            } else if (requestCode == galleryDis1) {
                dis1Uri = data.getData();
                binding.displayImage1.setImageURI(dis1Uri);
                dis1Set = true;
                updateButtonStatus();

            } else if (requestCode == galleryDis2) {
                dis2Uri = data.getData();
                binding.displayImage2.setImageURI(dis2Uri);
                dis2Set = true;
                updateButtonStatus();

            } else if (requestCode == galleryDis3) {
                dis3Uri = data.getData();
                binding.displayImage3.setImageURI(dis3Uri);
                dis3Set = true;
                updateButtonStatus();

            }

        }


    }

    private void updateButtonStatus() {

        if (profileSet && dis3Set && dis2Set && dis1Set & aboutText) {
            binding.next.setBackgroundResource(R.drawable.login_btn_background);
        } else {
            binding.next.setBackgroundResource(R.drawable.login_btn_background4);
        }

    }

    public void PublishProfile(View view) {

        try {


            if (profileSet && dis3Set && dis2Set && dis1Set & aboutText) {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED ||
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {

                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 89);
                    return;
                }

                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                    Toast.makeText(this, "Please turn on your device location", Toast.LENGTH_SHORT).show();
                    return;
                }

                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        List<Uri> uriList = new ArrayList<>();
                        uriList.add(dis1Uri);
                        uriList.add(dis2Uri);
                        uriList.add(dis3Uri);

                        viewModel.publishProfile(profileUri, binding.about.getText().toString(), uriList, location.getLongitude(), location.getLatitude());
                        progressDialog.show();
//                    Toast.makeText(CreateProfileActivity.this, location.getLatitude()+" "+location.getLatitude(), Toast.LENGTH_LONG).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        List<Uri> uriList = new ArrayList<>();
                        uriList.add(dis1Uri);
                        uriList.add(dis2Uri);
                        uriList.add(dis3Uri);

                        viewModel.publishProfile(profileUri, binding.about.getText().toString(), uriList, 0, 0);
                        progressDialog.show();
                    }
                });

            }

        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }
}