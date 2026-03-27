package com.example.cardealership.service;

import com.example.cardealership.dto.CarRequest;
import com.example.cardealership.dto.CarResponse;
import com.example.cardealership.entity.Car;
import com.example.cardealership.entity.Owner;
import com.example.cardealership.exception.ResourceNotFoundException;
import com.example.cardealership.mapper.CarMapper;
import com.example.cardealership.repository.CarRepository;
import com.example.cardealership.repository.OwnerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final CarMapper carMapper;
    private final OwnerRepository ownerRepository;

    public CarServiceImpl(CarRepository carRepository, CarMapper carMapper, OwnerRepository ownerRepository) {
        this.carRepository = carRepository;
        this.carMapper = carMapper;
        this.ownerRepository = ownerRepository;
    }

    @Override
    public Page<CarResponse> getAllCars(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return carRepository.findAll(pageable)
                .map(carMapper::toResponse);
    }

    @Override
    public List<CarResponse> searchCars(String keyword) {
        return carRepository.searchByKeyword(keyword)
                .stream()
                .map(carMapper::toResponse)
                .toList();
    }

    @Override
    public List<CarResponse> filterCars(String make, String color, Integer minYear, Integer maxYear,
                                        Double minPrice, Double maxPrice) {
        return carRepository.filterCars(make, color, minYear, maxYear, minPrice, maxPrice)
                .stream()
                .map(carMapper::toResponse)
                .toList();
    }

    @Override
    public CarResponse getCarById(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car", id));
        return carMapper.toResponse(car);
    }


    @Override
    public CarResponse createCar(CarRequest request) {
        Car car = carMapper.toEntity(request);
        Car saved = carRepository.save(car);
        return carMapper.toResponse(saved);
    }

    @Override
    public CarResponse updateCar(Long id, CarRequest request) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car", id));
        carMapper.updateEntity(car, request);
        return carMapper.toResponse(carRepository.save(car));
    }

    @Override
    public void deleteCar(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car", id));
        carRepository.delete(car);
    }

    @Override
    public CarResponse assignOwner(Long carId, Long ownerId) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car", carId));
        Owner owner = ownerRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner", ownerId));
        car.setOwner(owner);
        Car saved = carRepository.save(car);
        return carMapper.toResponse(saved);
    }
}
