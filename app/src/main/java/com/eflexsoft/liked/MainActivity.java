package com.eflexsoft.liked;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.eflexsoft.liked.fragment.DiscoverFragment;
import com.eflexsoft.liked.fragment.MessageFragment;
import com.eflexsoft.liked.fragment.ProfileFragment;
import com.eflexsoft.liked.fragment.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;


    FrameLayout frameLayout;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        frameLayout = findViewById(R.id.frag_frame);
        bottomNavigationView = findViewById(R.id.bnv);

        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelected);



        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_frame, new DiscoverFragment()).commit();

        }

    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelected = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.discover:

                    fragment = new DiscoverFragment();

                    break;
                case R.id.search:
                    fragment = new SearchFragment();

                    break;
                case R.id.message:

                    fragment = new MessageFragment();
//                    FirebaseAuth.getInstance().signOut();
                    break;
                case R.id.profile:

                    fragment = new ProfileFragment();

                    break;

            }

            getSupportFragmentManager().beginTransaction().replace(R.id.frag_frame, fragment).commit();

            return true;
        }
    };
    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

    }
}