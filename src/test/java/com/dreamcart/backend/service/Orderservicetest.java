/*
 * Unit tests for OrderService: placing an order from the cart,
 * including stock deduction and the insufficient-stock guard that
 * the pessimistic locking in checkout relies on.
 */
package com.dreamcart.backend.service;

import com.dreamcart.backend.entity.*;
import com.dreamcart.backend.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ShippingAddressRepository shippingAddressRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;

    private User user;
    private ShippingAddress address;
    private Cart cart;
    private Product product;

    @BeforeEach
    void setUp() {

        user = User.builder().id(1L).email("jane@example.com").build();
        address = ShippingAddress.builder().id(1L).user(user).build();

        cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);

        product = Product.builder()
                .id(10L)
                .name("Galaxy A17")
                .price(new BigDecimal("1499.99"))
                .stockQuantity(5)
                .build();
    }

    @Test
    void placeOrder_deductsStockAndClearsCartOnSuccess() {

        CartItem item = new CartItem();
        item.setId(1L);
        item.setCart(cart);
        item.setProduct(product);
        item.setQuantity(2);

        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));
        when(shippingAddressRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(address));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCart(cart)).thenReturn(List.of(item));
        when(productRepository.findByIdWithLock(10L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order order = orderService.placeOrder("jane@example.com", 1L);

        assertEquals("PLACED", order.getStatus());
        assertEquals(0, new BigDecimal("2999.98").compareTo(order.getTotalAmount()));
        assertEquals(3, product.getStockQuantity()); // 5 - 2
        verify(productRepository).save(product);
        verify(cartItemRepository).deleteByCart(cart);
    }

    @Test
    void placeOrder_throwsWhenStockIsInsufficient() {

        product.setStockQuantity(1);

        CartItem item = new CartItem();
        item.setId(1L);
        item.setCart(cart);
        item.setProduct(product);
        item.setQuantity(2); // more than the 1 in stock

        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));
        when(shippingAddressRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(address));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCart(cart)).thenReturn(List.of(item));
        when(productRepository.findByIdWithLock(10L)).thenReturn(Optional.of(product));

        assertThrows(RuntimeException.class,
                () -> orderService.placeOrder("jane@example.com", 1L));

        verify(orderRepository, never()).save(any(Order.class));
        verify(cartItemRepository, never()).deleteByCart(cart);
    }

    @Test
    void placeOrder_throwsWhenCartIsEmpty() {

        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));
        when(shippingAddressRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(address));
        when(cartRepository.findByUser(user)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCart(cart)).thenReturn(List.of());

        assertThrows(RuntimeException.class,
                () -> orderService.placeOrder("jane@example.com", 1L));

        verify(orderRepository, never()).save(any(Order.class));
    }
}