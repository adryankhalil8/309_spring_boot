package com.example.cardealership.service;

import com.example.cardealership.dto.CarRequest;
import com.example.cardealership.dto.CarResponse;
import java.util.List;

public interface CarService {
    List<CarResponse> getAllCars();
    List<CarResponse> searchCarsByMake(String make);
    CarResponse getCarById(Long id);
    CarResponse createCar(CarRequest request);
    CarResponse updateCar(Long id, CarRequest request);
    void deleteCar(Long id);
    CarResponse assignOwner(Long carId, Long ownerId);
}
