package com.eflexsoft.liked.signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.eflexsoft.liked.LoginActivity;
import com.eflexsoft.liked.R;
import com.eflexsoft.liked.databinding.ActivityAgeBinding;
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;

import java.util.Calendar;
import java.util.Date;

public class AgeActivity extends AppCompatActivity {

    ActivityAgeBinding binding;
    boolean isAgeAbove18;
    int userAge;

    String name;
    boolean isNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_age);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_age);

        Intent intent = getIntent();

        name = intent.getStringExtra("name");
        isNum = intent.getBooleanExtra("isNum",false);
//        Toast.makeText(this, name, Toast.LENGTH_LONG).show();
        binding.toolb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        binding.dateSelector.addOnDateChangedListener(new SingleDateAndTimePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(String displayed, Date date) {

                Calendar calendar = Calendar.getInstance();

                Calendar calendar2 = Calendar.getInstance();
                calendar2.set(Calendar.YEAR, 2003);
                calendar2.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
                calendar2.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));

                if (date.after(calendar2.getTime())) {

                    binding.next.setBackgroundResource(R.drawable.login_btn_background4);
                    isAgeAbove18 = false;
                } else if (date.getYear() > calendar2.getTime().getYear()) {

                    binding.next.setBackgroundResource(R.drawable.login_btn_background4);
                    isAgeAbove18 = false;
                } else if (date.getYear() == calendar2.getTime().getYear() && date.getMonth() > calendar2.getTime().getMonth()) {
                    binding.next.setBackgroundResource(R.drawable.login_btn_background4);
                    isAgeAbove18 = false;
                } else if (date.getYear() == calendar2.getTime().getYear()
                        && date.getMonth() == calendar2.getTime().getMonth() && date.getDay() > calendar2.getTime().getDay()) {
                    binding.next.setBackgroundResource(R.drawable.login_btn_background4);
                    isAgeAbove18 = false;
                } else {

                    binding.next.setBackgroundResource(R.drawable.login_btn_background);
                    isAgeAbove18 = true;

                    Calendar dob = Calendar.getInstance();
                    dob.set(date.getYear(), date.getMonth(), date.getMinutes());

                    int age = calendar.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

                    if (calendar.get(Calendar.DAY_OF_YEAR) > dob.get(Calendar.DAY_OF_YEAR)) {
                        age -= 1;
                    }

                    String theage = String.valueOf(age).substring(2);

//                        Toast.makeText(LoginActivity.this,String.valueOf(age).substring(2), Toast.LENGTH_LONG).show();
                    userAge = Integer.parseInt(theage);

                }
            }
        });

        binding.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isAgeAbove18) {

                    Intent intent = new Intent(AgeActivity.this, GenderActivity.class);
                    intent.putExtra("name", name);
                    intent.putExtra("age", String.valueOf(userAge));
                    intent.putExtra("isNum",isNum);
                    startActivity(intent);


                }
            }
        });

    }
}