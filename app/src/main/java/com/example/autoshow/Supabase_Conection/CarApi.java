package com.example.autoshow.Supabase_Conection;

import com.example.autoshow.Car;
import com.example.autoshow.CarImage;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CarApi {
    @GET("cars")
    Call<List<Car>> getCars();

    @GET("carimages")
    Call<List<CarImage>> getCarImages();
}