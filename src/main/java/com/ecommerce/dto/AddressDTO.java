package com.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    
    private Long addressId;
    private Long userId;
    
    @NotBlank(message = "Address line 1 is required")
    @Size(max = 255)
    private String addressLine1;
    
    @Size(max = 255)
    private String addressLine2;
    
    @NotBlank(message = "City is required")
    @Size(max = 100)
    private String city;
    
    @NotBlank(message = "State is required")
    @Size(max = 100)
    private String state;
    
    @NotBlank(message = "Postal code is required")
    @Size(max = 20)
    private String postalCode;
    
    @NotBlank(message = "Country code is required")
    @Size(min = 2, max = 2)
    private String countryCode;
    
    private Boolean isDefault;
}

// Made with Bob
