package com.fooddeliveryapp.order_service.dto;

import com.fooddeliveryapp.order_service.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private Long id;
    private Long customerId; 
    private String customerEmail;
    private List<OrderItemResponse> items;
    private String pickupLocation;
    private String dropoffLocation;
    private BigDecimal subTotal;
    private BigDecimal deliveryFee;
    private BigDecimal taxAmount;
    private BigDecimal tipAmount;
    private BigDecimal totalAmount;
    private String currency;
    private OrderStatus status;
    private Long driverId;
    private Long restaurantId;
    private Instant createdAt;
    private Instant updatedAt;
    private String paymentTransactionId;
}
