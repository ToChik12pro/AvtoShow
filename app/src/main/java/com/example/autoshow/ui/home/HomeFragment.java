package com.example.autoshow.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autoshow.Car;
import com.example.autoshow.CarAdapter;
import com.example.autoshow.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private CarAdapter carAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Инициализация RecyclerView
        recyclerView = root.findViewById(R.id.recycler_view);
        carAdapter = new CarAdapter(new ArrayList<>());
        recyclerView.setAdapter(carAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fetchCars(); // Получение данных с Supabase
        return root;
    }

    private void fetchCars() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://rszhykplvuzblnknphqf.supabase.co/rest/v1/Cars")
                .addHeader("apikey", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InJzemh5a3BsdnV6Ymxua25waHFmIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTcxOTA4MTUzOCwiZXhwIjoyMDM0NjU3NTM4fQ.zKXkdN6314IpCWY-3n7zRKOa3GcsH4P29kICpmVfz60")
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("HomeFragment", "Ошибка: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String responseBody = response.body().string();

                    Gson gson = new Gson();
                    Car[] cars = gson.fromJson(responseBody, Car[].class);

                    if (getActivity() != null) {
                        getActivity().runOnUiThread(() -> carAdapter.updateCars(Arrays.asList(cars)));
                    }
                }
            }
        });
    }
}
