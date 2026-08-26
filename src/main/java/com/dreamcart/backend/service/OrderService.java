package com.dreamcart.backend.service;

import com.dreamcart.backend.entity.*;
import com.dreamcart.backend.exceptions.ResourceNotFoundException;
import com.dreamcart.backend.repository.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Email;
import org.springframework.stereotype.Service;
import com.dreamcart.backend.repository.ShippingAddressRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final ShippingAddressRepository shippingAddressRepository;
    private final ProductRepository productRepository;
    
    public OrderService(
            UserRepository userRepository,
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            OrderRepository orderRepository,
            ShippingAddressRepository  shippingAddressRepository, ProductRepository productRepository) {

        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.orderRepository = orderRepository;
        this.shippingAddressRepository = shippingAddressRepository;
        this.productRepository = productRepository;
    }

    /**
     * Places an order using all items currently in the user's cart.
     */
    @Transactional
    public Order placeOrder(String email, Long addressId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        ShippingAddress address =
            shippingAddressRepository
                        .findByIdAndUser(addressId, user)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Shipping address not found"));

        Order order = new Order();

        order.setUser(user);
        order.setShippingAddress(address);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PLACED");

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart not found"));

        List<CartItem> cartItems = cartItemRepository.findByCart(cart);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        List<OrderItem> orderItems = new ArrayList<>();

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : cartItems) {

            //Fetch product with pessimistic lock
            Product lockedProduct = productRepository.findByIdWithLock(item.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Product Not Found"));

            //Validate Stock while holding the stock
            if(lockedProduct.getStockQuantity() < item.getQuantity()) {
                throw new RuntimeException("Inssufficient stock for product" + lockedProduct.getName());
            }

            //Deduct the stock
            lockedProduct.setStockQuantity(lockedProduct.getStockQuantity() - item.getQuantity());
            productRepository.save(lockedProduct);
            
            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(order);
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());

            BigDecimal price = item.getProduct().getPrice();

            orderItem.setPrice(price);

            total = total.add(
                    price.multiply(BigDecimal.valueOf(item.getQuantity()))
            );

            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);
        order.setTotalAmount(total);

        Order savedOrder = orderRepository.save(order);

        cartItemRepository.deleteByCart(cart);

        return savedOrder;
    }

    /**
     * Returns all orders for a user.
     */
    public List<Order> getOrders(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        return orderRepository.findByUser(user);
    }

    /**
     * Returns one order by its ID.
     */
    public Order getOrder(Long id) {

        return orderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found"));
    }
    /**
     * Returns all orders in the system.
     * (Used by Admin)
     */
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    /**
     * Updates the status of a specific order.
     * (Used by Admin)
     */
    public Order updateOrderStatus(Long id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        order.setStatus(status);
        return orderRepository.save(order);
    }
}