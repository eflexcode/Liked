package com.eflexsoft.liked;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.eflexsoft.liked.databinding.ActivityLoginEmailBinding;
import com.eflexsoft.liked.viewmodel.LoginViewModel;

public class LoginEmailActivity extends AppCompatActivity {

    ActivityLoginEmailBinding binding;
    LoginViewModel viewModel;
    ProgressDialog progressDialog;

    boolean emailEmpty;
    boolean passwordEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login_email);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_email);

        setSupportActionBar(binding.toolb);

//        binding.toolb.setNavigationIcon(R.drawable.back);

        binding.toolb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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

        binding.password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                binding.passwordLayout.setError(null);

                String password = s.toString();

                if (password.trim().isEmpty()) {
                    passwordEmpty = false;
                } else {
                    passwordEmpty = true;
                }
                changeSignBntBackground();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        binding.email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String email = s.toString();

                if (email.trim().isEmpty()) {
                    emailEmpty = false;
                } else {
                    emailEmpty = true;
                }
                changeSignBntBackground();

                if (email.contains("@") && email.contains(".com")){
                    binding.emailLayout.setError(null);
                }else {
                    binding.emailLayout.setError("Invalid email address");
                }

//
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void SingInEmail(View view) {

        String email = binding.email.getText().toString();
        String password = binding.password.getText().toString();

        if (!email.trim().isEmpty() && !password.trim().isEmpty()) {
            viewModel.doLogin(email, password);
            progressDialog.show();
        }

    }

    public void changeSignBntBackground() {

        if (emailEmpty && passwordEmpty) {
            binding.singIn.setBackgroundResource(R.drawable.login_btn_background);
        } else {
            binding.singIn.setBackgroundResource(R.drawable.login_btn_background4);
        }

    }

}