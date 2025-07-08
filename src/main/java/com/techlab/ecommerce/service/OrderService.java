package com.techlab.ecommerce.service;

import com.techlab.ecommerce.entity.*;
import com.techlab.ecommerce.exception.StockException;
import com.techlab.ecommerce.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;

    public OrderService(OrderRepository orderRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
    }

    @Transactional
    public Order createOrder(Usuario user, List<CartItem> cartItems, String shippingAddress, String paymentMethod) {
        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("El carrito está vacío");
        }

        Order order = new Order();
        order.setUsuario(user);
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus("PENDIENTE");
        order.setShippingAddress(shippingAddress);
        order.setPaymentMethod(paymentMethod);

        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0.0;

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();

            if (product.getStock() < cartItem.getQuantity()) {
                throw new StockException("Stock insuficiente para: " + product.getName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setOrder(order);

            orderItems.add(orderItem);

            total += product.getPrice() * cartItem.getQuantity();

            product.setStock(product.getStock() - cartItem.getQuantity());
            productService.saveProduct(product);
        }

        order.setItems(orderItems);
        order.setTotal(total);

        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Transactional
    public void updateOrderStatus(Long orderId, String status) {
        Order order = getOrderById(orderId);
        if (order != null) {
            order.setStatus(status);
            orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Pedido no encontrado con id: " + orderId);
        }
    }
}
