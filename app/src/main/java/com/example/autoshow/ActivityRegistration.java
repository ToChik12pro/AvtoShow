package com.example.autoshow;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

        // Проверка на пустые поля
        if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Заполните все обязательные поля", Toast.LENGTH_SHORT).show();
            return;
        }

        // Подготовка данных для регистрации
        Map<String, Object> data = new HashMap<>();
        data.put("Username", username);  // соответствующий столбец 'Username' в таблице
        data.put("Password", password);  // соответствующий столбец 'Password' в таблице
        data.put("Email", email);        // соответствующий столбец 'Email' в таблице
        data.put("Phone", phone);        // соответствующий столбец 'Phone' в таблице (если нужно)
        data.put("Address", address);    // соответствующий столбец 'Address' в таблице (если нужно)

        // Преобразование данных в JSON
        Gson gson = new Gson();
        String jsonBody = gson.toJson(data);

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json"));

        // Создание запроса
        Request request = new Request.Builder()
                .url(SUPABASE_URL + "/rest/v1/Users") // URL таблицы
                .addHeader("apikey", SUPABASE_KEY) // API-ключ
                .addHeader("Authorization", "Bearer " + SUPABASE_KEY) // Bearer токен
                .addHeader("Content-Type", "application/json") // Тип данных
                .post(body) // POST запрос
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Registration", "Ошибка: " + e.getMessage());
                runOnUiThread(() -> Toast.makeText(ActivityRegistration.this, "Ошибка подключения", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.d("Registration", "Успешно!");
                    runOnUiThread(() -> {
                        Toast.makeText(ActivityRegistration.this, "Регистрация прошла успешно", Toast.LENGTH_SHORT).show();
                        clearFields();  // Очищаем поля после успешной регистрации
                    });
                } else {
                    String errorBody = response.body() != null ? response.body().string() : "Неизвестная ошибка";
                    Log.e("Registration", "Ошибка: " + response.code() + ", " + errorBody);
                    runOnUiThread(() -> Toast.makeText(ActivityRegistration.this, "Ошибка: " + response.message() + "\n" + errorBody, Toast.LENGTH_SHORT).show());
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
}