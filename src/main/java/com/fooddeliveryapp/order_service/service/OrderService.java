package com.fooddeliveryapp.order_service.service;

import com.fooddeliveryapp.order_service.dto.OrderItemRequest;
import com.fooddeliveryapp.order_service.dto.OrderItemResponse;
import com.fooddeliveryapp.order_service.dto.OrderRequest;
import com.fooddeliveryapp.order_service.dto.OrderResponse;
import com.fooddeliveryapp.order_service.entity.Order;
import com.fooddeliveryapp.order_service.entity.OrderItem;
import com.fooddeliveryapp.order_service.entity.OrderStatus;
import com.fooddeliveryapp.order_service.kafka.OrderCreatedEventPublisher;
import com.fooddeliveryapp.order_service.kafka.OrderPlacedEvent;
import com.fooddeliveryapp.order_service.repository.OrderRepository;
import jakarta.persistence.EntityExistsException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final OrderCreatedEventPublisher orderCreatedEventPublisher;

    public OrderService(OrderRepository orderRepository, OrderCreatedEventPublisher orderCreatedEventPublisher){
        this.orderRepository = orderRepository;
        this.orderCreatedEventPublisher = orderCreatedEventPublisher;
    }

     
    @Transactional // Ensures that database operations within this method are atomic
    public OrderResponse createOrder(OrderRequest orderRequest) {
     
        Order order = mapToEntity(orderRequest);

        Order savedOrder = orderRepository.save(order);
        log.info("Order saved to database with ID: {}", savedOrder.getId());

       
        OrderPlacedEvent event = mapToOrderPlacedEvent(savedOrder);

        orderCreatedEventPublisher.publish(event);
        log.info("OrderPlacedEvent published for order ID: {}", savedOrder.getId());

     
        return mapToResponseDto(savedOrder);
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long orderId){
        Order order = orderRepository.findById(orderId)
                 .orElseThrow(() -> new EntityExistsException("Order not found with id:" + orderId));
        return mapToResponseDto(order);
    }
}
