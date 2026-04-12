/*Repository interface for the user entity.
This repository helps interact with the uesrs table.
and also used to registration , login and user related features.
* */
package com.dreamcart.backend.repository;
import com.dreamcart.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository <User, Long>{
    Optional<User> findByEmail(String email); //Used to check whether the user exists with the given email.
}
