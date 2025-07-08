package com.techlab.ecommerce.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    private Product product;
    private int quantity;

    public double getSubtotal() {
        return product.getPrice() * quantity;
    }
}
