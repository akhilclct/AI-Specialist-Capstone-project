package com.ecommerce.controller;

import com.ecommerce.dto.AddressDTO;
import com.ecommerce.service.AddressService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@Tag(name = "Addresses", description = "User address management")
@SecurityRequirement(name = "bearerAuth")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    public ResponseEntity<List<AddressDTO>> getUserAddresses(Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        return ResponseEntity.ok(addressService.getUserAddresses(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long id, Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        return ResponseEntity.ok(addressService.getAddressById(userId, id));
    }

    @PostMapping
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO, 
                                                    Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(addressService.createAddress(userId, addressDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long id,
                                                    @Valid @RequestBody AddressDTO addressDTO,
                                                    Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        return ResponseEntity.ok(addressService.updateAddress(userId, id, addressDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long id, Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        addressService.deleteAddress(userId, id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/set-default")
    public ResponseEntity<AddressDTO> setDefaultAddress(@PathVariable Long id, Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        return ResponseEntity.ok(addressService.setDefaultAddress(userId, id));
    }

    private Long getUserIdFromAuth(Authentication authentication) {
        return Long.parseLong(authentication.getName());
    }
}

// Made with Bob
