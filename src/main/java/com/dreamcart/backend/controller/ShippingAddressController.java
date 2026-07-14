/*
 * This controller exposes REST APIs for managing
 * shipping addresses of authenticated users.
 */
package com.dreamcart.backend.controller;

import com.dreamcart.backend.dto.request.ShippingAddressRequest;
import com.dreamcart.backend.entity.ShippingAddress;
import com.dreamcart.backend.service.ShippingAddressService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/address")
public class ShippingAddressController {

    private final ShippingAddressService shippingAddressService;

    public ShippingAddressController(
            ShippingAddressService shippingAddressService) {
        this.shippingAddressService = shippingAddressService;
    }

    /*
     * Add a new shipping address.
     */
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<ShippingAddress> addAddress(
            Authentication authentication,
            @Valid @RequestBody ShippingAddressRequest request) {

        return ResponseEntity.ok(
                shippingAddressService.addAddress(
                        authentication.getName(),
                        request
                )
        );
    }

    /*
     * Get all addresses of the authenticated user.
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<List<ShippingAddress>> getAddresses(
            Authentication authentication) {

        return ResponseEntity.ok(
                shippingAddressService.getAddresses(
                        authentication.getName()
                )
        );
    }

    /*
     * Update an existing shipping address.
     */
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/{id}")
    public ResponseEntity<ShippingAddress> updateAddress(
            @PathVariable Long id,
            Authentication authentication,
            @Valid @RequestBody ShippingAddressRequest request) {

        return ResponseEntity.ok(
                shippingAddressService.updateAddress(
                        id,
                        authentication.getName(),
                        request
                )
        );
    }

    /*
     * Delete a shipping address.
     */
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAddress(
            @PathVariable Long id,
            Authentication authentication) {

        shippingAddressService.deleteAddress(
                id,
                authentication.getName()
        );

        return ResponseEntity.ok("Address deleted successfully.");
    }

    /*
     * Set an address as the default shipping address.
     */
    @PreAuthorize("hasRole('USER')")
    @PutMapping("/default/{id}")
    public ResponseEntity<ShippingAddress> setDefaultAddress(
            @PathVariable Long id,
            Authentication authentication) {

        return ResponseEntity.ok(
                shippingAddressService.setDefaultAddress(
                        id,
                        authentication.getName()
                )
        );
    }
}