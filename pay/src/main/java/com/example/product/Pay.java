package com.example.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.MimeTypeUtils;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PostPersist;

@Entity
public class Pay {

    @Id
    @GeneratedValue
    private Long id;
    private Long orderId;

    @PostPersist
    public void onCreated(){
        PayApproved payApproved = new PayApproved();
        payApproved.setOrderId(this.getOrderId());
        payApproved.setPayId(this.getId());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;

        try {
            json = objectMapper.writeValueAsString(payApproved);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON format exception", e);
        }

        Processor processor = PayApplication.applicationContext.getBean(Processor.class);
        MessageChannel outputChannel = processor.output();

        outputChannel.send(MessageBuilder
                .withPayload(json)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
