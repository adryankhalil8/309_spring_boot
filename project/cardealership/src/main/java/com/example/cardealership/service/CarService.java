package com.example.cardealership.service;

import com.example.cardealership.entity.Car;
import java.util.List;

public interface CarService {
    List<Car> getAllCars();
    Car getCarById(Long id);
    Car createCar(Car car);
    Car updateCar(Long id, Car carDetails);
    void deleteCar(Long id);
}