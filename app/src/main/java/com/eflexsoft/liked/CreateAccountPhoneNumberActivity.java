package com.eflexsoft.liked;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.eflexsoft.liked.viewmodel.CreateAccountViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import com.jgabrielfreitas.core.BlurImageView;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.concurrent.TimeUnit;

public class CreateAccountPhoneNumberActivity extends AppCompatActivity {

    MaterialEditText name;
    MaterialEditText address;
    MaterialEditText age;
    MaterialEditText aboutMe;
    MaterialEditText gender;
    MaterialEditText number;
    MaterialEditText code;
    Button sendButton;
    CountryCodePicker countryCodePicker;

    CreateAccountViewModel viewModel;
    ProgressDialog progressDialog;

    String getAddress;
    String getGender;
    String getAge;
    String getAboutMe;
    String Names;
    String getNumber;
    String getCode;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;

    boolean isCodeSent = false;

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acount_phone_number);

        name = findViewById(R.id.Names);
        address = findViewById(R.id.Address);
        age = findViewById(R.id.Age);
        aboutMe = findViewById(R.id.AboutMe);
        gender = findViewById(R.id.Gender);
        number = findViewById(R.id.number);
        code = findViewById(R.id.code);
        sendButton = findViewById(R.id.letGo);
        countryCodePicker = findViewById(R.id.ccp);
        countryCodePicker.registerCarrierNumberEditText(number);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating account");
        progressDialog.setCancelable(false);

        gender.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showGenderDialog();
                }

                return true;
            }

        });

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

        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                viewModel.createAccountCredential(phoneAuthCredential,Names,getAddress,getGender,getAge,getAboutMe);
                progressDialog.show();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                progressDialog.dismiss();
                Toast.makeText(CreateAccountPhoneNumberActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                id = s;
                isCodeSent = true;
                sendButton.setText("Continue");
                code.setVisibility(View.VISIBLE);

            }
        };

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

    public void sendCode(View view) {
        getAddress = address.getText().toString();
        getGender = gender.getText().toString();
        getAge = age.getText().toString();
        getAboutMe = aboutMe.getText().toString();
        Names = name.getText().toString();
        getNumber = number.getText().toString();

        if (isCodeSent) {
            getCode = code.getText().toString();
            if (getCode.trim().trim().isEmpty()) {
                code.setError("missing");
                return;
            }

            PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(id, getCode);
            viewModel.createAccountCredential(phoneAuthCredential,Names,getAddress,getGender,getAge,getAboutMe);
            progressDialog.show();
        } else {


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

            if (ageNum < 18) {
                age.setError("age cannot be below 18");
                return;
            }

            if (getAboutMe.trim().isEmpty()) {
                aboutMe.setError("missing");
                return;
            }

            if (getNumber.trim().isEmpty()) {
                number.setError("missing");
                return;
            }

            PhoneAuthProvider.getInstance().verifyPhoneNumber(countryCodePicker.getFullNumberWithPlus(), 120, TimeUnit.SECONDS, this, callbacks);
        }
    }
}