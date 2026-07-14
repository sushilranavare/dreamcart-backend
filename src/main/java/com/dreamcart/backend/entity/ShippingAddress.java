/*
 * This entity stores shipping addresses for users.
 * A user can save multiple addresses and choose one
 * as the default delivery address.
 */
package com.dreamcart.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "shipping_addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShippingAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Owner of the shipping address.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /*
     * Full name of the receiver.
     */
    @Column(nullable = false)
    private String fullName;

    /*
     * Contact phone number.
     */
    @Column(nullable = false)
    private String phoneNumber;

    /*
     * House number / Street.
     */
    @Column(nullable = false)
    private String addressLine1;

    /*
     * Apartment, building etc.
     */
    private String addressLine2;

    /*
     * City.
     */
    @Column(nullable = false)
    private String city;

    /*
     * State.
     */
    @Column(nullable = false)
    private String state;

    /*
     * Postal code.
     */
    @Column(nullable = false)
    private String postalCode;

    /*
     * Country.
     */
    @Column(nullable = false)
    private String country;

    /*
     * Indicates whether this is the default address.
     */
    private Boolean isDefault;
}