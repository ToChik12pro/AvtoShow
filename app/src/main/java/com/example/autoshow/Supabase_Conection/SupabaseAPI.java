package com.example.autoshow.Supabase_Conection;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

import java.util.List;
import java.util.Map;

public interface SupabaseAPI {

    // GET-запрос для получения данных из таблицы "Users"
    @GET("/rest/v1/Users")
    Call<List<Map<String, Object>>> getTableData(@Header("apikey") String apiKey);

    // POST-запрос для вставки данных в таблицу "Users"
    @POST("/rest/v1/Users")
    Call<Void> insertData(@Header("apikey") String apiKey, @Body Map<String, Object> data);
}