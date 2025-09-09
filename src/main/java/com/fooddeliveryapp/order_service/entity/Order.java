package com.fooddeliveryapp.order_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;





@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@Builder
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerId;
    private String customerEmail;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    private String pickupLocation;
    private String dropoffLocation;

    @Column(precision = 10, scale = 2)
    private BigDecimal subtotal;
    @Column(precision = 10, scale = 2)
    private BigDecimal deliveryFee;
    @Column(precision = 10, scale = 2)
    private BigDecimal taxAmount;
    @Column(precision = 10, scale = 2)
    private BigDecimal tipAmount;
    @Column(precision = 10, scale = 2)
    private BigDecimal totalAmount;
    private String currency;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private Long driverId;
    private Long restaurantId;

    private Instant createdAt;
    private Instant updatedAt;


    private String paymentTransactionId;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now(); 
        updatedAt = Instant.now();
        if (status == null) {
            status = OrderStatus.PENDING;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

}
