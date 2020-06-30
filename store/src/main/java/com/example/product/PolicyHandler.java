package com.example.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Processor;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler {

    @Autowired
    StoreRepository storeRepository;

    @StreamListener(Processor.INPUT)
    public void onEventListen(@Payload PayApproved payApproved){

//        if( payApproved != null && payApproved.equals(PayApproved.class.getSimpleName()) ){
//
//        }

        if( PayApproved.class.getSimpleName().equals(payApproved.getEventType()) ){
            System.out.println("=========요리시작 =========");
            System.out.println(payApproved);
            Store store = new Store();
            store.setOrderId(payApproved.getOrderId());
            storeRepository.save(store);
            System.out.println("==================");
        }
    }
}
