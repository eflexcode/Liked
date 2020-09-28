package com.eflexsoft.liked;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.eflexsoft.liked.viewmodel.CreateAccountViewModel;
import com.jgabrielfreitas.core.BlurImageView;
import com.rengwuxian.materialedittext.MaterialEditText;

public class CreateAccountEmailPasswordActivity extends AppCompatActivity {

    MaterialEditText name;
    MaterialEditText address;
    MaterialEditText age;
    MaterialEditText aboutMe;
    MaterialEditText gender;
    MaterialEditText email;
    MaterialEditText password;
    MaterialEditText confirmPassword;

    CreateAccountViewModel viewModel;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        BlurImageView blurImageView = findViewById(R.id.back_image);
        blurImageView.setBlur(2);
        blurImageView.setBitmapScale(1f);

        name = findViewById(R.id.Names);
        address = findViewById(R.id.Address);
        age = findViewById(R.id.Age);
        aboutMe = findViewById(R.id.AboutMe);
        gender = findViewById(R.id.Gender);
        email = findViewById(R.id.Email);
        password = findViewById(R.id.Password);
        confirmPassword = findViewById(R.id.ConfirmPassword);

        gender.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showGenderDialog();
                }

                return true;
            }

        });
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating account");
        progressDialog.setCancelable(false);

        viewModel = ViewModelProviders.of(this).get(CreateAccountViewModel.class);
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

    public void tryAnotherWay(View view) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this)
                .setMessage("Continue with")
                .setPositiveButton("Number", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(CreateAccountEmailPasswordActivity.this, CreateAccountPhoneNumberActivity.class));
                        dialog.dismiss();
                    }
                }).setNegativeButton("Google", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(CreateAccountEmailPasswordActivity.this, CreateAccountGoogle.class));
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = alertDialog.create();

        dialog.show();

    }

    public void letsGo(View view) {

        String getEmail = email.getText().toString();
        String getPassword = password.getText().toString();
        String getConfirmPassword = confirmPassword.getText().toString();
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

        if (getEmail.trim().isEmpty()) {
            email.setError("missing");
            return;
        }

        if (getPassword.trim().isEmpty()) {
            password.setError("missing");
            return;
        }

        if (getConfirmPassword.trim().isEmpty()) {
            confirmPassword.setError("missing");
            return;
        }

        if (getPassword.length()<8){
            password.setError("too short (at lest 8 characters)");
            return;
        }

        if (!getPassword.equals(getConfirmPassword)){
            password.setError("mismatch");
            confirmPassword.setError("mismatch");
            return;
        }

            viewModel.createAccountEmailPassword(Names,getAddress,getGender,getAge,getAboutMe,getEmail,getPassword);
            progressDialog.show();
    }
}