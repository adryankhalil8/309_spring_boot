package com.example.cardealership.controller;

import com.example.cardealership.dto.CarRequest;
import com.example.cardealership.dto.CarResponse;
import com.example.cardealership.service.CarService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public Page<CarResponse> getAllCars(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        return carService.getAllCars(page, size, sortBy, direction);
    }

    @GetMapping("/search")
    public List<CarResponse> search(@RequestParam String q) {
        return carService.searchCars(q);
    }

    @GetMapping("/filter")
    public List<CarResponse> filterCars(
            @RequestParam(required = false) String make,
            @RequestParam(required = false) String color,
            @RequestParam(required = false) Integer minYear,
            @RequestParam(required = false) Integer maxYear,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        return carService.filterCars(make, color, minYear, maxYear, minPrice, maxPrice);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarResponse> getCarById(@PathVariable Long id) {
        return ResponseEntity.ok(carService.getCarById(id));
    }

    @PostMapping
    public ResponseEntity<CarResponse> createCar(@Valid @RequestBody CarRequest request) {
        CarResponse response = carService.createCar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarResponse> updateCar(@PathVariable Long id, @Valid @RequestBody CarRequest request) {
        return ResponseEntity.ok(carService.updateCar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{carId}/owner/{ownerId}")
    public ResponseEntity<CarResponse> assignOwner(
            @PathVariable Long carId,
            @PathVariable Long ownerId) {
        CarResponse response = carService.assignOwner(carId, ownerId);
        return ResponseEntity.ok(response);
    }
}
