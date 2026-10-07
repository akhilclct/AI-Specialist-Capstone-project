package com.ecommerce.service;

import com.ecommerce.dto.BrandDTO;
import com.ecommerce.entity.Brand;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.repository.BrandRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BrandService {

    private final BrandRepository brandRepository;

    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public List<BrandDTO> getAllBrands() {
        return brandRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    public BrandDTO getBrandById(Long id) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand", "id", id));
        return toDTO(brand);
    }

    private BrandDTO toDTO(Brand brand) {
        return BrandDTO.builder()
                .brandId(brand.getBrandId())
                .name(brand.getName())
                .build();
    }
}

// Made with Bob
