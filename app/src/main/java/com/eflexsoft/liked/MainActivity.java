package com.eflexsoft.liked;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.eflexsoft.liked.databinding.ActivityMainBinding;
import com.eflexsoft.liked.fragment.DiscoverFragment;
import com.eflexsoft.liked.fragment.MessageFragment;
import com.eflexsoft.liked.fragment.ProfileFragment;
import com.eflexsoft.liked.fragment.SearchFragment;
import com.eflexsoft.liked.model.Ads;
import com.eflexsoft.liked.model.User;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Fragment discoverFragment;
    Fragment message;
    Fragment profile;

    FragmentManager fragmentManager;

    Fragment active;

    AdRequest adRequest;

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_main);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        adRequest = new AdRequest.Builder()
                .build();

        mInterstitialAd = new InterstitialAd(this);


        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        DocumentReference documentReference = firebaseFirestore.collection("Ads")
                .document("TimeAds");

        final String[] unitId = new String[1];

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                try {

                    Ads ads = value.toObject(Ads.class);

                    unitId[0] = ads.getAdUnit();


                    mInterstitialAd.setAdUnitId(unitId[0]);
                    mInterstitialAd.loadAd(adRequest);
                    Handler handler = new Handler();

                    Timer timer = new Timer();
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
//                                Toast.makeText(MainActivity.this, unitId[0], Toast.LENGTH_SHORT).show();
                                    if (mInterstitialAd.isLoaded()) {
                                        mInterstitialAd.show();
                                    } else {
//                                    Toast.makeText(MainActivity.this, "Unable to show ads", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }

                    }, 6000000, 2100000);
                } catch (Exception e) {

                }


            }
        });


        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                mInterstitialAd.loadAd(adRequest);
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                mInterstitialAd.loadAd(adRequest);
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                mInterstitialAd.loadAd(adRequest);
            }
        });


        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            discoverFragment = new DiscoverFragment();
            fragmentManager.beginTransaction().add(R.id.nav_host_fragment_container, discoverFragment).commit();
            active = discoverFragment;

        }
        binding.bnv.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users")
//                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (!snapshot.exists()) {
//                    startActivity(new Intent(MainActivity.this, EditProfileActivity.class));
//                    finish();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });

//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.frag_frame, new DiscoverFragment()).commit();
//
//        }

//        NavController navController = Navigation.findNavController(this, R.id.fragment);
//        NavigationUI.setupWithNavController(binding.bnv, navController);

//        Handler handler = new Handler();

//        Timer timer = new Timer();
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (mInterstitialAd.isLoaded()) {
//                            mInterstitialAd.show();
//                        }
//                    }
//                });
//
//            }
//        }, 600000, 200000);

//        Timer timer = new Timer();
//        timer.scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(MainActivity.this, unitId[0], Toast.LENGTH_SHORT).show();
//                        if (mInterstitialAd.isLoaded()) {
//                            mInterstitialAd.show();
//                        }else {
//                            Toast.makeText(MainActivity.this, "Unable to show ads", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//
//            }
//        }, 20000, 50000);

    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            try {
                switch (item.getItemId()) {

                    case R.id.discoverFragment:

                        if (discoverFragment == null) {
                            discoverFragment = new DiscoverFragment();
                            fragmentManager.beginTransaction().add(R.id.nav_host_fragment_container, discoverFragment).commit();
                        } else {
                            fragmentManager.beginTransaction().hide(active).show(discoverFragment).commit();
                        }
                        active = discoverFragment;

                        break;
                    case R.id.messageFragment:

                        if (message == null) {
                            message = new MessageFragment();
                            fragmentManager.beginTransaction().add(R.id.nav_host_fragment_container, message).commit();
                        } else {
                            fragmentManager.beginTransaction().hide(active).show(message).commit();
                        }
                        active = message;

                        break;
                    case R.id.profileFragment:

                        if (profile == null) {
                            profile = new ProfileFragment();
                            fragmentManager.beginTransaction().add(R.id.nav_host_fragment_container, profile).commit();
                        } else {
                            fragmentManager.beginTransaction().hide(active).show(profile).commit();
                        }
                        active = profile;
                        break;

                }
            } catch (Exception e) {

            }


            return true;
        }
    };
}