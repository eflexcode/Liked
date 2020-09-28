package com.eflexsoft.liked;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.eflexsoft.liked.viewmodel.CreateAccountViewModel;
import com.eflexsoft.liked.viewmodel.LoginViewModel;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.concurrent.TimeUnit;

public class LoginNumActivity extends AppCompatActivity {

    MaterialEditText number;
    MaterialEditText code;
    Button sendButton;
    CountryCodePicker countryCodePicker;

    ProgressDialog progressDialog;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;

    boolean isCodeSent = false;

    String id;

    LoginViewModel viewModel;

    String getNumber;
    String getCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_num);


        number = findViewById(R.id.number);
        code = findViewById(R.id.code);
        sendButton = findViewById(R.id.letGo);
        countryCodePicker = findViewById(R.id.ccp);
        countryCodePicker.registerCarrierNumberEditText(number);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing in");
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

        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                viewModel.loginCredential(phoneAuthCredential);
                progressDialog.show();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                progressDialog.dismiss();
                Toast.makeText(LoginNumActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void sendCode(View view) {

        getNumber = number.getText().toString();

        if (isCodeSent) {
            getCode = code.getText().toString();
            if (getCode.trim().trim().isEmpty()) {
                code.setError("missing");
                return;
            }

            PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(id, getCode);
            viewModel.loginCredential(phoneAuthCredential);
            progressDialog.show();
        } else {
            if (getNumber.trim().isEmpty()) {
                number.setError("missing");
                return;
            }

            PhoneAuthProvider.getInstance().verifyPhoneNumber(countryCodePicker.getFullNumberWithPlus(), 120, TimeUnit.SECONDS, this, callbacks);

        }

    }
}