package com.eflexsoft.liked.repository;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.ViewModelStoreOwner;

import com.eflexsoft.liked.LoginActivity;
import com.eflexsoft.liked.MainActivity;
import com.eflexsoft.liked.model.User;
import com.eflexsoft.liked.signup.CreateProfileActivity;
import com.eflexsoft.liked.viewmodel.LoginViewModel;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

import java.util.HashMap;

public class LoginRepository {

    Context context;
    public MutableLiveData<Boolean> booleanMutableLiveData = new MutableLiveData<>();
    LoginViewModel viewModel;

    public LoginRepository(Context context) {
        this.context = context;
//        viewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(LoginViewModel.class);
    }

    public void doLogin(String email, String password) {
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            context.startActivity(intent);
                            booleanMutableLiveData.setValue(false);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                booleanMutableLiveData.setValue(false);
            }
        });

    }

    public void loginCredentialPhone(AuthCredential authCredential) {
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    boolean isUserNew = task.getResult().getAdditionalUserInfo().isNewUser();

                    if (isUserNew) {

                        Toast.makeText(context, "You are a new user please create an account", Toast.LENGTH_LONG).show();

                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                        firebaseUser.delete();

                        firebaseAuth.signOut();

                    } else {
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                    }
                    booleanMutableLiveData.setValue(false);
//
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                booleanMutableLiveData.setValue(false);
            }
        });
    }

    public void loginCredential(AuthCredential authCredential, int age, String gender, LoginActivity loginActivity) {
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        DocumentReference documentReference = FirebaseFirestore.getInstance()
                .collection("Users")
                .document(firebaseAuth.getUid());

        viewModel = new ViewModelProvider(loginActivity).get(LoginViewModel.class);

        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    boolean isUserNew = task.getResult().getAdditionalUserInfo().isNewUser();

                    if (isUserNew) {

                        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(context);

                        String id = firebaseAuth.getUid();
                        String name = googleSignInAccount.getDisplayName();
                        String email = googleSignInAccount.getEmail();
                        String imageUrl = googleSignInAccount.getPhotoUrl().toString();
//                        String gender = googleSignInAccount.;
                        String about = "No About yet";

                        HashMap<String, Object> map = new HashMap<>();
                        map.put("id", firebaseAuth.getCurrentUser().getUid());
                        map.put("name", name);
                        map.put("gender", gender);
                        map.put("about", about);
                        map.put("isOnline", "yes");
                        map.put("age", String.valueOf(age));
                        map.put("email", email);
                        map.put("profilePictureUrl", imageUrl);
//                        map.put("displayImage1", "no image");
//                        map.put("displayImage2", "no image");
//                        map.put("displayImage3", "no image");
//                        User user = new User(id, name, "", gender, "No age", "No about yet", email, imageUrl, "No phone number yet", "yes", "no image", "no image", "no image");
//                        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                        documentReference.set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(context, CreateProfileActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    context.startActivity(intent);
                                    booleanMutableLiveData.setValue(false);
//                                    viewModel.accessUserLocationMutableLiveData.setValue(true);
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                booleanMutableLiveData.setValue(false);
                            }
                        });

                    } else {
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                        booleanMutableLiveData.setValue(false);
                    }

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                booleanMutableLiveData.setValue(false);
            }
        });
    }

//    public void loginCredentialFb(AuthCredential authCredential) {
//
//        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//
//                    boolean isUserNew = task.getResult().getAdditionalUserInfo().isNewUser();
//
//                    if (isUserNew) {
//
//                        FacebookAuthCredential googleSignInAccount = GoogleSignIn.getLastSignedInAccount(context);
//
//                        String id = firebaseAuth.getUid();
//                        String name = googleSignInAccount.getDisplayName();
//                        String email = googleSignInAccount.getEmail();
//                        String imageUrl = googleSignInAccount.getPhotoUrl().toString();
//                        String gender = "Male";
//                        String address = "not stated";
//                        String age = "no age entered";
//                        String about = "love living";
//
//                        HashMap<String, Object> map = new HashMap<>();
//                        map.put("id", firebaseAuth.getCurrentUser().getUid());
//                        map.put("name", name);
//                        map.put("address", address);
//                        map.put("gender", gender);
//                        map.put("age", age);
//                        map.put("about", about);
//                        map.put("isOnline", "no");
//                        map.put("email", email);
//                        map.put("profilePictureUrl", imageUrl);
//
//                        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
//                        databaseReference.child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if (task.isSuccessful()) {
//                                    Intent intent = new Intent(context, MainActivity.class);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                    context.startActivity(intent);
//                                    booleanMutableLiveData.setValue(false);
//                                }
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//                                booleanMutableLiveData.setValue(false);
//                            }
//                        });
//
//                    } else {
//                        Intent intent = new Intent(context, MainActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        context.startActivity(intent);
//                        booleanMutableLiveData.setValue(false);
//                    }
//
//
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//                booleanMutableLiveData.setValue(false);
//            }
//        });
//    }

    public void doSignInWithFb(AccessToken token, AuthCredential authCredential, int age, String gender, LoginActivity loginActivity) {

        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        viewModel = new ViewModelProvider(loginActivity).get(LoginViewModel.class);

        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    boolean isUserNew = task.getResult().getAdditionalUserInfo().isNewUser();

                    if (isUserNew) {

//                        FacebookAuthCredential googleSignInAccount = GoogleSignIn.getLastSignedInAccount(context);
//
//                        String id = firebaseAuth.getUid();
//                        String name = googleSignInAccount.getDisplayName();
//                        String email = googleSignInAccount.getEmail();
//                        String imageUrl = googleSignInAccount.getPhotoUrl().toString();
//                        String gender = "Male";
//                        String address = "not stated";
//                        String age = "no age entered";
//                        String about = "love living";
//
//                        HashMap<String, Object> map = new HashMap<>();
//                        map.put("id", firebaseAuth.getCurrentUser().getUid());
//                        map.put("name", name);
//                        map.put("address", address);
//                        map.put("gender", gender);
//                        map.put("age", age);
//                        map.put("about", about);
//                        map.put("isOnline", "no");
//                        map.put("email", email);
//                        map.put("profilePictureUrl", imageUrl);
//
//                        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
//                        databaseReference.child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if (task.isSuccessful()) {
//                                    Intent intent = new Intent(context, MainActivity.class);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                    context.startActivity(intent);
//                                    booleanMutableLiveData.setValue(false);
//                                }
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//                                booleanMutableLiveData.setValue(false);
//                            }
//                        });
                        GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                try {

//                                    private String id;
//                                    private String name;
//                                    private String location;
//                                    private String gender;
//                                    private String age;
//                                    private String about;
//                                    private String email;
//                                    private String profilePictureUrl;
//                                    private String phoneNumber;
//                                    private String isOnline;
//
//                                    private String displayImage1;
//                                    private String displayImage2;
//                                    private String displayImage3;
//
//                                    // for liking
//                                    private boolean isLiked;


                                    String name = object.getString("name");
                                    String email = object.getString("email");
//                                    String gender = object.getString("gender");
                                    String id = firebaseAuth.getUid();
                                    String profilePic = object.getString("picture");

                                    HashMap<String, Object> map = new HashMap<>();
                                    map.put("id", id);
                                    map.put("name", name);
                                    map.put("gender", gender);
                                    map.put("about", "No about yet");
                                    map.put("isOnline", "yes");
                                    map.put("email", email);
                                    map.put("age", String.valueOf(age));
                                    map.put("profilePictureUrl", profilePic);
                                    map.put("displayImage1", "no image");
                                    map.put("displayImage2", "no image");
                                    map.put("displayImage3", "no image");
                                    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                                    databaseReference.child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
//                                    Intent intent = new Intent(context, MainActivity.class);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                    context.startActivity(intent);
//                                    booleanMutableLiveData.setValue(false);
                                                viewModel.accessUserLocationMutableLiveData.setValue(true);
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            booleanMutableLiveData.setValue(false);
                                        }
                                    });
//                                    User user = new User(id,name,"",gender,"No age","No about yet",email,profilePic,"No phone number yet","yes","no image","no image","no image");

                                } catch (Exception e) {
                                    Toast.makeText(loginActivity, "" + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        request.executeAsync();
                    } else {
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                        booleanMutableLiveData.setValue(false);
                    }


                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                booleanMutableLiveData.setValue(false);
            }
        });

    }

    public void updateMyLocation(double longitude, double latitude) {

        String id = FirebaseAuth.getInstance().getUid();
        DocumentReference documentReference = FirebaseFirestore.getInstance()
                .collection("Users")
                .document(id);
        HashMap<String, Object> map = new HashMap<>();

        map.put("longitude", longitude);
        map.put("latitude", latitude);
//        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        documentReference.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                booleanMutableLiveData.setValue(false);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                booleanMutableLiveData.setValue(false);
            }
        });

    }

}