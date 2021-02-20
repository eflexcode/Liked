package com.eflexsoft.liked;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.eflexsoft.liked.fragment.SingInFragmentBottomSheet;
import com.eflexsoft.liked.viewmodel.LoginViewModel;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.GoogleAuthProvider;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class LoginActivity extends AppCompatActivity {

    MaterialEditText email;
    MaterialEditText password;
    ProgressDialog progressDialog;

    LoginViewModel viewModel;
    SignInButton signInButton;

    GoogleSignInClient googleSignInClient;

    CallbackManager callbackManager;

    AlertDialog alertDialog;

    FusedLocationProviderClient fusedLocationProviderClient;

    boolean facebook;

    Date date1;

    int userAge;
    String userGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        BlurImageView blurImageView = findViewById(R.id.back_image);
//        blurImageView.setBlur(1);
//        blurImageView.setBitmapScale(1f);
//        signInButton = findViewById(R.id.google);
//
//        email = findViewById(R.id.Email);
//        password = findViewById(R.id.Password);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 89);

        }
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

        EditText editText = new EditText(this);
        editText.setHint("18+");
        editText.setWidth(5);
        editText.setHeight(5);
        editText.setMaxLines(3);
//        editText.
        editText.setPadding(30, 30, 30, 30);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
//
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(5,5);
//        params.setMargins(50,0,50,0);
//
//        editText.setLayoutParams(params);

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.age_layout);

        Dialog dialogGender = new Dialog(this);
        dialogGender.setContentView(R.layout.gender_layout);

//        TextInputEditText textInputEditText = dialog.findViewById(R.id.age);
        TextView ageText = dialog.findViewById(R.id.contiue);
        SingleDateAndTimePicker singleDateAndTimePicker = dialog.findViewById(R.id.date_selector);

        RadioButton male = dialogGender.findViewById(R.id.male);
        RadioButton female = dialogGender.findViewById(R.id.female);

        TextView genderText = dialogGender.findViewById(R.id.continue_gender);

        singleDateAndTimePicker.addOnDateChangedListener(new SingleDateAndTimePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(String displayed, Date date) {
                date1 = date;
            }
        });

        ageText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();

                Calendar calendar2 = Calendar.getInstance();
                calendar2.set(Calendar.YEAR, 2008);
                calendar2.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
                calendar2.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));


                if (date1 != null) {

                    if (date1.after(calendar.getTime())) {

                        Toast.makeText(LoginActivity.this, "Invalid date selected", Toast.LENGTH_SHORT).show();

                    } else if (date1.getYear() > calendar2.getTime().getYear()) {

                        Toast.makeText(LoginActivity.this, "Selected date is too young", Toast.LENGTH_SHORT).show();

                    } else if (date1.getYear() == calendar2.getTime().getYear() && date1.getMonth() > calendar2.getTime().getMonth()) {
                        Toast.makeText(LoginActivity.this, "Selected date is too young", Toast.LENGTH_SHORT).show();

                    } else if (date1.getYear() == calendar2.getTime().getYear()
                            && date1.getMonth() == calendar2.getTime().getMonth() && date1.getDay() > calendar2.getTime().getDay()) {
                        Toast.makeText(LoginActivity.this, "Selected date is too young", Toast.LENGTH_SHORT).show();

                    } else {

                        String dateOfBirth = DateFormat.getDateInstance(DateFormat.MEDIUM).format(date1);

//                        int userAge = new LocalDat.now(); - date1.getYear();

                        Calendar dob = Calendar.getInstance();
                        dob.set(date1.getYear(), date1.getMonth(), date1.getMinutes());

                        int age = calendar.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

                        if (calendar.get(Calendar.DAY_OF_YEAR) > dob.get(Calendar.DAY_OF_YEAR)) {
                            age--;
                        }

                        String theage = String.valueOf(age).substring(2);

//                        Toast.makeText(LoginActivity.this,String.valueOf(age).substring(2), Toast.LENGTH_LONG).show();
                        userAge = Integer.parseInt(theage);

                        dialog.dismiss();

                        date1 = null;

                        dialogGender.show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Please pick a date", Toast.LENGTH_SHORT).show();

                }


            }
        });

        male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    userGender = "Male";
                }
            }
        });

        female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    userGender = "Female";
                }
            }
        });

        alertDialog = new AlertDialog.Builder(this)
                .setTitle("Please enter your age")
                .setView(R.layout.age_layout)
                .setCancelable(false)
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//
                        String age = editText.getText().toString();
                        Toast.makeText(LoginActivity.this, age, Toast.LENGTH_SHORT).show();

//
//                        if (age != null) {
//
//                            int mAge = Integer.parseInt(age);
//
//                            if (mAge > 18) {
//                                dialog.dismiss();
//                            }
//
//                        }

                    }
                })
                .create();

//        alertDialog.show();
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        viewModel.getGoogleMutableLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

                if (aBoolean) {

                    dialog.show();
                    facebook = false;
                }
            }
        });
        FacebookSdk.sdkInitialize(this.getApplicationContext());

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleToken(loginResult.getAccessToken());
                progressDialog.show();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

//        signInButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = googleSignInClient.getSignInIntent();
//                startActivityForResult(intent, 6);
//            }
//        });

        viewModel.getFbMutableLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    dialog.show();
                    facebook = true;
                }
            }
        });

        genderText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userGender == null) {
                    Toast.makeText(LoginActivity.this, "Please select your gender", Toast.LENGTH_SHORT).show();
                } else {
                    if (facebook) {
                        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));

                    } else {
                        Intent intent = googleSignInClient.getSignInIntent();
                        startActivityForResult(intent, 6);
                    }
                    dialogGender.dismiss();
                }

            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        viewModel.getAccessUserLocationMutableLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
//                    progressDialog.dismiss();
//                    alertDialog.show();

//                    progressDialog.setMessage("Getting user location");
//
//                    if (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                        // TODO: Consider calling
//                        //    ActivityCompat#requestPermissions
//                        // here to request the missing permissions, and then overriding
//                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                        //                                          int[] grantResults)
//                        // to handle the case where the user grants the permission. See the documentation
//                        // for ActivityCompat#requestPermissions for more details.
//                        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
//                            @Override
//                            public void onSuccess(Location location) {
//
//                                if (location != null) {
//
//                                    viewModel.updateMyLocation(location.getLongitude(), location.getLatitude());
//                                }
//
//                            }
//                        }).addOnSuccessListener(new OnSuccessListener<Location>() {
//                            @Override
//                            public void onSuccess(Location location) {
//
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
//                            }
//                        });
//
//                    }


                }
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

        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 6) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSignInAccount googleSignInAccount = null;

            try {
                googleSignInAccount = task.getResult(ApiException.class);
                AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
                viewModel.loginCredential(authCredential, userAge, userGender, this);
                progressDialog.show();
            } catch (ApiException e) {
                e.printStackTrace();
                Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show();
            }

        }

    }

    public void handleToken(AccessToken token) {
        AuthCredential authCredential = FacebookAuthProvider.getCredential(token.getToken());

        viewModel.doSignInWithFb(token, authCredential, userAge, userGender, this);
    }

    public void openPhoneLog(View view) {
        startActivity(new Intent(this, LoginNumActivity.class));
    }

    public void singInNumber(View view) {

        SingInFragmentBottomSheet singInFragmentBottomSheet = new SingInFragmentBottomSheet("number");
        singInFragmentBottomSheet.show(getSupportFragmentManager(), "number");

    }

    public void singInGoogle(View view) {
        SingInFragmentBottomSheet singInFragmentBottomSheet = new SingInFragmentBottomSheet("google");
        singInFragmentBottomSheet.show(getSupportFragmentManager(), "google");
    }

    public void singInEmail(View view) {
        SingInFragmentBottomSheet singInFragmentBottomSheet = new SingInFragmentBottomSheet("email");
        singInFragmentBottomSheet.show(getSupportFragmentManager(), "email");

//        startActivity(new Intent(this,LoginEmailActivity.class));
    }

    public void singInFb(View view) {
        SingInFragmentBottomSheet singInFragmentBottomSheet = new SingInFragmentBottomSheet("facebook");
        singInFragmentBottomSheet.show(getSupportFragmentManager(), "facebook");
    }
}