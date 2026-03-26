package com.example.cardealership.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class CarRequest {

    @NotBlank(message = "Make is required")
    @Size(min = 2, max = 50, message = "Make must be between 2 and 50 characters")
    private String make;

    @NotBlank(message = "Model is required")
    @Size(min = 1, max = 50, message = "Model must be between 1 and 50 characters")
    private String model;

    @Min(value = 1886, message = "Year must be 1886 or later")
    private int year;

    @NotBlank(message = "Color is required")
    private String color;

    @Positive(message = "Price must be greater than zero")
    private double price;

    public CarRequest() {}

    public CarRequest(String make, String model, int year, String color, double price) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.color = color;
        this.price = price;
    }

    public String getMake() { return make; }
    public void setMake(String make) { this.make = make; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    @AssertTrue(message = "Year cannot be more than 1 year in the future")
    public boolean isYearWithinAllowedRange() {
        if (year == 0) {
            return true;
        }
        return year <= java.time.Year.now().getValue() + 1;
    }
}
