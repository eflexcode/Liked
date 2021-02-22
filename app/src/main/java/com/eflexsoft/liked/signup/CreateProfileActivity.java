package com.eflexsoft.liked.signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.eflexsoft.liked.R;
import com.eflexsoft.liked.databinding.ActivityCreateProfileBinding;

public class CreateProfileActivity extends AppCompatActivity {

    ActivityCreateProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_create_profile);

        binding = DataBindingUtil.setContentView(this,R.layout.activity_create_profile);

        binding.chandeProPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}