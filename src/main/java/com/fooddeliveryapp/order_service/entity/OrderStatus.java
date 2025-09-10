package com.fooddeliveryapp.order_service.entity; // Adjust package as needed

public enum OrderStatus {
    // Enum constants with associated description and final state flag
    PENDING("Order received, awaiting initial processing", false),
    Placed("Order has been placed and is being processed", false),
  

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

    // Optional: A method to check if a transition is valid (more complex, often handled by a state machine pattern)
    // public boolean isValidTransition(OrderStatus newStatus) {
    //     // Implement state machine logic here
    //     return true; // Placeholder
    // }
}
