package com.example.cardealership.dto;

import java.util.List;

public class OwnerResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private List<CarResponse> cars;

    public OwnerResponse() {}
    // Add getters and setters for all fields
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public List<CarResponse> getCars() { return cars; }
    public void setCars(List<CarResponse> cars) { this.cars = cars; }
}
