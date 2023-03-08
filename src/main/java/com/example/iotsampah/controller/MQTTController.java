package com.example.iotsampah.controller;

import com.example.iotsampah.service.MqttService;
import com.example.iotsampah.service.MstDevicesService;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
public class MQTTController {
    @Autowired
    MstDevicesService mstDevicesService;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    private final String uri = "tcp://tailor.cloudmqtt.com:16503";

    @GetMapping("/send")
    public ResponseEntity sendMessage(@RequestParam(value = "topic") String topic, @RequestBody String body) throws MqttException, URISyntaxException {
        String clientId = UUID.randomUUID().toString();
        MqttService s = new MqttService(this.uri, topic, "server-" + clientId, simpMessagingTemplate);
        s.sendMessage(body);
        return ResponseEntity.ok(s.getClientId());
    }

    @GetMapping("/subscribe")
    public ResponseEntity subscribeMessage(
            @RequestParam(value = "topic", required = false) String topic,
            @RequestParam(value = "school_id") Integer schoolId) throws MqttException, URISyntaxException {
        List<String> topics = topic == null ? new ArrayList<>() : Arrays.asList(topic.split("\\s*,\\s*"));
        Integer clientId = schoolId;
//        String clientId = UUID.randomUUID().toString();
        if (topics.size() == 0) {
            topics = mstDevicesService.getTopicsBySchoolId(schoolId);
        }
        MqttService s = new MqttService(this.uri, topics.toArray(new String[0]), "server-" + clientId, simpMessagingTemplate);
        return ResponseEntity.ok(s.getClientId());
    }

    @GetMapping("/unsubscribe/{clientId}")
    public ResponseEntity unsubscribeMessage(@RequestParam(value = "topic") String topic, @PathVariable(value = "clientId") String clientId) throws MqttException, URISyntaxException {
        MqttService s = new MqttService(this.uri, topic, "server-" + clientId, simpMessagingTemplate);
        s.unsubscribe(topic);
        return ResponseEntity.ok(s.getClientId());
    }
}
