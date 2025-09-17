package com.fooddeliveryapp.order_service.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemRequest {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotBlank(message = "Product name cannot be blank")
    private String productName;

    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Price per unit is required")
    @Min(value = 0, message = "Price per unit must be greater than 0")
    private BigDecimal pricePerUnit;

    private List<String> notes;
}
