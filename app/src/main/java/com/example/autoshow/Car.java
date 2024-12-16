package com.example.autoshow;

public class Car {
    private int CarId;
    private String Model;
    private int Year;
    private String Vin;
    private double Price;
    private boolean Available;
    private String Characteristics;
    private int Category;

    // Геттеры
    public int getCarId() { return CarId; }
    public String getModel() { return Model; }
    public int getYear() { return Year; }
    public String getVin() { return Vin; }
    public double getPrice() { return Price; }
    public boolean isAvailable() { return Available; }
    public String getCharacteristics() { return Characteristics; }
    public int getCategory() { return Category; }
}