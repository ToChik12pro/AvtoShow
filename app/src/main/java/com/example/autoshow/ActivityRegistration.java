package com.example.autoshow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import at.favre.lib.crypto.bcrypt.BCrypt;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ActivityRegistration extends AppCompatActivity {

    private EditText etUsername, etPassword, etEmail, etPhone, etAddress;
    private CheckBox cbPrivacyPolicy;
    private Button btnRegister;
    private ProgressDialog progressDialog;

    // URL и API-ключ Supabase
    private static final String SUPABASE_URL = "https://rszhykplvuzblnknphqf.supabase.co";
    private static final String SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJzemh5a3BsdnV6Ymxua25waHFmIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MTkwODE1MzgsImV4cCI6MjAzNDY1NzUzOH0.j70ogRbWt510o6jNIDwHl5lz4eMVkjWY5nXQemexbT8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Инициализация UI элементов
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_phone);
        etAddress = findViewById(R.id.et_address);
        cbPrivacyPolicy = findViewById(R.id.cb_privacy_policy);
        btnRegister = findViewById(R.id.btn_register);
        TextView tvLoginLink = findViewById(R.id.tv_login_link);
        tvLoginLink.setOnClickListener(v -> {
            // Переход на экран авторизации
            Intent intent = new Intent(ActivityRegistration.this, LoginActivity.class);
            startActivity(intent);
        });

        // Прогресс-диалог для загрузки
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Регистрация, пожалуйста, подождите...");
        progressDialog.setCancelable(false);

        // Активировать кнопку регистрации только после принятия политики конфиденциальности
        cbPrivacyPolicy.setOnCheckedChangeListener((buttonView, isChecked) -> btnRegister.setEnabled(isChecked));

        // Обработка нажатия на кнопку регистрации
        btnRegister.setOnClickListener(v -> registerUser());
    }


    private void registerUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        // Валидация полей
        if (username.isEmpty()) {
            etUsername.setError("Имя пользователя обязательно");
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Пароль обязателен");
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Пароль должен быть не менее 6 символов");
            return;
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Введите корректный email");
            return;
        }

        if (!phone.isEmpty() && !phone.matches("\\d{10,15}")) {
            etPhone.setError("Введите корректный номер телефона");
            return;
        }

        // Хеширование пароля
        String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());

        // Подготовка данных для регистрации
        Map<String, Object> data = new HashMap<>();
        data.put("Username", username);
        data.put("Password", hashedPassword);
        data.put("Email", email);
        data.put("Phone", phone);
        data.put("Address", address);

        // Преобразование данных в JSON
        Gson gson = new Gson();
        String jsonBody = gson.toJson(data);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json"));

        // Показать прогресс-диалог
        progressDialog.show();

        // Создание запроса
        Request request = new Request.Builder()
                .url(SUPABASE_URL + "/rest/v1/Users")
                .addHeader("apikey", SUPABASE_KEY)
                .addHeader("Authorization", "Bearer " + SUPABASE_KEY)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Registration", "Ошибка подключения: " + e.getMessage());
                runOnUiThread(() -> {
                    progressDialog.dismiss();
                    showErrorDialog("Ошибка подключения", "Проверьте соединение с интернетом и попробуйте снова.");
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                progressDialog.dismiss(); // Скрыть прогресс-диалог
                if (response.isSuccessful()) {
                    Log.d("Registration", "Регистрация успешна!");
                    runOnUiThread(() -> {
                        showSuccessDialog("Успех", "Регистрация прошла успешно!", () -> {
                            Intent intent = new Intent(ActivityRegistration.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        });
                        clearFields();
                    });
                } else {
                    String errorBody = response.body() != null ? response.body().string() : "Неизвестная ошибка";
                    Log.e("Registration", "Ошибка: " + response.code() + ", " + errorBody);
                    runOnUiThread(() -> {
                        String userFriendlyMessage = getErrorMessage(response.code(), errorBody);
                        showErrorDialog("Ошибка регистрации", userFriendlyMessage);
                    });
                }
            }
        });
    }

    // Очистка всех полей после успешной регистрации
    private void clearFields() {
        etUsername.setText("");
        etPassword.setText("");
        etEmail.setText("");
        etPhone.setText("");
        etAddress.setText("");
        cbPrivacyPolicy.setChecked(false);
    }

    // Показ диалога ошибки
    private void showErrorDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    // Показ диалога успеха с переходом
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

    // Генерация пользовательского сообщения об ошибке
    private String getErrorMessage(int statusCode, String errorBody) {
        if (statusCode == 409 && errorBody.contains("Users_Username_key")) {
            return "Имя пользователя уже занято. Пожалуйста, выберите другое имя.";
        }
        if (statusCode == 409 && errorBody.contains("Users_Email_key")) {
            return "Этот email уже зарегистрирован. Используйте другой email.";
        }
        switch (statusCode) {
            case 400:
                return "Некорректные данные. Пожалуйста, проверьте введённую информацию.";
            case 401:
                return "Ошибка авторизации. Попробуйте снова.";
            case 403:
                return "У вас нет доступа для выполнения данной операции.";
            case 404:
                return "Сервер не найден. Проверьте адрес и подключение.";
            case 500:
                return "На сервере произошла ошибка. Пожалуйста, попробуйте позже.";
            default:
                return "Произошла неизвестная ошибка (" + statusCode + "). Попробуйте снова.";
        }
    }
}
