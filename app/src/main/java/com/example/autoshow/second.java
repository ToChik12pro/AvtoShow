package com.example.autoshow;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class second extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_second);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button backButton = findViewById(R.id.backButton);
        Button nextButton = findViewById(R.id.nextButton);

        backButton.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_scale));
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_scale_reverse));
            }
            return false;
        });

        nextButton.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_scale));
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.button_scale_reverse));
            }
            return false;
        });

        backButton.setOnClickListener(view -> {
            finish(); // Закрыть текущую активность
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right); // Анимации перехода
        });

        nextButton.setOnClickListener(view -> {
            Intent intent = new Intent(second.this, Thrid.class); // Замените на ваш класс активности
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left); // Анимации перехода
        });
    }
}