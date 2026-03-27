package com.example.cardealership.service;

import com.example.cardealership.dto.CarRequest;
import com.example.cardealership.dto.CarResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CarService {
    Page<CarResponse> getAllCars(int page, int size, String sortBy, String direction);
    List<CarResponse> searchCars(String keyword);
    List<CarResponse> filterCars(String make, String color, Integer minYear, Integer maxYear,
                                 Double minPrice, Double maxPrice);
    CarResponse getCarById(Long id);
    CarResponse createCar(CarRequest request);
    CarResponse updateCar(Long id, CarRequest request);
    void deleteCar(Long id);
    CarResponse assignOwner(Long carId, Long ownerId);
}
