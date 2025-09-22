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
import jakarta.persistence.EntityNotFoundException;
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

    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders(){
        return orderRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }


    @Transactional
    public OrderResponse updateOrder(Long orderId, OrderRequest orderRequest){
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));
        existingOrder.setCustomerId(orderRequest.getCustomerId());
        existingOrder.setCustomerEmail(orderRequest.getCustomerEmail());
        existingOrder.setPickupLocation(orderRequest.getPickupLocation());
        existingOrder.setDropoffLocation(orderRequest.getDropoffLocation());
        existingOrder.setSubtotal(orderRequest.getSubTotal());
        existingOrder.setDeliveryFee(orderRequest.getDeliveryFee());
        existingOrder.setTaxAmount(orderRequest.getTaxAmount());
        existingOrder.setTipAmount(orderRequest.getTipAmount());
        existingOrder.setTotalAmount(orderRequest.getTotalAmount());
        existingOrder.setCurrency(orderRequest.getCurrency());

        existingOrder.getItems().clear();
        orderRequest.getItems().forEach(itemRequest -> {
            OrderItem item = mapToEntity(itemRequest);
            item.setOrder(existingOrder);
            existingOrder.getItems().add(item);
        });

        Order updatedOrder = orderRepository.save(existingOrder);
        log.info("Order updated in database with ID: {}", updatedOrder.getId());

        return mapToResponseDto(updatedOrder);
    }

    @Transactional
    public void deleteOrder(Long orderId){
        if(!orderRepository.existsById(orderId)) {
            throw new EntityNotFoundException("Order not found with ID:", + orderId);
        }
        orderRepository.deleteById(orderId);
        log.info("Order deleted from database with ID: {}", orderId);
    }

    private Order mapToEntity(OrderRequest request){
        Order order = Order.builder().customerId(request.getCustomerId())
                .customerEmail(request.getCustomerEmail()).
                .pickupLocation(request.getPickupLocation())
                .dropoffLocation(request.getDropoffLocation()).
                .subtotal(request.getSubTotal()).
                .deliveryFee(request.getDeliveryFee()).
                .taxAmount(request.getTaxAmount()).
                .tipAmount(request.getTipAmount()).
                .totalAmount(request.getTotalAmount()).
                         add(request.getDeliveryFee()).
                         add(Optional.ofNullable(request.getTaxAmount()).orElse(BigDecimal.ZERO)).
                         add(Optional.ofNullable(request.getTipAmount()).orElse(BigDecimal.ZERO))
                .currency(request.getCurrency())
                .status(OrderStatus.PENDING).build();

        if(request.getItems() != null) {
            List<OrderItem> orderItems = request.getItems().stream()
                      .map(itemRequest ->{

                          OrderItem item = mapOrderItemRequestToEntity(request);
                          item.setOrder(order);
                          return item;
                        }
                           ).collect(Collectors.toList());
            order.setItems(orderItems);
        }
        return order;
        }
                
    }

}
