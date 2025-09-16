package com.fooddeliveryapp.order_service.kafka;

public interface OrderCreatedEventPublisher {
    void publish(OrderPlacedEvent event);
}
