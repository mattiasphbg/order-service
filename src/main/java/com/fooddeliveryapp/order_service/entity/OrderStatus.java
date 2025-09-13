package com.fooddeliveryapp.order_service.entity; // Adjust package as needed

public enum OrderStatus {
    // Enum constants with associated description and final state flag
    PENDING("Order received, awaiting initial processing", false),
    Placed("Order has been placed and is being processed", false),
    ACCEPTED("Order has been accepted", false),
    PREPARING("Order is being prepared", false),
    In_TRANSIT("Order is on the way", false),
    DELIVERED("Order has been delivered", true),
    CANCELLED("Order has been cancelled", true),
    FAILED("Order failed to be processed", true),
    COMPLETED("Order has been completed", true);
  

    private final String description;
    private final boolean isFinalState; 

    // Constructor for the enum constants
    OrderStatus(String description, boolean isFinalState) {
        this.description = description;
        this.isFinalState = isFinalState;
    }

    // Getter methods for the fields
    public String getDescription() {
        return description;
    }

    public boolean isFinalState() {
        return isFinalState;
    }


}
