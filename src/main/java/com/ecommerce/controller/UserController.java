package com.ecommerce.controller;

import com.ecommerce.dto.ChangePasswordRequest;
import com.ecommerce.dto.UpdateUserRequest;
import com.ecommerce.dto.UserDTO;
import com.ecommerce.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "User profile operations")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getUserProfile(Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }

    @PutMapping("/profile")
    public ResponseEntity<UserDTO> updateUserProfile(@Valid @RequestBody UpdateUserRequest request,
                                                    Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        return ResponseEntity.ok(userService.updateUserProfile(
                userId,
                request.getFirstName(),
                request.getLastName(),
                request.getPhoneNumber()
        ));
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request,
                                              Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        userService.changePassword(userId, request.getCurrentPassword(), request.getNewPassword());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/account")
    public ResponseEntity<Void> deleteAccount(Authentication authentication) {
        Long userId = getUserIdFromAuth(authentication);
        userService.deleteAccount(userId);
        return ResponseEntity.noContent().build();
    }

    private Long getUserIdFromAuth(Authentication authentication) {
        return Long.parseLong(authentication.getName());
    }
}

// Made with Bob
