/*
* This class inserts initial data into database when the application starts.
* and if the data is not exists already.
* */
package com.dreamcart.backend.config;
import com.dreamcart.backend.entity.Category;
import com.dreamcart.backend.entity.Role;
import com.dreamcart.backend.repository.CategoryRepository;
import com.dreamcart.backend.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.dreamcart.backend.entity.User;
import com.dreamcart.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(RoleRepository roleRepository, CategoryRepository categoryRepository,
                               UserRepository userRepository, PasswordEncoder passwordEncoder){
        return args -> {
            // Insert roles if not present
            if(roleRepository.findByName("ADMIN").isEmpty()){
                roleRepository.save(Role.builder().name("ADMIN").build());
            }
            if(roleRepository.findByName("USER").isEmpty()){
                roleRepository.save(Role.builder().name("USER").build());
            }

            //Insert categories if not present
            if (categoryRepository.findByName("Fashion").isEmpty()){
                categoryRepository.save(
                        Category.builder()
                                .name("Fashion")
                                .description("Clothing and fashion accessories")
                                .build()
                );
            }
            if (categoryRepository.findByName("Books").isEmpty()){
                categoryRepository.save(
                        Category.builder()
                                .name("Books")
                                .description("Books across different genres")
                                .build()
                );
            }
            // Insert default admin user if not present
            if (userRepository.findByEmail("admin@dreamcart.com").isEmpty()) {

                Role adminRole = roleRepository.findByName("ADMIN")
                        .orElseThrow(() -> new RuntimeException("ADMIN role not found"));

                userRepository.save(
                        User.builder()
                                .firstName("Admin")
                                .lastName("DreamCart")
                                .email("admin@dreamcart.com")
                                .password(passwordEncoder.encode("Admin@123"))
                                .phoneNumber("9999999999")
                                .isActive(true)
                                .role(adminRole)
                                .build()
                );

                System.out.println("Default ADMIN user created.");
            }
        };
    }
}
