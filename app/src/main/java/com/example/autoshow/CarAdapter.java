package com.example.autoshow;

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

    public CarAdapter(List<Car> carList) {
        this.carList = carList;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.car_item, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        Car car = carList.get(position);
        holder.tvModel.setText("Модель: " + car.getModel());
        holder.tvYear.setText("Год: " + car.getYear());
        holder.tvPrice.setText("Цена: $" + car.getPrice());
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public void updateCars(List<Car> cars) {
        this.carList = cars;
        notifyDataSetChanged();
    }

    public static class CarViewHolder extends RecyclerView.ViewHolder {
        TextView tvModel, tvYear, tvPrice;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            tvModel = itemView.findViewById(R.id.tv_model);
            tvYear = itemView.findViewById(R.id.tv_year);
            tvPrice = itemView.findViewById(R.id.tv_price);
        }
    }
}
