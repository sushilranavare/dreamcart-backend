package com.dreamcart.backend.repository;

import com.dreamcart.backend.entity.ShippingAddress;
import com.dreamcart.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShippingAddressRepository
        extends JpaRepository<ShippingAddress, Long> {

    List<ShippingAddress> findByUser(User user);

    Optional<ShippingAddress> findByIdAndUser(Long id, User user);
}