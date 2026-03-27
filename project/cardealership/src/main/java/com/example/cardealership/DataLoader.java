package com.example.cardealership;

import com.example.cardealership.entity.Car;
import com.example.cardealership.entity.Owner;
import com.example.cardealership.repository.CarRepository;
import com.example.cardealership.repository.OwnerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final CarRepository carRepository;
    private final OwnerRepository ownerRepository;

    public DataLoader(CarRepository carRepository, OwnerRepository ownerRepository) {
        this.carRepository = carRepository;
        this.ownerRepository = ownerRepository;
    }

    @Override
    public void run(String... args) {
        if (carRepository.count() == 0) {
            Owner john = ownerRepository.save(new Owner("John", "Smith", "john@example.com", "555-0100"));
            Owner jane = ownerRepository.save(new Owner("Jane", "Doe", "jane@example.com", "555-0200"));

            Car car1 = new Car("Toyota", "Camry", 2023, "Silver", 28000);
            car1.setOwner(john);
            carRepository.save(car1);

            Car car2 = new Car("Honda", "Civic", 2022, "Blue", 24000);
            car2.setOwner(john);
            carRepository.save(car2);

            Car car3 = new Car("Ford", "Mustang", 2024, "Red", 45000);
            car3.setOwner(jane);
            carRepository.save(car3);

            System.out.println("\n=== Seeded " + carRepository.count() + " cars and " + ownerRepository.count() + " owners ===");
        } else {
            System.out.println("\n=== Database already seeded - skipping ===");
        }
    }
}
