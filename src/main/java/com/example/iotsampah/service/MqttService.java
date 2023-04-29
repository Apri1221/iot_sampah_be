package com.example.iotsampah.service;

import com.example.iotsampah.entity.MstUsers;
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

    private final MstUsersService mstUsersService;

    private final AuditLogService auditLogService;


    public MqttService(String uri, String topic, String clientId, SimpMessagingTemplate simpMessagingTemplate, MstUsersService mstUsersService, AuditLogService auditLogService) throws MqttException, URISyntaxException {
        this(new URI(uri), topic, null, clientId, simpMessagingTemplate, mstUsersService, auditLogService);
    }

    public MqttService(String uri, String[] topics, String clientId, SimpMessagingTemplate simpMessagingTemplate, MstUsersService mstUsersService, AuditLogService auditLogService) throws MqttException, URISyntaxException {
        this(new URI(uri), null, topics, clientId, simpMessagingTemplate, mstUsersService, auditLogService);
    }

    public MqttService(URI uri, String topic, String[] topics, String clientId, SimpMessagingTemplate simpMessagingTemplate, MstUsersService mstUsersService, AuditLogService auditLogService) throws MqttException {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.mstUsersService = mstUsersService;
        this.auditLogService = auditLogService;
        String host = String.format("tcp://%s:%d", uri.getHost(), uri.getPort());
//        String[] auth = this.getAuth(uri); auth[0] dan auth[1];
        this.topic = topic;
        this.topics = topics;
        this.clientId = clientId;

        MqttConnectOptions conOpt = new MqttConnectOptions();
        conOpt.setCleanSession(true);
        conOpt.setUserName(this.username);
        conOpt.setPassword(this.password.toCharArray());

        this.client = new MqttClient(host, this.clientId, new MemoryPersistence());
        this.client.setCallback(this);
        this.client.connect(conOpt);
        this.client.unsubscribe(this.topics);
        if (this.topic == null) this.client.subscribe(this.topics);
        else this.client.subscribe(this.topic, qos);
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
        this.setMessage(messageStr);

        try {
            if (topic.contains("pir")) {
                this.dataDevicePIR(messageStr);
            } else if (topic.contains("jarak")) {
                this.dataDeviceJarak(messageStr);
            } else if (topic.contains("ir")) {
                this.dataDeviceIR(messageStr);
            }
        } catch (Exception err) {
            System.out.println(err);
        } finally {
            this.sendMessage();
        }
    }

    public void dataDeviceJarak(String messageStr) {
        double newData = Double.parseDouble(messageStr);
        if (this.getDataJarak().size() > 10) {
            this.dataJarak.add(newData);
            Double[] dataCalc = this.calculateStats(this.getDataJarak());
            double std = Math.sqrt(dataCalc[1]);
            System.out.printf("Avg = %s, std = %s, data = %s%n", dataCalc[0], 3 * std, newData);
        } else {
            this.dataJarak.add(newData);
        }
    }

    public double getMeanJarak() {
        Double[] dataCalc = this.calculateStats(this.getDataJarak());
        return Math.sqrt(dataCalc[1]);
    }


    public void dataDeviceIR(String messageStr) {
        double newData = Double.parseDouble(messageStr);
        this.dataIR.add(newData);
        Double[] dataCalc = this.calculateStats(this.getDataIR());
        System.out.printf("Avg is: %s, ceil is: %s%n", dataCalc[0], Math.ceil(dataCalc[0]));
        if (Math.ceil(dataCalc[0]) > 0) {
            this.setIRDetection(true);
            final String time = new SimpleDateFormat("HH:mm").format(new Date());
            this.simpMessagingTemplate.convertAndSend("/topic/pushmessages/" + this.clientId,
                    new OutputMessage("Chuck Norris", "1", time));
            this.auditLog(1);
        }
    }

    public void dataDevicePIR(String messageStr) {
//        double newData = Double.parseDouble(messageStr);
        this.setPIRDetection(true);
        final String time = new SimpleDateFormat("HH:mm").format(new Date());
        this.simpMessagingTemplate.convertAndSend("/topic/pushmessages/" + this.clientId,
                new OutputMessage("Chuck Norris", "-1", time));
        this.auditLog(-1);
    }

    private Double[] calculateStats(List<Double> data) {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        double average = 0;
        double variance = 0;
        for (double sample : data) {
            if (sample > max) max = sample;
            if (sample < min) min = sample;
            average += sample;
            variance += sample * sample;
        }
        average = average / data.size();
        variance = variance / data.size() - average * average;
        return new Double[]{average, variance};
    }

    public void sendMessage() {
//        boolean isSampahMasuk = this.isAllConditionTrue;
//        final String time = new SimpleDateFormat("HH:mm").format(new Date());
//        this.simpMessagingTemplate.convertAndSend("/topic/pushmessages/" + this.clientId,
//                new OutputMessage("Chuck Norris", isSampahMasuk ? "1" : "0", time));

//        this.auditLog(isSampahMasuk ? 1 : 0);
//        this.simpMessagingTemplate.convertAndSendToUser(
//                this.clientId, "/queue/specific-user/" + this.clientId, new OutputMessage("Chuck Norris", isSampahMasuk ? "Masuk" : "Tidak Masuk", time));

        this.setAllConditionTrue(false);
        this.setIRDetection(false);
        this.setOutliersJarak(false);
        this.setDataIR(new ArrayList<>());
        this.setDataJarak(new ArrayList<>());
        this.setPIRDetection(false);
    }


    public void auditLog(Integer point) {
        String[] data = this.clientId.split("-");

        MstUsers mstUsers = mstUsersService.getUser(Integer.valueOf(data[2]));
        auditLogService.auditLogPoint(point, mstUsers);
    }
}
