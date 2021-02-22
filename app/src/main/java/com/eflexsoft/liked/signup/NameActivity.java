package com.eflexsoft.liked.signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.eflexsoft.liked.R;
import com.eflexsoft.liked.databinding.ActivityNameBinding;

public class NameActivity extends AppCompatActivity {

    ActivityNameBinding binding;

    boolean isNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_name);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_name);

        Intent intent = getIntent();
        isNum = intent.getBooleanExtra("isNum",false);

        binding.toolb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String num = s.toString();

                if (num.trim().isEmpty()) {
                    binding.next.setBackgroundResource(R.drawable.login_btn_background4);
                } else {
                    binding.next.setBackgroundResource(R.drawable.login_btn_background);
                }

                if (num.length()<3){
                    binding.nameLayout.setError("Name most be at least 3 characters");
                }else {
                    binding.nameLayout.setError(null);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void doNext(View view) {

        String name = binding.name.getText().toString();

        if (!name.trim().isEmpty()) {
            Intent intent = new Intent(this,AgeActivity.class);
            intent.putExtra("name",name);
            intent.putExtra("isNum",isNum);
            startActivity(intent);
        }

    }
}