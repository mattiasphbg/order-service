package com.fooddeliveryapp.order_service.controller;


import com.fooddeliveryapp.order_service.dto.OrderRequest;
import com.fooddeliveryapp.order_service.dto.OrderResponse;
import com.fooddeliveryapp.order_service.service.OrderService;
import jakarta.persistence.EntityExistsException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/orders")
@Slf4j
public class OrderController {
    
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        log.info("Received request to create a new order for customer: {}", orderRequest.getCustomerId());
        OrderResponse createdOrder = orderService.createOrder(orderRequest);
        log.info("Order created successfully with ID: {}", createdOrder.getId());
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }


    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long orderId) {
        log.info("Received request to get order by ID: {}", orderId);
        OrderResponse orderResponse = orderService.getOrderById(orderId);
        log.info("Order with ID {} retrieved successfully.", orderId);
        return ResponseEntity.ok(orderResponse);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        log.info("Received request to get all orders.");
        List<OrderResponse> orders = orderService.getAllOrders();
        log.info("Retrieved {} orders.", orders.size());
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponse> updateOrder(@PathVariable Long orderId,
                                                     @Valid @RequestBody OrderRequest orderRequest) {
        log.info("Received request to update order with ID: {}",
        orderId);
        OrderResponse updatedOrder = orderService.updateOrder(orderId, orderRequest);
        log.info("Order with ID {} updated successfully.", orderId);
        return ResponseEntity.ok(updatedOrder);                                                 
    }


}
