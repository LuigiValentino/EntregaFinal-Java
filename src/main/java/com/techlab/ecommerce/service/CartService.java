package com.techlab.ecommerce.service;

import com.techlab.ecommerce.entity.CartItem;
import com.techlab.ecommerce.entity.Product;
import com.techlab.ecommerce.exception.StockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CartService {

    private List<CartItem> cartItems = new ArrayList<>();

    @Autowired
    private ProductService productService;

    public void addToCart(Long productId, int quantity) {
        Product product = productService.getProductById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Producto no encontrado");
        }

        if (product.getStock() < quantity) {
            throw new StockException("No hay suficiente stock para: " + product.getName());
        }

        Optional<CartItem> existingItem = cartItems.stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            int newQuantity = item.getQuantity() + quantity;
            if (product.getStock() < newQuantity) {
                throw new StockException("No hay suficiente stock para: " + product.getName());
            }
            item.setQuantity(newQuantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            cartItems.add(newItem);
        }
    }

    public void updateCart(Long productId, int quantity) {
        Product product = productService.getProductById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Producto no encontrado");
        }

        if (product.getStock() < quantity) {
            throw new StockException("No hay suficiente stock para: " + product.getName());
        }

        cartItems.stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(item -> item.setQuantity(quantity));
    }

    public void removeFromCart(Long productId) {
        cartItems.removeIf(item -> item.getProduct().getId().equals(productId));
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public BigDecimal getTotalAmount() {
        return BigDecimal.valueOf(cartItems.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum());
    }

    public void clearCart() {
        cartItems.clear();
    }

    public void checkout() {
        clearCart();
    }

    public int getCartSize() {
        return cartItems.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }

    public int getCartSize(Long userId) {
        return getCartSize();
    }
}
