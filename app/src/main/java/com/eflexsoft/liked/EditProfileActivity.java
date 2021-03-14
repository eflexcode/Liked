package com.eflexsoft.liked;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.eflexsoft.liked.databinding.ActivityEditProfileBinding;
import com.eflexsoft.liked.fragment.PickDateFragmentDialog;
import com.eflexsoft.liked.signup.CreateProfileActivity;
import com.eflexsoft.liked.viewmodel.EditProfileViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EditProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    AlertDialog alertDialog;

    String name;
    int age;
    String about;
    String location;
    String profileImageUrl;
    String dis1;
    String dis2;
    String dis3;
    String gender;

    Intent intent;

    ProgressDialog progressDialog;

    EditProfileViewModel viewModel;

    ActivityEditProfileBinding binding;

    boolean isAgeAbove18;
    int userAge;

    FusedLocationProviderClient fusedLocationProviderClient;

    LocationManager locationManager;

    String address = "";
    double lon;
    double lat;

    Handler handler = new Handler();

    int galleryProfile = 45;
    int galleryDis1 = 46;
    int galleryDis2 = 47;
    int galleryDis3 = 48;

    Uri profileUri;
    Uri dis1Uri;
    Uri dis2Uri;
    Uri dis3Uri;

    boolean profileSet;
    boolean dis1Set;
    boolean dis2Set;
    boolean dis3Set;

    String newGender;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);

//        setContentView(R.layout.activity_edit_profile);

        String[] listGender = {"Select your gender", "Male", "Female"};

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, listGender);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.gender.setAdapter(arrayAdapter);

        binding.gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                newGender = arrayAdapter.getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        intent = getIntent();
        name = intent.getStringExtra("name");
        age = intent.getIntExtra("age", 0);
        about = intent.getStringExtra("about");
        location = intent.getStringExtra("location");
        profileImageUrl = intent.getStringExtra("profileImageUrl");
        dis1 = intent.getStringExtra("dis1");
        dis2 = intent.getStringExtra("dis2");
        gender = intent.getStringExtra("gender");
        dis3 = intent.getStringExtra("dis3");

        binding.name.setText(name);
        binding.about.setText(about);
        binding.location.setText(location);
        binding.age.setText(String.valueOf(age));

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(R.color.grey);
        requestOptions.placeholder(R.color.grey);

        Glide.with(this).load(dis1)
                .apply(requestOptions)
                .into(binding.displayImage1);

        Glide.with(this).load(dis2)
                .apply(requestOptions)
                .into(binding.displayImage2);

        Glide.with(this).load(dis3)
                .apply(requestOptions)
                .into(binding.displayImage3);

        RequestOptions requestOptions2 = new RequestOptions();
        requestOptions.error(R.drawable.no_p);
        requestOptions.placeholder(R.drawable.no_p);

        Glide.with(this).load(profileImageUrl)
                .apply(requestOptions2)
                .into(binding.proPicMe);

        for (int i = 0; i < binding.gender.getCount(); i++) {

            if (binding.gender.getItemAtPosition(i).equals(gender)) {
                binding.gender.setSelection(i);

            }

        }

        viewModel = new ViewModelProvider(this).get(EditProfileViewModel.class);


        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Discard Edit?")
                .setMessage("Are you sure you want to discard your edit so far?")
                .setPositiveButton("Keep editing", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
//                      overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    }
                }).setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finish();
                    }
                });

        alertDialog = builder.create();

        binding.age.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    PickDateFragmentDialog dateFragmentDialog = new PickDateFragmentDialog(age);
                    dateFragmentDialog.show(getSupportFragmentManager(), "date");

                }

                return true;
            }

        });

        binding.location.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    if (ActivityCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.

                        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                                || locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {

                                    lon = location.getLongitude();
                                    lat = location.getLatitude();

                                    Thread thread = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            getUserAddressFromLonAndLat(location.getLongitude(), location.getLatitude());
                                        }
                                    });
                                    thread.start();

                                }
                            });
                        } else {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }


                    } else {
                        ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 89);

                    }

                }

                return true;
            }

        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving...");
        progressDialog.setCancelable(false);
//
//        viewModel.isFailureLiveData().observe(this, new Observer<Boolean>() {
//            @Override
//            public void onChanged(Boolean aBoolean) {
//                if (aBoolean){
//                    progressDialog.dismiss();
//                }
//            }
//        });
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.success_sound);
        viewModel.isSuccessLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    mediaPlayer.start();
                    progressDialog.dismiss();
                    finish();
//                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                }
            }
        });

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar select = Calendar.getInstance();
        select.set(Calendar.YEAR, year);
        select.set(Calendar.MONTH, month);
        select.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        Date date = select.getTime();

        Calendar calendar = Calendar.getInstance();

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.YEAR, 2003);
        calendar2.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        calendar2.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));

        if (date.after(calendar2.getTime())) {
            Toast.makeText(this, "Invalid age", Toast.LENGTH_SHORT).show();
            isAgeAbove18 = false;
        } else if (date.getYear() > calendar2.getTime().getYear()) {
            Toast.makeText(this, "Invalid age", Toast.LENGTH_SHORT).show();
            isAgeAbove18 = false;
        } else if (date.getYear() == calendar2.getTime().getYear() && date.getMonth() > calendar2.getTime().getMonth()) {
            Toast.makeText(this, "Invalid age", Toast.LENGTH_SHORT).show();
            isAgeAbove18 = false;
        } else if (date.getYear() == calendar2.getTime().getYear()
                && date.getMonth() == calendar2.getTime().getMonth() && date.getDay() > calendar2.getTime().getDay()) {
            Toast.makeText(this, "Invalid age", Toast.LENGTH_SHORT).show();
            isAgeAbove18 = false;
        } else {

            isAgeAbove18 = true;

            Calendar dob = Calendar.getInstance();
            dob.set(date.getYear(), date.getMonth(), date.getMinutes());

            int age = calendar.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

            if (calendar.get(Calendar.DAY_OF_YEAR) > dob.get(Calendar.DAY_OF_YEAR)) {
                age -= 1;
            }

            String theage = String.valueOf(age).substring(2);

//                        Toast.makeText(LoginActivity.this,String.valueOf(age).substring(2), Toast.LENGTH_LONG).show();
            userAge = Integer.parseInt(theage);

            binding.age.setText(String.valueOf(userAge));

        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        alertDialog.show();
    }

    private void getUserAddressFromLonAndLat(double longitude, double latitude) {


        StringBuilder stringBuilder = new StringBuilder("");
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());


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
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(EditProfileActivity.this, "Unable to get user location", Toast.LENGTH_SHORT).show();
                }
            });

        }

        handler.post(new Runnable() {
            @Override
            public void run() {
                binding.location.setText(address);
            }
        });

    }

    public void doSaveProfile(View view) {

        String name = binding.name.getText().toString();
        String about = binding.about.getText().toString();
        String age = binding.age.getText().toString();
        String location = binding.location.getText().toString();

        if (name.trim().isEmpty() || about.trim().isEmpty() || location.trim().isEmpty() || newGender.equals("Select your gender")) {
            Toast.makeText(this, "Unable to save. some field may be missing", Toast.LENGTH_SHORT).show();
        } else {

//            if ()
//
//            doTextUpdate(name,about,age);
//            doImageUpdate();

//            if (lon == 0 && lat == 0) {
            viewModel.updateProfile(name, lon, lat, newGender, age, about, profileUri, dis1Uri, dis2Uri, dis3Uri);

            progressDialog.show();

        }


    }

    public void pickProfilePic(View view) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    12);
        } else {


            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, galleryProfile);
        }
    }

    public void pickDisPic1(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    12);
        } else {


            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, galleryDis1);
        }
    }

    public void pickDisPic2(View view) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    12);
        } else {


            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, galleryDis2);
        }
    }

    public void pickDisPic3(View view) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    12);
        } else {


            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, galleryDis3);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == galleryProfile) {

                profileUri = data.getData();
                binding.proPicMe.setImageURI(profileUri);
                profileSet = true;

            } else if (requestCode == galleryDis1) {
                dis1Uri = data.getData();
                binding.displayImage1.setImageURI(dis1Uri);
                dis1Set = true;

            } else if (requestCode == galleryDis2) {
                dis2Uri = data.getData();
                binding.displayImage2.setImageURI(dis2Uri);
                dis2Set = true;

            } else if (requestCode == galleryDis3) {
                dis3Uri = data.getData();
                binding.displayImage3.setImageURI(dis3Uri);
                dis3Set = true;

            }

        }


    }

}