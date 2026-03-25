package com.example.cardealership.controller;

import com.example.cardealership.entity.Car;
import com.example.cardealership.repository.CarRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    private final CarRepository carRepository;

    public CarController(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    // GET all cars
    @GetMapping
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    // GET car by ID
    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        return carRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST create car
    @PostMapping
    public ResponseEntity<Car> createCar(@RequestBody Car car) {
        Car saved = carRepository.save(car);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
    // PUT update car
    @PutMapping("/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable Long id, @RequestBody Car carDetails) {
        return carRepository.findById(id)
                .map(car -> {
                    car.setMake(carDetails.getMake());
                    car.setModel(carDetails.getModel());
                    car.setYear(carDetails.getYear());
                    car.setColor(carDetails.getColor());
                    car.setPrice(carDetails.getPrice());
                    return ResponseEntity.ok(carRepository.save(car));
                })
                .orElse(ResponseEntity.notFound().build());
    }
    // DELETE car
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        if (carRepository.existsById(id)) {
            carRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    @GetMapping("/search")
    public List<Car> searchByMake(@RequestParam String make) {
        return carRepository.findByMake(make);
    }
}