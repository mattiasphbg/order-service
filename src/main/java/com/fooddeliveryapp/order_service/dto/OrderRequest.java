package com.fooddeliveryapp.order_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {
    @NotNull(message = "Customer ID is required")
    private String customerId;

    @NotBlank(message = "Customer email is required")
    @Email(message = "Invalid email format")
    private String customerEmail;

    @NotEmpty(message = "Order must contain at least one item")
    @Valid
    private List<OrderItemRequest> items;

    @NotBlank(message = "Pickup location is required")
    private String pickupLocation;

    @NotBlank(message = "Dropoff location is required")
    private String dropoffLocation;


  
    @Getter(lazy = true)
    private final BigDecimal totalAmount = calculateTotalAmount();
    
    private BigDecimal calculateTotalAmount() {
        return subTotal
            .add(deliveryFee)
            .add(Optional.ofNullable(taxAmount).orElse(BigDecimal.ZERO))
            .add(Optional.ofNullable(tipAmount).orElse(BigDecimal.ZERO));
    }

    @NotNull(message = "SubTotal is required")
    @DecimalMin(value= "0.0", inclusive = false, message = "SubTotal must be greater than 0")
    private BigDecimal subTotal;

    @NotNull(message = "Delivery fee is required")
    @DecimalMin(value= "0.0", message = "Delivery fee must be greater than 0")
    private BigDecimal deliveryFee;

    @DecimalMin(value= "0.0", message = "Tax amount must be greater than 0")
    private BigDecimal taxAmount;

    @DecimalMin(value = "0.0", message = "Tip amount must be greater than 0")
    private BigDecimal tipAmount;

    @NotBlank(message = "Currency is required")
    @Size(min = 3, max = 3, message = "Currency must be 3 characters")
    private String currency;
}
