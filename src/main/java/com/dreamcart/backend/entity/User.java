/*This entity stores user account information for the DreamCart system
*
* A user can be normal customer or an adult depending on the assigned role.
* Used for Authentication and Authorization
*  */
package com.dreamcart.backend.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, unique = true, length = 150) //Email is unique so no two user can login with same email.
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 12)
    private String phoneNumber;

    @Column(nullable = false) // This filed is for to check whether the user is Active or not.
    private boolean isActive = true;

    @ManyToOne  // many users can share the same role, Ex. USER or ADMIN
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

}
