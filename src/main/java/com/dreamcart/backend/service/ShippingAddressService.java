/*
 * This service contains the business logic for managing
 * shipping addresses of authenticated users.
 */
package com.dreamcart.backend.service;

import com.dreamcart.backend.dto.request.ShippingAddressRequest;
import com.dreamcart.backend.entity.ShippingAddress;
import com.dreamcart.backend.entity.User;
import com.dreamcart.backend.repository.ShippingAddressRepository;
import com.dreamcart.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShippingAddressService {

    private final ShippingAddressRepository shippingAddressRepository;
    private final UserRepository userRepository;

    public ShippingAddressService(
            ShippingAddressRepository shippingAddressRepository,
            UserRepository userRepository) {

        this.shippingAddressRepository = shippingAddressRepository;
        this.userRepository = userRepository;
    }

    /*
     * Add a new shipping address for the authenticated user.
     */
    public ShippingAddress addAddress(String email,
                                      ShippingAddressRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if (Boolean.TRUE.equals(request.getIsDefault())) {

            List<ShippingAddress> addresses =
                    shippingAddressRepository.findByUser(user);

            for (ShippingAddress address : addresses) {
                address.setIsDefault(false);
            }

            shippingAddressRepository.saveAll(addresses);
        }

        ShippingAddress address = ShippingAddress.builder()
                .user(user)
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .addressLine1(request.getAddressLine1())
                .addressLine2(request.getAddressLine2())
                .city(request.getCity())
                .state(request.getState())
                .postalCode(request.getPostalCode())
                .country(request.getCountry())
                .isDefault(request.getIsDefault())
                .build();

        return shippingAddressRepository.save(address);
    }

    /*
     * Returns all addresses of the authenticated user.
     */
    public List<ShippingAddress> getAddresses(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return shippingAddressRepository.findByUser(user);
    }

    /*
     * Update an existing shipping address.
     */
    public ShippingAddress updateAddress(Long id,
                                         String email,
                                         ShippingAddressRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        ShippingAddress address =
                shippingAddressRepository.findByIdAndUser(id, user)
                        .orElseThrow(() ->
                                new RuntimeException("Address not found"));

        if (Boolean.TRUE.equals(request.getIsDefault())) {

            List<ShippingAddress> addresses =
                    shippingAddressRepository.findByUser(user);

            for (ShippingAddress item : addresses) {
                item.setIsDefault(false);
            }

            shippingAddressRepository.saveAll(addresses);
        }

        address.setFullName(request.getFullName());
        address.setPhoneNumber(request.getPhoneNumber());
        address.setAddressLine1(request.getAddressLine1());
        address.setAddressLine2(request.getAddressLine2());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setPostalCode(request.getPostalCode());
        address.setCountry(request.getCountry());
        address.setIsDefault(request.getIsDefault());

        return shippingAddressRepository.save(address);
    }

    /*
     * Delete a shipping address.
     */
    public void deleteAddress(Long id, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        ShippingAddress address =
                shippingAddressRepository.findByIdAndUser(id, user)
                        .orElseThrow(() ->
                                new RuntimeException("Address not found"));

        shippingAddressRepository.delete(address);
    }

    /*
     * Mark an address as the default address.
     */
    public ShippingAddress setDefaultAddress(Long id,
                                             String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        List<ShippingAddress> addresses =
                shippingAddressRepository.findByUser(user);

        ShippingAddress defaultAddress = null;

        for (ShippingAddress address : addresses) {

            if (address.getId().equals(id)) {
                address.setIsDefault(true);
                defaultAddress = address;
            } else {
                address.setIsDefault(false);
            }
        }

        shippingAddressRepository.saveAll(addresses);

        if (defaultAddress == null) {
            throw new RuntimeException("Address not found");
        }

        return defaultAddress;
    }
}