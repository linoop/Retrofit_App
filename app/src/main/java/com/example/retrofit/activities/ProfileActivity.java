package com.example.retrofit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.retrofit.R;
import com.example.retrofit.fragments.HomeFragment;
import com.example.retrofit.fragments.SettingsFragment;
import com.example.retrofit.fragments.UserFragment;
import com.example.retrofit.storage.SharedPrefManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


/*    @BindView(R.id.textView)
    TextView textView;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);



        /*User user = SharedPrefManager.getInstance(this).getUser();
        textView.setText("Welcome back " + user.getName());*/


        BottomNavigationView navigationView = findViewById(R.id.bottom_nav);
        navigationView.setOnNavigationItemSelectedListener(this);

        dispalyFragment(new HomeFragment());


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }


    private void dispalyFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.relativeLayout, fragment)
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch(item.getItemId()){
            case R.id.menu_home:
                fragment = new HomeFragment();
                break;
            case R.id.menu_users:
                fragment = new UserFragment();
                break;
            case R.id.menu_settings:
                fragment = new SettingsFragment();
                break;

        }
        if(fragment!=null){
            dispalyFragment(fragment);
        }

        return false;
    }




}
