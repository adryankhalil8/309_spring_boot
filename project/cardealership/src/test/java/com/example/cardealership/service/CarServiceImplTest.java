package com.example.cardealership.service;

import com.example.cardealership.entity.Car;
import com.example.cardealership.repository.CarRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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

    @InjectMocks
    private CarServiceImpl carService;

    private Car sampleCar;

    @BeforeEach
    void setUp() {
        sampleCar = new Car("Toyota", "Camry", 2023, "Silver", 28000);
        sampleCar.setId(1L);
    }

    @Test
    void getAllCars_returnsList() {
        // Arrange
        List<Car> cars = Arrays.asList(sampleCar, new Car("Honda", "Civic", 2022, "Blue", 24000));
        when(carRepository.findAll()).thenReturn(cars);

        // Act
        List<Car> result = carService.getAllCars();

        // Assert
        assertEquals(2, result.size());
        verify(carRepository, times(1)).findAll();
    }

    @Test
    void getCarById_found() {
        when(carRepository.findById(1L)).thenReturn(Optional.of(sampleCar));

        Car result = carService.getCarById(1L);

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

        Car result = carService.createCar(sampleCar);

        assertNotNull(result);
        assertEquals("Toyota", result.getMake());
        verify(carRepository).save(sampleCar);
    }

    @Test
    void createCar_negativePrice_throwsException() {
        Car badCar = new Car("Test", "Car", 2023, "Red", -5000);

        assertThrows(IllegalArgumentException.class, () -> carService.createCar(badCar));
        verify(carRepository, never()).save(any());
    }

    @Test
    void createCar_invalidYear_throwsException() {
        Car badCar = new Car("Test", "Car", 1800, "Red", 25000);

        assertThrows(IllegalArgumentException.class, () -> carService.createCar(badCar));
        verify(carRepository, never()).save(any());
    }

    @Test
    void deleteCar_found_deletes() {
        when(carRepository.findById(1L)).thenReturn(Optional.of(sampleCar));

        carService.deleteCar(1L);

        verify(carRepository).delete(sampleCar);
    }
}