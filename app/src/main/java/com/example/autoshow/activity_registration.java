package com.example.autoshow;

import android.os.Bundle;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.autoshow.Supabase_Conection.SupabaseAPI;
import com.example.autoshow.Supabase_Conection.SupabaseService;

import java.util.HashMap;
import java.util.Map;

import at.favre.lib.crypto.bcrypt.BCrypt;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class activity_registration extends AppCompatActivity {

    private EditText etUsername, etPassword, etEmail, etPhone, etAddress;
    private CheckBox cbPrivacyPolicy;
    private Button btnRegister;

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
        TextView tvPrivacyPolicy = findViewById(R.id.tv_privacy_policy);

        // Активировать кнопку регистрации только после принятия политики конфиденциальности
        cbPrivacyPolicy.setOnCheckedChangeListener((buttonView, isChecked) -> {
            btnRegister.setEnabled(isChecked);
        });

        // Обработка нажатия на кнопку регистрации
        btnRegister.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        // Проверка на заполненность всех полей
        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Log.w("Registration", "Некоторые поля не заполнены. Регистрация невозможна.");
            Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        // Хэширование пароля с помощью BCrypt
        String hashedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        Log.d("Registration", "Пароль успешно хэширован: " + hashedPassword);

        // Подготовка данных для отправки в Supabase
        Map<String, Object> userData = new HashMap<>();
        userData.put("username", username);
        userData.put("password", hashedPassword);
        userData.put("email", email);
        userData.put("phone", phone);
        userData.put("address", address);

        Log.d("Registration", "Данные пользователя подготовлены: " + userData);

        // Инициализация API и отправка запроса
        SupabaseAPI api = SupabaseService.getClient().create(SupabaseAPI.class);
        Call<Void> call = api.insertData(SupabaseService.getApiKey(), userData);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.i("Registration", "Пользователь успешно зарегистрирован в Supabase!");
                    Toast.makeText(activity_registration.this, "Пользователь успешно зарегистрирован в Supabase!", Toast.LENGTH_SHORT).show();
                    clearFields();
                } else {
                    Log.e("Registration", "Ошибка при регистрации: код " + response.code() + ", сообщение: " + response.message());
                    Toast.makeText(activity_registration.this, "Ошибка при регистрации: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Registration", "Ошибка подключения к Supabase: " + t.getMessage(), t);
                Toast.makeText(activity_registration.this, "Ошибка подключения: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Log.d("Registration", "Запрос на регистрацию отправлен.");
    }

    private void clearFields() {
        etUsername.setText("");
        etPassword.setText("");
        etEmail.setText("");
        etPhone.setText("");
        etAddress.setText("");
        cbPrivacyPolicy.setChecked(false);
    }
}