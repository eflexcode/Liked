package com.eflexsoft.liked;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.eflexsoft.liked.databinding.ActivityLoginNumBinding;
import com.eflexsoft.liked.viewmodel.CreateAccountViewModel;
import com.eflexsoft.liked.viewmodel.LoginViewModel;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.concurrent.TimeUnit;

import io.michaelrocks.callmemaybe.CallMeMaybe;
import io.michaelrocks.callmemaybe.FormatParameters;

public class LoginNumActivity extends AppCompatActivity {
    //
//    MaterialEditText number;
//    MaterialEditText code;
//    Button sendButton;
//    CountryCodePicker countryCodePicker;
//
    ProgressDialog progressDialog;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;

    boolean isCodeSent = false;

    String id;

    LoginViewModel viewModel;

    String getNumber;
    String getCode;

    ActivityLoginNumBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login_num);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_num);

        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        number = findViewById(R.id.number);
//        code = findViewById(R.id.code);
//        sendButton = findViewById(R.id.letGo);
//        countryCodePicker = findViewById(R.id.ccp);

        binding.ccp.registerCarrierNumberEditText(binding.number);
//        binding.ccp.for

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing in");
        progressDialog.setCancelable(false);

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
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
                viewModel.loginCredentialPhone(phoneAuthCredential);
                progressDialog.show();
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                progressDialog.dismiss();
                Toast.makeText(LoginNumActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                Log.d(TAG, "onVerificationFailed: " + e.getMessage());
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                id = s;
                isCodeSent = true;
                binding.sendCode.setVisibility(View.GONE);
                binding.verifyLayout.setVisibility(View.VISIBLE);
            }
        };

        binding.number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String num = s.toString();

                if (num.trim().isEmpty()) {
                    binding.letGo.setBackgroundResource(R.drawable.login_btn_background4);
                } else {
                    binding.letGo.setBackgroundResource(R.drawable.login_btn_background);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private static final String TAG = "LoginNumActivity";

    public void sendCode(View view) {

//        binding.sendCode.setVisibility(View.GONE);
//        binding.verifyLayout.setVisibility(View.VISIBLE);

        String number = binding.number.getText().toString();

        if (!number.trim().isEmpty() && number.length() >= 10) {
            Toast.makeText(this, "Sending OTP", Toast.LENGTH_SHORT).show();
            PhoneAuthProvider.getInstance().verifyPhoneNumber(binding.ccp.getFullNumberWithPlus(), 120, TimeUnit.SECONDS, this, callbacks);
        }

//        getNumber = number.getText().toString();
//
//        if (isCodeSent) {
//            getCode = code.getText().toString();
//            if (getCode.trim().trim().isEmpty()) {
//                code.setError("missing");
//                return;
//            }
//
//            PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(id, getCode);
//            viewModel.loginCredential(phoneAuthCredential);
//            progressDialog.show();
//        } else {
//            if (getNumber.trim().isEmpty()) {
//                number.setError("missing");
//                return;
//            }
//
//            PhoneAuthProvider.getInstance().verifyPhoneNumber(countryCodePicker.getFullNumberWithPlus(), 120, TimeUnit.SECONDS, this, callbacks);
//
//        }

    }

    public void verify(View view) {

        String code = binding.otpView.getOTP();

        if (!code.trim().isEmpty()) {

            PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(id, code);
            viewModel.loginCredentialPhone(phoneAuthCredential);
            progressDialog.show();

        }
    }
}