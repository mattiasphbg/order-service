package com.fooddeliveryapp.order_service.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class KafkaOrderProducer implements OrderCreatedEventPublisher {
    
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String orderCreatedTopic;
    private final ObjectMapper objectMapper;

    public KafkaOrderProducer(KafkaTemplate<String, String> kafkaTemplate,  @Value("${kafka.topics.order-created}") String orderCreatedTopic, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.orderCreatedTopic = orderCreatedTopic;
        this.objectMapper = objectMapper;
    }
    
    @Override
    public void publish(OrderPlacedEvent event) {
        try {
            String eventMessage = objectMapper.writeValueAsString(event);
            String key = event.getOrderId().toString();

            log.info("Publishing orderPlaceEvent to topic {} with key {}: {}", orderCreatedTopic, key, eventMessage); 

            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(orderCreatedTopic, key, eventMessage);

          
            future.whenComplete((result, ex) -> { 
                if (ex == null) {
                    log.info("Successfully published OrderPlacedEvent [key={}, partition={}, offset={}] to topic {}",
                            key, result.getRecordMetadata().partition(), result.getRecordMetadata().offset(), orderCreatedTopic);
                } else {
                    log.error("Failed to publish OrderPlacedEvent [key={}] to topic {}, Error: {}",
                    key, orderCreatedTopic, ex.getMessage(), ex);
                }
            }); 

        } catch (JsonProcessingException e) {
            log.error("Error serializing OrderPlacedEvent to JSON: {}", e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error while publishing OrderPlacedEvent: {}", e.getMessage(), e);
        }
    }

}
