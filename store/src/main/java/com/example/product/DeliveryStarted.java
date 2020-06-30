package com.example.product;

public class DeliveryStarted {

    String eventType;
    Long orderId;
    public DeliveryStarted(){
        this.eventType = DeliveryStarted.class.getSimpleName();
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
