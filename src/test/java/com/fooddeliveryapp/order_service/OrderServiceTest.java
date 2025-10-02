package com.fooddeliveryapp.order_service;

import  com.fooddeliveryapp.order_service.dto.OrderItemRequest;
import  com.fooddeliveryapp.order_service.dto.OrderItemResponse;
import com.fooddeliveryapp.order_service.dto.OrderRequest;
import  com.fooddeliveryapp.order_service.entity.Order;
import  com.fooddeliveryapp.order_service.entity.OrderItem;
import  com.fooddeliveryapp.order_service.entity.OrderStatus;
import  com.fooddeliveryapp.order_service.kafka.OrderCreatedEventPublisher;
import  com.fooddeliveryapp.order_service.repository.OrderRepository;
import  com.fooddeliveryapp.order_service.service.OrderService;
import  jakarta.persistence.EntityNotFoundException;
import  org.junit.jupiter.api.BeforeEach;
import  org.junit.jupiter.api.Test;
import  org.junit.jupiter.api.extension.ExtendWith;
import  org.mockito.InjectMocks;
import  org.mockito.Mock;
import  org.mockito.junit.jupiter.MockitoExtension;

import  java.math.BigDecimal;
import  java.time.Instant;
import  java.util.ArrayList;
import  java.util.Collections;
import  java.util.List;
import  java.util.Optional;

import  static org.junit.jupiter.api.Assertions.*;
import  static org.mockito.ArgumentMatchers.any;
import  static org.mockito.Mockito.*;

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
    }

}