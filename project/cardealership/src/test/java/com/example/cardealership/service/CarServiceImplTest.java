package com.example.cardealership.service;

import com.example.cardealership.dto.CarRequest;
import com.example.cardealership.dto.CarResponse;
import com.example.cardealership.entity.Car;
import com.example.cardealership.mapper.CarMapper;
import com.example.cardealership.repository.CarRepository;
import com.example.cardealership.repository.OwnerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    @Mock
    private CarRepository carRepository;

    @Mock
    private OwnerRepository ownerRepository;

    private CarServiceImpl carService;

    private Car sampleCar;
    private CarRequest sampleRequest;

    @BeforeEach
    void setUp() {
        carService = new CarServiceImpl(carRepository, new CarMapper(), ownerRepository);
        sampleCar = new Car("Toyota", "Camry", 2023, "Silver", 28000);
        sampleCar.setId(1L);
        sampleRequest = new CarRequest("Toyota", "Camry", 2023, "Silver", 28000);
    }

    @Test
    void getAllCars_returnsList() {
        List<Car> cars = Arrays.asList(sampleCar, new Car("Honda", "Civic", 2022, "Blue", 24000));
        when(carRepository.findAll()).thenReturn(cars);

        List<CarResponse> result = carService.getAllCars();

        assertEquals(2, result.size());
        assertEquals("Toyota", result.get(0).getMake());
        verify(carRepository, times(1)).findAll();
    }

    @Test
    void searchCarsByMake_returnsMatches() {
        when(carRepository.findByMake("Toyota")).thenReturn(List.of(sampleCar));

        List<CarResponse> result = carService.searchCarsByMake("Toyota");

        assertEquals(1, result.size());
        assertEquals("Toyota", result.get(0).getMake());
        verify(carRepository).findByMake("Toyota");
    }

    @Test
    void getCarById_found() {
        when(carRepository.findById(1L)).thenReturn(Optional.of(sampleCar));

        CarResponse result = carService.getCarById(1L);

        assertEquals("Toyota", result.getMake());
        assertEquals("Camry", result.getModel());
    }

    @Test
    void getCarById_notFound_throwsException() {
        when(carRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> carService.getCarById(99L));
    }

    @Test
    void createCar_validCar_saves() {
        when(carRepository.save(any(Car.class))).thenReturn(sampleCar);

        CarResponse result = carService.createCar(sampleRequest);

        assertNotNull(result);
        assertEquals("Toyota", result.getMake());
        verify(carRepository).save(any(Car.class));
    }

    @Test
    void updateCar_found_updatesAndReturnsResponse() {
        CarRequest updatedRequest = new CarRequest("Toyota", "Camry", 2025, "Midnight Blue", 32000);
        Car updatedCar = new Car("Toyota", "Camry", 2025, "Midnight Blue", 32000);
        updatedCar.setId(1L);

        when(carRepository.findById(1L)).thenReturn(Optional.of(sampleCar));
        when(carRepository.save(sampleCar)).thenReturn(updatedCar);

        CarResponse result = carService.updateCar(1L, updatedRequest);

        assertEquals(2025, result.getYear());
        assertEquals("Midnight Blue", result.getColor());
        assertEquals(32000, result.getPrice());
        verify(carRepository).save(sampleCar);
    }

    @Test
    void deleteCar_found_deletes() {
        when(carRepository.findById(1L)).thenReturn(Optional.of(sampleCar));

        carService.deleteCar(1L);

        verify(carRepository).delete(sampleCar);
    }
}
