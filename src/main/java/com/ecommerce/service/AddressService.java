package com.ecommerce.service;

import com.ecommerce.dto.AddressDTO;
import com.ecommerce.entity.Address;
import com.ecommerce.entity.User;
import com.ecommerce.exception.BadRequestException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.repository.AddressRepository;
import com.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressService(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    public List<AddressDTO> getUserAddresses(Long userId) {
        return addressRepository.findByUserUserId(userId).stream()
                .map(this::toDTO)
                .toList();
    }

    public AddressDTO getAddressById(Long userId, Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressId));
        
        if (!address.getUser().getUserId().equals(userId)) {
            throw new BadRequestException("Address does not belong to user");
        }
        
        return toDTO(address);
    }

    public AddressDTO createAddress(Long userId, AddressDTO addressDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        // If this is set as default, unset other default addresses
        if (Boolean.TRUE.equals(addressDTO.getIsDefault())) {
            addressRepository.findByUserUserIdAndIsDefaultTrue(userId)
                    .ifPresent(addr -> {
                        addr.setIsDefault(false);
                        addressRepository.save(addr);
                    });
        }

        Address address = Address.builder()
                .user(user)
                .addressLine1(addressDTO.getAddressLine1())
                .addressLine2(addressDTO.getAddressLine2())
                .city(addressDTO.getCity())
                .state(addressDTO.getState())
                .postalCode(addressDTO.getPostalCode())
                .countryCode(addressDTO.getCountryCode())
                .isDefault(addressDTO.getIsDefault() != null ? addressDTO.getIsDefault() : false)
                .build();

        return toDTO(addressRepository.save(address));
    }

    public AddressDTO updateAddress(Long userId, Long addressId, AddressDTO addressDTO) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressId));

        if (!address.getUser().getUserId().equals(userId)) {
            throw new BadRequestException("Address does not belong to user");
        }

        address.setAddressLine1(addressDTO.getAddressLine1());
        address.setAddressLine2(addressDTO.getAddressLine2());
        address.setCity(addressDTO.getCity());
        address.setState(addressDTO.getState());
        address.setPostalCode(addressDTO.getPostalCode());
        address.setCountryCode(addressDTO.getCountryCode());

        return toDTO(addressRepository.save(address));
    }

    public void deleteAddress(Long userId, Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressId));

        if (!address.getUser().getUserId().equals(userId)) {
            throw new BadRequestException("Address does not belong to user");
        }

        addressRepository.delete(address);
    }

    public AddressDTO setDefaultAddress(Long userId, Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressId));

        if (!address.getUser().getUserId().equals(userId)) {
            throw new BadRequestException("Address does not belong to user");
        }

        // Unset other default addresses
        addressRepository.findByUserUserIdAndIsDefaultTrue(userId)
                .ifPresent(addr -> {
                    addr.setIsDefault(false);
                    addressRepository.save(addr);
                });

        address.setIsDefault(true);
        return toDTO(addressRepository.save(address));
    }

    private AddressDTO toDTO(Address address) {
        return AddressDTO.builder()
                .addressId(address.getAddressId())
                .userId(address.getUser().getUserId())
                .addressLine1(address.getAddressLine1())
                .addressLine2(address.getAddressLine2())
                .city(address.getCity())
                .state(address.getState())
                .postalCode(address.getPostalCode())
                .countryCode(address.getCountryCode())
                .isDefault(address.getIsDefault())
                .build();
    }
}

// Made with Bob
