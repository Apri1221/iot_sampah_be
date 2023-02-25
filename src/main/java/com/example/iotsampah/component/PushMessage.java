package com.example.iotsampah.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class PushMessage {

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    public <T> void invokeWebSocketEndpoint(String endpoint, T payload) {
        this.simpMessagingTemplate.convertAndSend(endpoint, payload);
    }

}