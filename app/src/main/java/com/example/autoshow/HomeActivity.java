package com.example.autoshow;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.autoshow.ui.home.HomeFragment;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);

        // Добавляем фрагмент HomeFragment в nav_host_fragment_activity_home2
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment_activity_home2, new HomeFragment())
                    .commit();
        }
    }
}
