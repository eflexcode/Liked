package com.eflexsoft.liked;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.eflexsoft.liked.viewmodel.EditProfileViewModel;
import com.rengwuxian.materialedittext.MaterialEditText;

public class EditProfileActivity extends AppCompatActivity {

    AlertDialog alertDialog;
    MaterialEditText name;
    MaterialEditText address;
    MaterialEditText age;
    MaterialEditText aboutMe;
    MaterialEditText gender;

    Intent intent;

    ProgressDialog progressDialog;

    EditProfileViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        intent = getIntent();

        name = findViewById(R.id.Names);
        address = findViewById(R.id.Address);
        age = findViewById(R.id.Age);
        aboutMe = findViewById(R.id.AboutMe);
        gender = findViewById(R.id.Gender);

        viewModel = new ViewModelProvider(this).get(EditProfileViewModel.class);

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage("Discard Edit?")
                .setPositiveButton("Discard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      finish();
                      overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    }
                }).setNegativeButton("Keep editing", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        gender.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showGenderDialog();
                }

                return true;
            }

        });

        name.setText(intent.getStringExtra("name"));
        age.setText(intent.getStringExtra("age"));
        aboutMe.setText(intent.getStringExtra("aboutMe"));
        address.setText(intent.getStringExtra("address"));
        gender.setText(intent.getStringExtra("gender"));

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("updating profile...");
        progressDialog.setCancelable(false);

        alertDialog = builder.create();

        viewModel.isFailureLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    progressDialog.dismiss();
                }
            }
        });
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.success_sound);
        viewModel.isSuccessLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    mediaPlayer.start();
                    progressDialog.dismiss();
                    finish();
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                }
            }
        });

    }

    private void showGenderDialog() {
        String[] genders = {"Male", "Female"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select your gender");
        builder.setSingleChoiceItems(genders, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        gender.setText("Male");
                        dialog.dismiss();
                        break;
                    case 1:
                        gender.setText("Female");
                        dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        alertDialog.show();
    }

    public void saveUpdate(View view) {

        String getAddress = address.getText().toString();
        String getGender = gender.getText().toString();
        String getAge = age.getText().toString();
        String getAboutMe = aboutMe.getText().toString();
        String Names = name.getText().toString();

        if (Names.trim().isEmpty()) {
            name.setError("missing");
            return;
        }


        if (getAddress.trim().isEmpty()) {
            address.setError("missing");
            return;
        }

        if (getGender.trim().isEmpty()) {
            gender.setError("missing");
            return;
        }

        if (getAge.trim().isEmpty()) {
            age.setError("missing");
            return;
        }

        int ageNum = Integer.parseInt(getAge);
        if (ageNum <18){
            age.setError("age cannot be below 18");
            return;
        }

        if (getAboutMe.trim().isEmpty()) {
            aboutMe.setError("missing");
            return;
        }

        viewModel.updateProfile(Names,getAddress,getGender,getAge,getAboutMe);
        progressDialog.show();

    }
}