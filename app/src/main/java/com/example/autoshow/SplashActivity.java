package com.example.autoshow;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.autoshow.MainActivity2;
import com.example.autoshow.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        // Найдите элемент для анимации, например, логотип
        ImageView logo = findViewById(R.id.logo);
        TextView titleText = findViewById(R.id.titleText);

        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logo.startAnimation(fadeIn);
        titleText.startAnimation(fadeIn);


        // Таймер для перехода на главный экран
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity2.class));
            finish();
        }, 2000); // Время показа экрана, например, 2 секунды
    }
}
