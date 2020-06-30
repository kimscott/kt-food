package com.example.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.client.RestTemplate;

import javax.persistence.*;

@Entity
@Table(name="order_table")
public class Order {
    @Id @GeneratedValue
    Long orderId;
//    Long itemId;
    String item;
//    int qty;
//    int price;

    @PostPersist
    public void onCheck(){

        // 1. 주문됨 이벤트 발송

        OrderPlaced orderPlaced = new OrderPlaced();
        orderPlaced.setOrderId(this.getOrderId());
        orderPlaced.setItem(this.getItem());


        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;

        try {
            json = objectMapper.writeValueAsString(orderPlaced);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON format exception", e);
        }

        Processor processor = OrderApplication.applicationContext.getBean(Processor.class);
        MessageChannel outputChannel = processor.output();

        outputChannel.send(MessageBuilder
                .withPayload(json)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build());


        // 2. 결재 정보 post
        RestTemplate restTemplate = OrderApplication.applicationContext.getBean(RestTemplate.class);
        String payUrl = "http://localhost:8082/pays";
        Pay pay = new Pay();
        pay.setOrderId(this.getOrderId());
        ResponseEntity<String> response = restTemplate.postForEntity(payUrl, pay ,String.class);
//        if( response.getStatusCode() == HttpStatus.CREATED){
//
//        }
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
