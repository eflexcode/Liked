package com.eflexsoft.liked;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.eflexsoft.liked.viewmodel.CreateAccountViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.jgabrielfreitas.core.BlurImageView;
import com.rengwuxian.materialedittext.MaterialEditText;

public class CreateAccountGoogle extends AppCompatActivity {

    MaterialEditText name;
    MaterialEditText address;
    MaterialEditText age;
    MaterialEditText aboutMe;
    MaterialEditText gender;

    CreateAccountViewModel viewModel;
    ProgressDialog progressDialog;

    GoogleSignInClient googleSignInClient;

    String getAddress;
    String getGender;
    String getAge;
    String getAboutMe;
    String Names;
    GoogleSignInAccount signInAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account_google);
        BlurImageView blurImageView = findViewById(R.id.back_image);

        name = findViewById(R.id.Names);
        address = findViewById(R.id.Address);
        age = findViewById(R.id.Age);
        aboutMe = findViewById(R.id.AboutMe);
        gender = findViewById(R.id.Gender);

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

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        SignInButton signInButton = findViewById(R.id.google);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doGoogleSignIn();
//                Intent intent = googleSignInClient.getSignInIntent();
//                CreateAccountGoogle.this.startActivityForResult(intent, 7);

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

    private void doGoogleSignIn() {

        getAddress = address.getText().toString();
        getGender = gender.getText().toString();
        getAge = age.getText().toString();
        getAboutMe = aboutMe.getText().toString();
        Names = name.getText().toString();


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

        Intent intent = googleSignInClient.getSignInIntent();
        startActivityForResult(intent, 7);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7) {
            Task<GoogleSignInAccount> googleSignInAccount = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                signInAccount = googleSignInAccount.getResult(ApiException.class);
            }
            catch (ApiException e) {
                e.printStackTrace();
            }
            assert signInAccount != null;
            AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);

            viewModel.createAccountCredential(authCredential, Names, getGender, getAge);
            progressDialog.show();
        }

    }
}