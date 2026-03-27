package com.example.cardealership.repository;

import com.example.cardealership.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByMake(String make);
    List<Car> findByColor(String color);
    List<Car> findByYear(int year);
    List<Car> findByPriceBetween(double min, double max);
    List<Car> findByMakeContainingIgnoreCase(String keyword);
    List<Car> findByOwnerIsNull();
    List<Car> findByOwnerIsNotNull();

    @Query("SELECT c FROM Car c WHERE LOWER(c.make) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(c.model) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Car> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT c FROM Car c WHERE " +
            "(:make IS NULL OR c.make = :make) AND " +
            "(:color IS NULL OR c.color = :color) AND " +
            "(:minYear IS NULL OR c.year >= :minYear) AND " +
            "(:maxYear IS NULL OR c.year <= :maxYear) AND " +
            "(:minPrice IS NULL OR c.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR c.price <= :maxPrice)")
    List<Car> filterCars(
            @Param("make") String make,
            @Param("color") String color,
            @Param("minYear") Integer minYear,
            @Param("maxYear") Integer maxYear,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice
    );
}
