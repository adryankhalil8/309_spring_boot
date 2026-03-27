package com.example.cardealership.service;

import com.example.cardealership.dto.OwnerRequest;
import com.example.cardealership.dto.OwnerResponse;

import java.util.List;

public interface OwnerService {
    List<OwnerResponse> getAllOwners();
    OwnerResponse getOwnerById(Long id);
    OwnerResponse createOwner(OwnerRequest request);
}