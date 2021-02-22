package com.eflexsoft.liked.signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.eflexsoft.liked.R;
import com.eflexsoft.liked.databinding.ActivityEmailBinding;

public class EmailActivity extends AppCompatActivity {

    ActivityEmailBinding binding;

    String name;
    String age;
    String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_email);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_email);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        age = intent.getStringExtra("age");
        gender = intent.getStringExtra("gender");
        binding.toolb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        Toast.makeText(this, name+age+gender, Toast.LENGTH_LONG).show();
        binding.email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String email = s.toString();

                if (email.contains("@") && email.contains(".com")){
                    binding.emailLayout.setError(null);
                }else {
                    binding.emailLayout.setError("Invalid email address");
                }

                if (email.trim().isEmpty()){
                    binding.next.setBackgroundResource(R.drawable.login_btn_background4);
                }else {
                    binding.next.setBackgroundResource(R.drawable.login_btn_background);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    public void doNext(View view) {

        String email = binding.email.getText().toString();

        if (!email.trim().isEmpty()){
            Intent intent = new Intent(EmailActivity.this, PasswordActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("age", age);
            intent.putExtra("gender",gender);
            intent.putExtra("email",email);
            startActivity(intent);
        }

    }
}