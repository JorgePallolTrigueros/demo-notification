package com.shoppingcart.notification.dao.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * shopping_cart -> - product1
 *                  - product2
 *                  - product3
 *
 *
 */

@Data
@Entity(name = "shopping_cart")
public class ShoppingCartItemEntity {

    @Id
    @NotNull
    private String id;

    @OneToMany(mappedBy = "shoppingCartEntity",cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<ProductShoppingCartEntity> products = new ArrayList<>();

}
