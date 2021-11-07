package com.cinch.kafka.orders.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import com.cinch.kafka.orders.model.CustomerContactInfo;

@Component
public class CustomerRequestConsumer {


    @Autowired
    private CustomerContactInfo customerContactInfo;

    @KafkaListener(topics = "${kafka.customer.consumer.topic-name}", containerFactory = "requestListenerContainerFactory")
    @SendTo()
    public String receive(String request) {
        customerContactInfo.setId(request);
        customerContactInfo.setEmailId("john.doe@gmail.com");
        customerContactInfo.setPhoneNumber("999-999-9999");
        return customerContactInfo.toString();
    }

}
