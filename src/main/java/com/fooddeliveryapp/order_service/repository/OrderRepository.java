package com.fooddeliveryapp.order_service.repository;

import com.fooddeliveryapp.order_service.entity.Order;
import com.fooddeliveryapp.order_service.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

      List<Order> findByCustomerEmail(String customerEmail);
      List<Order> findByStatus(OrderStatus status);
      List<Order> findByCustomerIdAndStatus(String customerId, OrderStatus status);
      List<Order> findByTotalAmountGreaterThan(BigDecimal amount);
      Optional<Order> findByPaymentTransactionId(String paymentTranstionID);
      
      
}
