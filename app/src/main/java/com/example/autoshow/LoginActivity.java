package com.example.autoshow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.IOException;

import at.favre.lib.crypto.bcrypt.BCrypt;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private ProgressDialog progressDialog;

    // URL и API-ключ Supabase
    private static final String SUPABASE_URL = "https://rszhykplvuzblnknphqf.supabase.co";
    private static final String SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJzemh5a3BsdnV6Ymxua25waHFmIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MTkwODE1MzgsImV4cCI6MjAzNDY1NzUzOH0.j70ogRbWt510o6jNIDwHl5lz4eMVkjWY5nXQemexbT8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Инициализация UI элементов
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        TextView tvRegisterLink = findViewById(R.id.tv_register_link);

        tvRegisterLink.setOnClickListener(v -> {
            // Переход на экран регистрации
            Intent intent = new Intent(LoginActivity.this, ActivityRegistration.class);
            startActivity(intent);
        });

        // Прогресс-диалог для загрузки
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Авторизация, пожалуйста, подождите...");
        progressDialog.setCancelable(false);

        // Обработка нажатия на кнопку авторизации
        btnLogin.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // Валидация полей
        if (username.isEmpty()) {
            etUsername.setError("Имя пользователя обязательно");
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Пароль обязателен");
            return;
        }

        // Показать прогресс-диалог
        progressDialog.show();

        OkHttpClient client = new OkHttpClient();

        // GET-запрос для получения пользователя по имени
        Request request = new Request.Builder()
                .url(SUPABASE_URL + "/rest/v1/Users?Username=eq." + username) // Фильтрация по Username
                .addHeader("apikey", SUPABASE_KEY)
                .addHeader("Authorization", "Bearer " + SUPABASE_KEY)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Login", "Ошибка подключения: " + e.getMessage());
                runOnUiThread(() -> {
                    progressDialog.dismiss();
                    showErrorDialog("Ошибка подключения", "Проверьте соединение с интернетом и попробуйте снова.");
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                progressDialog.dismiss();

                if (response.isSuccessful()) {
                    String responseBody = response.body().string();

                    // Логируем ответ для отладки
                    Log.d("Login", "Ответ: " + responseBody);

                    // Проверяем, получены ли данные пользователя
                    if (!responseBody.equals("[]")) {
                        // Используем GSON для десериализации ответа
                        Gson gson = new Gson();
                        User[] users = gson.fromJson(responseBody, User[].class);
                        User user = users[0]; // Предполагаем, что получен только один пользователь

                        // Сравнение пароля с хешем
                        if (verifyPassword(password, user.Password)) {
                            runOnUiThread(() -> {
                                showSuccessDialog("Успех", "Авторизация прошла успешно!", () -> {
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                });
                            });
                        } else {
                            runOnUiThread(() -> showErrorDialog("Ошибка", "Неверное имя пользователя или пароль."));
                        }
                    } else {
                        runOnUiThread(() -> showErrorDialog("Ошибка", "Пользователь не найден."));
                    }
                } else {
                    runOnUiThread(() -> showErrorDialog("Ошибка", "Не удалось получить данные пользователя."));
                }
            }
        });
    }

    // Проверка пароля с использованием Bcrypt
    private boolean verifyPassword(String plainPassword, String hashedPassword) {
        return BCrypt.verifyer().verify(plainPassword.toCharArray(), hashedPassword).verified;
    }

    // Показ диалога ошибки
    private void showErrorDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    // Показ диалога успеха
    private void showSuccessDialog(String title, String message, Runnable onPositiveClick) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.dismiss();
                    onPositiveClick.run();
                })
                .show();
    }

    // Модель User для десериализации
    private static class User {
        private String Username;
        private String Password; // Хешированный пароль из базы данных
    }
}

