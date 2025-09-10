package com.fooddeliveryapp.order_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long productId;
    private String productName; 

    private Integer quantity;

    @Column(precision = 10, scale = 2)
    private BigDecimal pricePerUnit;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    
    @ElementCollection
    @CollectionTable(name = "order_item_notes", joinColumns = @JoinColumn(name = "order_item_id"))
    @Column(name = "note")
    private List<String> notes = new ArrayList<>();


}
