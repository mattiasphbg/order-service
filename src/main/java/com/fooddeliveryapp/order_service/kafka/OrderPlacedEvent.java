package com.fooddeliveryapp.order_service.kafka;

import com.fooddeliveryapp.order_service.entity.OrderStatus;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderPlacedEvent {
    

    private String eventId;
    private Instant timeStamp;
    private String eventType;
    private String sourceService;

    private Long orderId;
    private Long customerId;
    private String customerEmail;
    private BigDecimal totalAmount;
    private String currency;
    private String pickupLocation;
    private String dropoffLocation;
    private OrderStatus status;
    
    private List<OrderItemEventDto> items;

}
