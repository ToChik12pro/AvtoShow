package com.example.autoshow.Supabase_Conection;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface SupabaseAPI {
    @POST("/rest/v1/users") // Имя таблицы
    Call<Void> insertData(
            @Header("Authorization") String apiKey,
            @Body Map<String, Object> data
    );
}