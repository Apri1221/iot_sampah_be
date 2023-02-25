package com.example.iotsampah;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAutoConfiguration
public class IotSampahApplication {
	public static void main(String[] args) {
		SpringApplication.run(IotSampahApplication.class, args);
	}

}
