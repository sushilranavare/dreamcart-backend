/*
* Repository interface for Role entity
* This interface is used to perform database operations related to roles.
* Spring Data JPA automatically provides basic CRUD operations.
* */
package com.dreamcart.backend.repository;
import com.dreamcart.backend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name); // Used to find the role by name.

}
