package com.eflexsoft.liked.signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.eflexsoft.liked.R;
import com.eflexsoft.liked.databinding.ActivityPasswordBinding;
import com.eflexsoft.liked.viewmodel.CreateAccountViewModel;

public class PasswordActivity extends AppCompatActivity {

    ActivityPasswordBinding binding;

    String name;
    String age;
    String gender;
    String email;

    boolean isPasswordOk;
    boolean isCPasswordOk;

    CreateAccountViewModel viewModel;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_password);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_password);
        binding.toolb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        age = intent.getStringExtra("age");
        gender = intent.getStringExtra("gender");
        email = intent.getStringExtra("email");

//        Toast.makeText(this, name+age+gender+email, Toast.LENGTH_LONG).show();

        binding.password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (calculatePasswordStrength(s.toString()) == 1) {
                    binding.passwordLayout.setError("Weak password");
//                } else if (calculatePasswordStrength(s.toString()) == 1) {
//                    binding.passwordLayout.setError("Weak password");
                    isPasswordOk = false;
                } else {
                    binding.passwordLayout.setError(null);
                    isPasswordOk = true;
                }
                changeSignBntBackground();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.confirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (calculatePasswordStrength(s.toString()) == 1) {
                    binding.confirmPasswordLayout.setError("Weak password");
//                } else if (calculatePasswordStrength(s.toString()) == 1) {
//                    binding.confirmPasswordLayout.setError("Weak password");
                    isCPasswordOk = false;
                } else {
                    binding.confirmPasswordLayout.setError(null);
                    isCPasswordOk = true;
                }
                changeSignBntBackground();

            }

            @Override
            public void afterTextChanged(Editable s) {

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

    private int calculatePasswordStrength(String password) {

        String veryWeak = "Very weak password";

        String weak = "Weak password";

        int strengthCount = 0;

        boolean isCharacter = false;
        boolean isNumber = false;
        boolean isUpper = false;
        boolean isLower = false;

        for (int i = 0; i < password.length(); i++) {

            char c = password.charAt(i);

            if (!isNumber && Character.isDigit(c)) {
                strengthCount++;
                isNumber = true;
            } else if (!isUpper && Character.isUpperCase(c)) {
                strengthCount++;
                isUpper = true;
            } else if (!isLower && Character.isLowerCase(c)) {
                strengthCount++;
                isLower = true;
            }
//            else if (!isCharacter && Character.isLetter(c)) {
//                strengthCount++;
//                isCharacter = true;
//            }

        }

        return strengthCount;
    }

    public void CreateAccount(View view) {

        String password = binding.password.getText().toString();
        String cPassword = binding.confirmPassword.getText().toString();

        if (isCPasswordOk && isPasswordOk) {

            if (!password.equals(cPassword)) {

                binding.confirmPasswordLayout.setError("Not the same");
                binding.passwordLayout.setError("Not the same");
            } else {
//                Toast.makeText(this, "sgWWWWWWWWWWWWWWWWWWWWWWWWWWWWW", Toast.LENGTH_SHORT).show();
                progressDialog.show();
                viewModel.createAccountEmailPassword(name, gender, age, email, password);
            }

        }

    }

    public void changeSignBntBackground() {

        if (isPasswordOk && isCPasswordOk) {
            binding.next.setBackgroundResource(R.drawable.login_btn_background);
        } else {
            binding.next.setBackgroundResource(R.drawable.login_btn_background4);
        }

    }
}