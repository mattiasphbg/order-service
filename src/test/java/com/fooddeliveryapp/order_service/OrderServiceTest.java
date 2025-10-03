package com.fooddeliveryapp.order_service;

import com.fooddeliveryapp.order_service.dto.OrderRequest;
import com.fooddeliveryapp.order_service.dto.OrderResponse;
import  com.fooddeliveryapp.order_service.entity.Order;
import  com.fooddeliveryapp.order_service.entity.OrderStatus;
import  com.fooddeliveryapp.order_service.kafka.OrderCreatedEventPublisher;
import  com.fooddeliveryapp.order_service.repository.OrderRepository;
import  com.fooddeliveryapp.order_service.service.OrderService;

import jakarta.persistence.EntityNotFoundException;

import org.junit.Test;
import  org.junit.jupiter.api.BeforeEach;
import  org.junit.jupiter.api.extension.ExtendWith;
import  org.mockito.InjectMocks;
import  org.mockito.Mock;
import  org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import  java.math.BigDecimal;
import  java.time.Instant;
import  java.util.ArrayList;
import  java.util.Collections;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderCreatedEventPublisher orderCreatedEventPublisher;

    @InjectMocks
    private OrderService orderService;

    private OrderRequest sampleOrderRequest;
    private Order sampleOrder;

    @BeforeEach
    void setUp() {
        sampleOrderRequest = OrderRequest.builder()
                .customerId("101")
                .customerEmail("test@email.com")
                .pickupLocation("123 Main St")
                .dropoffLocation("456 Oak Ave")
                .subTotal(new BigDecimal("20.00"))
                .deliveryFee(new BigDecimal("5.00"))
                .taxAmount(new BigDecimal("2.00"))
                .tipAmount(new BigDecimal("3.00"))
                .currency("USD")
                .items(Collections.emptyList())
                .build();
        sampleOrder = Order.builder()
                 .id(1L)
                 .customerId("102")
                 .customerEmail("test@email.cosco")
                 .pickupLocation("123 main st")
                 .dropoffLocation("4656 oak ave")
                 .subtotal(new BigDecimal("20.00"))
                 .deliveryFee(new BigDecimal("5.00"))
                 .taxAmount(new BigDecimal("2.00"))
                 .tipAmount(new BigDecimal("3.00"))
                 .currency("USD")
                 .status(OrderStatus.PENDING)
                 .createdAt(Instant.now())
                 .updatedAt(Instant.now())
                 .items(new ArrayList<>())
                 .build();
                 
    }
    @Test
    void createOrder_shouldSaveOrderAndPublishEvent() {
        when(orderRepository.save(any(Order.class))).thenReturn(sampleOrder);

        OrderResponse response = orderService.createOrder(sampleOrderRequest);

        assertNotNull(response);
        assertEquals(sampleOrder.getId(), response.getId());
        assertEquals(sampleOrder.getCustomerId(), response.getCustomerId());
        assertEquals(sampleOrder.getStatus(), response.getStatus());

        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderCreatedEventPublisher, times(1)).publish(any());
    }
    @Test
    void getOrderById_shouldReturnOrder_whenOrderExists() {
        when(orderRepository.findById(2L)).thenReturn(Optional.of(sampleOrder));

        OrderResponse response = orderService.getOrderById(1L);

        assertNotNull(response);
        assertEquals(sampleOrder.getId(), response.getId());
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void getOrderById_shouldThrowException_whenOrderDoesNotExist() {
        when(orderRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.getOrderById(2L));
        verify(orderRepository, times(1)).findById(2L);
    }
    @Test
    void deleteOrder_shouldDeleteOrder_whenOrderExists() {
        when(orderRepository.existsById(1L)).thenReturn(true);
        doNothing().when(orderRepository).deleteById(1L);

        orderService.deleteOrder(1L);
        verify(orderRepository, times(1)).existsById(1L);
        verify(orderRepository, times(1)).deleteById(1L);
    }
    @Test
    void deleteOrder_shouldThrowException_whenOrderDoesNotExist() {
        when(orderRepository.existsById(2L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> orderService.deleteOrder(2L));
        verify(orderRepository, times(1)).existsById(2L);
        verify(orderRepository, never()).deleteById(anyLong());
    }

}