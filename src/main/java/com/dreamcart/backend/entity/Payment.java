/*
* This entity stores payment details for an order.
* Every order has one associated payment.
* */

package com.dreamcart.backend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.AnyDiscriminatorImplicitValues;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name="payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
    * Order assiciated with this payment.
    * */

    @OneToOne
    @JoinColumn(name="order_id", nullable = false)
    private Order order;

    /**
     * Selected Payment method
     */
    @Enumerated (EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    /*
    * Total payment amount
    * */

    @Column(nullable = false)
    private BigDecimal amount;

    /*/*
    Current payment status
    */
     @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

     /*
     * Unique transaction identifier
     * */

    @Column(unique = true)
    private String transactionId;

    /*Date and time of payment */
    private LocalDateTime paymentDate;

}
