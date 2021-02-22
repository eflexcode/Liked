package com.eflexsoft.liked.signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.eflexsoft.liked.R;
import com.eflexsoft.liked.databinding.ActivityGenderBinding;

public class GenderActivity extends AppCompatActivity {

    ActivityGenderBinding binding;
    String name;
    String age;
    String gender;
    boolean isNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_gender);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_gender);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        age = intent.getStringExtra("age");
        isNum = intent.getBooleanExtra("isNum", false);
//        Toast.makeText(this, name+age, Toast.LENGTH_LONG).show();
        binding.toolb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {


            }
        });

        binding.male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    gender = binding.male.getText().toString();
                    binding.next.setBackgroundResource(R.drawable.login_btn_background);
                }

            }
        });

        binding.female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    gender = binding.female.getText().toString();
                    binding.next.setBackgroundResource(R.drawable.login_btn_background);
                }
            }
        });

    }

    public void doNext(View view) {

        if (gender != null) {
            Intent intent;
            if (isNum) {
                intent = new Intent(GenderActivity.this, PhoneActivity.class);
            } else {
                intent = new Intent(GenderActivity.this, EmailActivity.class);
            }

            intent.putExtra("name", name);
            intent.putExtra("age", age);
            intent.putExtra("gender", gender);

            startActivity(intent);

        }

    }
}