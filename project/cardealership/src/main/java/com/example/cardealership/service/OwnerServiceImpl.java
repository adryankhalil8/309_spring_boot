package com.example.cardealership.service;

import com.example.cardealership.dto.OwnerRequest;
import com.example.cardealership.dto.OwnerResponse;
import com.example.cardealership.entity.Owner;
import com.example.cardealership.exception.ResourceNotFoundException;
import com.example.cardealership.mapper.OwnerMapper;
import com.example.cardealership.repository.OwnerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OwnerServiceImpl implements OwnerService {

    private final OwnerRepository ownerRepository;
    private final OwnerMapper ownerMapper;

    public OwnerServiceImpl(OwnerRepository ownerRepository, OwnerMapper ownerMapper) {
        this.ownerRepository = ownerRepository;
        this.ownerMapper = ownerMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OwnerResponse> getAllOwners() {
        return ownerRepository.findAll().stream()
                .map(ownerMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OwnerResponse getOwnerById(Long id) {
        Owner owner = ownerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Owner", id));
        return ownerMapper.toResponse(owner);
    }

    @Override
    public OwnerResponse createOwner(OwnerRequest request) {
        Owner owner = ownerMapper.toEntity(request);
        Owner saved = ownerRepository.save(owner);
        return ownerMapper.toResponse(saved);
    }
}
