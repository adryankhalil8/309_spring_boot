package com.example.cardealership.service;

import com.example.cardealership.entity.Car;
import com.example.cardealership.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;

    public CarServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    @Override
    public Car getCarById(Long id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car not found with id: " + id));
    }

    @Override
    public Car createCar(Car car) {
        // Business rule: price must be positive
        if (car.getPrice() <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }

        // Business rule: year must be valid
        int currentYear = java.time.Year.now().getValue();
        if (car.getYear() < 1886 || car.getYear() > currentYear + 1) {
            throw new IllegalArgumentException("Year must be between 1886 and " + (currentYear + 1));
        }

        return carRepository.save(car);
    }

    @Override
    public Car updateCar(Long id, Car carDetails) {
        Car car = getCarById(id);
        car.setMake(carDetails.getMake());
        car.setModel(carDetails.getModel());
        car.setYear(carDetails.getYear());
        car.setColor(carDetails.getColor());
        car.setPrice(carDetails.getPrice());
        return carRepository.save(car);
    }

    @Override
    public void deleteCar(Long id) {
        Car car = getCarById(id);
        carRepository.delete(car);
    }
}
