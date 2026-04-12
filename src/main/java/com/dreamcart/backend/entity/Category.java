/*
* This entity represents the product category in the system.
*
* Ex., Electronics, Fashion,Books. This is easy for browse products using filter
* */
package com.dreamcart.backend.entity;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 255)
    private String description;

    @OneToMany(mappedBy = "category") //One category can contain many products
    private List<Product> products;
}
