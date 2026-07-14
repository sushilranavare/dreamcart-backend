/*This entity represents the role of a user in the system
* Example roles:-
* -ADMIN -> can manage products, categoires, and orders.
* -USER -> can browse products, categories, add items to cart, and place orders.
*  */

package com.dreamcart.backend.entity;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "role") //One role can be assigned to many uesrs
    private List<User> users;

}
