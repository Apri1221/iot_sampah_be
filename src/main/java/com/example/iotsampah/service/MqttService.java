package com.example.iotsampah.service;

import com.example.iotsampah.entity.OutputMessage;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MqttService implements MqttCallback {
    private final int qos = 1;
    private String topic, clientId, message;
    private String[] topics;
    private MqttClient client;
    private String username = "lvntsnrq";
    private String password = "79W8iNWE4G9i";
    private boolean isOutliersJarak, isIRDetection, isAllConditionTrue, isPIRDetection;
    private List<Double> dataJarak = new ArrayList<>();
    private List<Double> dataIR = new ArrayList<>();

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isOutliersJarak() {
        return isOutliersJarak;
    }

    public void setOutliersJarak(boolean outliersJarak) {
        isOutliersJarak = outliersJarak;
    }

    public boolean isIRDetection() {
        return isIRDetection;
    }

    public void setIRDetection(boolean IRDetection) {
        isIRDetection = IRDetection;
    }

    public boolean isAllConditionTrue() {
        return isAllConditionTrue;
    }

    public void setAllConditionTrue(boolean allConditionTrue) {
        isAllConditionTrue = allConditionTrue;
    }

    public boolean isPIRDetection() {
        return isPIRDetection;
    }

    public void setPIRDetection(boolean PIRDetection) {
        isPIRDetection = PIRDetection;
    }

    public List<Double> getDataJarak() {
        return dataJarak;
    }

    public void setDataJarak(List<Double> dataJarak) {
        this.dataJarak = dataJarak;
    }

    public List<Double> getDataIR() {
        return dataIR;
    }

    public void setDataIR(List<Double> dataIR) {
        this.dataIR = dataIR;
    }

    private final SimpMessagingTemplate simpMessagingTemplate;

    public MqttService(String uri, String topic, String clientId, SimpMessagingTemplate simpMessagingTemplate) throws MqttException, URISyntaxException {
        this(new URI(uri), topic, null, clientId, simpMessagingTemplate);
    }

    public MqttService(String uri, String[] topics, String clientId, SimpMessagingTemplate simpMessagingTemplate) throws MqttException, URISyntaxException {
        this(new URI(uri), null, topics, clientId, simpMessagingTemplate);
    }

    public MqttService(URI uri, String topic, String[] topics, String clientId, SimpMessagingTemplate simpMessagingTemplate) throws MqttException {
        this.simpMessagingTemplate = simpMessagingTemplate;
        String host = String.format("tcp://%s:%d", uri.getHost(), uri.getPort());
//        String[] auth = this.getAuth(uri); auth[0] dan auth[1];
        this.topic = topic;
        this.topics = topics;
        this.clientId = clientId;

//        if (!uri.getPath().isEmpty()) {
//            this.topic = uri.getPath().substring(1);
//        }

        MqttConnectOptions conOpt = new MqttConnectOptions();
        conOpt.setCleanSession(true);
        conOpt.setUserName(this.username);
        conOpt.setPassword(this.password.toCharArray());

        this.client = new MqttClient(host, this.clientId, new MemoryPersistence());
        this.client.setCallback(this);
        this.client.connect(conOpt);
        this.client.unsubscribe(this.topics);
        if (this.topic == null) {
            this.client.subscribe(this.topics);
        } else {
            this.client.subscribe(this.topic, qos);
        }
    }

    private String[] getAuth(URI uri) {
        String a = uri.getAuthority();
        String[] first = a.split("@");
        return first[0].split(":");
    }

    public void sendMessage(String payload) throws MqttException {
        MqttMessage message = new MqttMessage(payload.getBytes());
        message.setQos(qos);
        this.client.publish(this.topic, message); // Blocking publish
    }

    public void unsubscribe(String topic) throws MqttException {
        this.client.unsubscribe(topic);
        this.client.disconnect();
    }

    /**
     * @see MqttCallback#connectionLost(Throwable)
     */
    public void connectionLost(Throwable cause) {
        System.out.println("Connection lost because: " + cause);
//        System.exit(1);
    }

    /**
     * @see MqttCallback#deliveryComplete(IMqttDeliveryToken)
     */
    public void deliveryComplete(IMqttDeliveryToken token) {
    }

    /**
     * @see MqttCallback#messageArrived(String, MqttMessage)
     */
    public void messageArrived(String topic, MqttMessage message) throws MqttException {
        String messageStr = new String(message.getPayload());
//        System.out.printf("[%s] - [%s]: %s%n", topic, this.clientId, messageStr);
        this.setMessage(messageStr);

        try {
            if (topic.contains("pir")) {
                this.dataDevicePIR(messageStr);
            } else if (topic.contains("jarak")) {
                this.dataDeviceJarak(messageStr);
            } else if (topic.contains("ir")) {
                this.dataDeviceIR(messageStr);
            }
            if (this.isAllConditionTrue) this.sendMessage();
            else this.isAllConditionTrue = isIRDetection || (isIRDetection && isOutliersJarak);
        } catch (Exception err) {
            System.out.println(err);
        }

    }

    public void dataDeviceJarak(String messageStr) {
        double newData = Double.parseDouble(messageStr);
        if (this.getDataJarak().size() > 3) {
            Double[] dataCalc = this.calculateStats(this.getDataJarak());
            double std = Math.sqrt(dataCalc[1]);
            System.out.printf("Avg = %s, std = %s, data = %s%n", dataCalc[0], 3 * std, newData);
            if (Math.abs(newData - dataCalc[0]) > 3 * std) {
                System.out.printf("%s is outlier%n", newData);
                this.setOutliersJarak(true);
            } else {
                this.dataJarak.add(newData);
            }
            if (this.getDataJarak().size() > 50) this.setDataJarak(new ArrayList<>());
        } else {
            this.dataJarak.add(newData);
        }
    }

    public void dataDeviceIR(String messageStr) {
        double newData = Double.parseDouble(messageStr);
        this.dataIR.add(newData);
//        if (dataIR.size() > 1) {
            Double[] dataCalc = this.calculateStats(this.getDataIR());
            System.out.printf("Avg is: %s, ceil is: %s%n", dataCalc[0], Math.ceil(dataCalc[0]));
            if (Math.ceil(dataCalc[0]) > 0) {
                this.setIRDetection(true);
            }
//        }
    }

    public void dataDevicePIR(String messageStr) {
//        double newData = Double.parseDouble(messageStr);
        this.setPIRDetection(true);
        final String time = new SimpleDateFormat("HH:mm").format(new Date());
        this.simpMessagingTemplate.convertAndSend("/topic/pushmessages/" + this.clientId,
                new OutputMessage("Chuck Norris", "-1", time));
    }

    private Double[] calculateStats(List<Double> data) {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        double average = 0;
        double variance = 0;
        for (int i = 0; i < data.size(); i++) {
            double sample = data.get(i);
            if (sample > max) max = sample;
            if (sample < min) min = sample;
            average += sample;
            variance += sample * sample;
        }
        average = average / data.size();
        variance = variance / data.size() - average * average;
        Double[] dataCalc = {average, variance};
        return dataCalc;
    }

    public void sendMessage() {
        boolean isSampahMasuk = this.isAllConditionTrue;
        final String time = new SimpleDateFormat("HH:mm").format(new Date());
        this.simpMessagingTemplate.convertAndSend("/topic/pushmessages/" + this.clientId,
                new OutputMessage("Chuck Norris", isSampahMasuk ? "1" : "0", time));

//        this.simpMessagingTemplate.convertAndSendToUser(
//                this.clientId, "/queue/specific-user/" + this.clientId, new OutputMessage("Chuck Norris", isSampahMasuk ? "Masuk" : "Tidak Masuk", time));

        this.setAllConditionTrue(false);
        this.setIRDetection(false);
        this.setOutliersJarak(false);
        this.setDataIR(new ArrayList<>());
        this.setDataJarak(new ArrayList<>());
        this.setPIRDetection(false);
    }

}
