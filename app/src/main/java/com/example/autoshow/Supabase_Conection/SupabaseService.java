package com.example.autoshow.Supabase_Conection;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SupabaseService {
    private static final String BASE_URL = "https://rszhykplvuzblnknphqf.supabase.co"; // Укажите URL вашего Supabase
    private static final String API_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJzemh5a3BsdnV6Ymxua25waHFmIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTcxOTA4MTUzOCwiZXhwIjoyMDM0NjU3NTM4fQ.zKXkdN6314IpCWY-3n7zRKOa3GcsH4P29kICpmVfz60"; // Замена на нужный API-ключ
    private static final String API_KEY_SECURE = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJzemh5a3BsdnV6Ymxua25waHFmIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MTkwODE1MzgsImV4cCI6MjAzNDY1NzUzOH0.j70ogRbWt510o6jNIDwHl5lz4eMVkjWY5nXQemexbT8"; // Замена на нужный API-ключ с более строгим контролем доступа

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static String getApiKey() {
        return API_KEY_SECURE; // Используйте более строгий API-ключ для операций с доступом
    }
}