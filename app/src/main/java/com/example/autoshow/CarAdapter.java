package com.example.autoshow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {
    private List<Car> carList;
    private Context context;

    public CarAdapter(Context context, List<Car> carList) {
        this.context = context;
        this.carList = carList;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_car, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        Car car = carList.get(position);

        // Загружаем изображение из URL с помощью Glide
        Glide.with(context)
                .load(car.getImageUrl()) // URL изображения из базы данных
                .placeholder(R.drawable.ic_placeholder_car) // Плейсхолдер
                .into(holder.carImageView);

        // Устанавливаем другие данные, например, модель автомобиля
        holder.carModel.setText(car.getModel());
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public static class CarViewHolder extends RecyclerView.ViewHolder {
        ImageView carImageView;
        TextView carModel;

        public CarViewHolder(View itemView) {
            super(itemView);
            carImageView = itemView.findViewById(R.id.car_image);
            carModel = itemView.findViewById(R.id.car_model);
        }
    }
}