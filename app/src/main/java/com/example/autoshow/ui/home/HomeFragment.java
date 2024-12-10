package com.example.autoshow.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autoshow.Car;
import com.example.autoshow.CarAdapter;
import com.example.autoshow.R;
import com.example.autoshow.Supabase_Conection.CarApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private CarAdapter carAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Retrofit setup
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://your-api-url.com/") // URL API
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CarApi carApi = retrofit.create(CarApi.class);

        // Получение списка машин
        carApi.getCars().enqueue(new Callback<List<Car>>() {
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Car> cars = response.body();
                    carAdapter = new CarAdapter(getContext(), cars);
                    recyclerView.setAdapter(carAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {
                // Обработка ошибки
            }
        });

        return rootView;
    }
}