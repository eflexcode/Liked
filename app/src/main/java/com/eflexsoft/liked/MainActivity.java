package com.eflexsoft.liked;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
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
import com.eflexsoft.liked.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_main);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

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

        NavController navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupWithNavController(binding.bnv, navController);

    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

    }
}