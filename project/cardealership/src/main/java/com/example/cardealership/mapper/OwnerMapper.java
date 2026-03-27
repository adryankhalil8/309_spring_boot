package com.example.cardealership.mapper;

import com.example.cardealership.dto.OwnerRequest;
import com.example.cardealership.dto.OwnerResponse;
import com.example.cardealership.entity.Owner;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OwnerMapper {

    private final CarMapper carMapper;

    public OwnerMapper(CarMapper carMapper) {
        this.carMapper = carMapper;
    }

    public OwnerResponse toResponse(Owner owner) {
        OwnerResponse response = new OwnerResponse();
        response.setId(owner.getId());
        response.setFirstName(owner.getFirstName());
        response.setLastName(owner.getLastName());
        response.setEmail(owner.getEmail());
        response.setPhone(owner.getPhone());
        response.setCars(owner.getCars().stream()
                .map(carMapper::toResponse)
                .collect(Collectors.toList()));
        return response;
    }

    public Owner toEntity(OwnerRequest request) {
        Owner owner = new Owner();
        owner.setFirstName(request.getFirstName());
        owner.setLastName(request.getLastName());
        owner.setEmail(request.getEmail());
        owner.setPhone(request.getPhone());
        return owner;
    }
}