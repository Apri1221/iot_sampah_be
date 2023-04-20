package com.example.iotsampah.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WebClientService {

    @Value("${mst.url.datacenter}")
    private String urlDataCenter;

    Map<String, Object> resolveDataSchool(String idSchool) {
        WebClient client = WebClient.builder().baseUrl(urlDataCenter).build();
        Map<String, Object> clientResponse = client.get().uri(String.format("/api/school/%s", idSchool)).retrieve().bodyToMono(Map.class).block();
        return clientResponse;
    }

    Map<String, Object> resolveDataStudent(String url, String nis) throws JsonProcessingException {
        WebClient client = WebClient.builder().baseUrl(String.format("%s", url)).build();
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        String clientResponse = client.get().uri(String.format("/api/student?nis=%s", nis)).retrieve().bodyToMono(String.class).block();
        List<Map<String, Object>> dataStudents = mapper.readValue(clientResponse, typeFactory.constructCollectionType(List.class, Map.class));
        return dataStudents.get(0);
    }

    public Integer getBalanceStudent(String url, Integer studentId) {
        WebClient client = WebClient.builder().baseUrl(String.format("%s", url)).build();
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        Map<String, Map<String, Map<String, Object>>> clientResponse = client.get().uri(String.format("/api/saldo/%s", studentId)).retrieve().bodyToMono(Map.class).block();
        Map<String, Map<String, Object>> data = clientResponse.get("data");
        Map<String, Object> saldo = data.get("saldo");
        if (saldo == null) return 0;
        return Integer.valueOf(saldo.get("balance_cash").toString());
    }

    boolean updateBalanceStudent(String url, Integer studentId, Integer saldo) {
        WebClient client = WebClient.builder().baseUrl(String.format("%s", url)).build();
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        Map<String, Integer> data = new HashMap<>();
        data.put("value", saldo);
        try {
            Map<String, Object> clientResponse = client.put().uri(String.format("/api/add/saldo/%s", studentId)).body(BodyInserters.fromValue(data)).retrieve().bodyToMono(Map.class).block();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
