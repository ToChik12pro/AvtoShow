package com.example.autoshow;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        TextView titleTextView = findViewById(R.id.titleTextView);
        TextView descriptionTextView = findViewById(R.id.descriptionTextView);
        ImageView imageView1 = findViewById(R.id.imageView1);
        ImageView imageView2 = findViewById(R.id.imageView2);
        TextView infoTextView = findViewById(R.id.infoTextView);
        Button exitButton = findViewById(R.id.exitButton);
        Button nextButton = findViewById(R.id.nextButton);

        // Анимация появления
        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in_from_bottom);
        titleTextView.startAnimation(fadeInAnimation);
        descriptionTextView.startAnimation(fadeInAnimation);
        imageView1.startAnimation(fadeInAnimation);
        imageView2.startAnimation(fadeInAnimation);
        infoTextView.startAnimation(fadeInAnimation);

        // Анимация нажатия на кнопку
        Animation buttonPressAnimation = AnimationUtils.loadAnimation(this, R.anim.button_press);

        exitButton.setOnClickListener(v -> {
            // Плавное закрытие приложения
            finish(); // Закрывает текущее Activity
            System.exit(0); // Завершает приложение
        });

        nextButton.setOnClickListener(v -> {
            // Плавная анимация нажатия
            v.startAnimation(buttonPressAnimation);

            Intent intent = new Intent(MainActivity2.this, second.class);
            startActivity(intent);
            // Опционально, можно добавить анимацию перехода
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });
    }
}