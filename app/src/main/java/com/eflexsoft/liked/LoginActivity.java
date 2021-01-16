package com.eflexsoft.liked;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.eflexsoft.liked.viewmodel.LoginViewModel;
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

public class LoginActivity extends AppCompatActivity {

    MaterialEditText email;
    MaterialEditText password;
    ProgressDialog progressDialog;

    LoginViewModel viewModel;
    SignInButton signInButton;

    GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        BlurImageView blurImageView = findViewById(R.id.back_image);
        blurImageView.setBlur(1);
        blurImageView.setBitmapScale(1f);
        signInButton = findViewById(R.id.google);

        email = findViewById(R.id.Email);
        password = findViewById(R.id.Password);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("signing in");
        progressDialog.setCancelable(false);
        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
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

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = googleSignInClient.getSignInIntent();
                startActivityForResult(intent, 6);
            }
        });

    }

    public void openCreateAccount(View view) {
        startActivity(new Intent(this, CreateAccountEmailPasswordActivity.class));
    }

    public void letsGo(View view) {
        String getEmail = email.getText().toString();
        String getPassword = password.getText().toString();

        if (getEmail.trim().isEmpty()) {
            email.setError("missing");
            return;
        }

        if (getPassword.trim().isEmpty()) {
            password.setError("missing");
            return;
        }

        if (getPassword.length() < 8) {
            password.setError("too short (at lest 8 characters)");
            return;
        }

        viewModel.doLogin(getEmail, getPassword);
        progressDialog.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 6) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSignInAccount googleSignInAccount = null;

            try {
                googleSignInAccount = task.getResult(ApiException.class);
                AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(),null);
                viewModel.loginCredential(authCredential);
                progressDialog.show();
            } catch (ApiException e) {
                e.printStackTrace();
                Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show();
            }



        }

    }

    public void openPhoneLog(View view) {
        startActivity(new Intent(this, LoginNumActivity.class));
    }
}