package com.example.iotsampah;

import com.example.iotsampah.entity.MstItems;
import com.example.iotsampah.entity.MstSchools;
import com.example.iotsampah.service.MstItemsService;
import com.example.iotsampah.service.MstSchoolsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableScheduling
@EnableAutoConfiguration
public class IotSampahApplication {

	@Autowired
	MstItemsService mstItemsService;

	@Autowired
	MstSchoolsService mstSchoolsService;

	public static void main(String[] args) {
		SpringApplication.run(IotSampahApplication.class, args);
	}

	@PostConstruct
	void init() {
		MstSchools mstSchools = new MstSchools();
		mstSchools.setName("SD Mardiyuana Cicurug");
		mstSchools.setCode("2");
		mstSchools.setUrl("https://sdcicurug.mardiyuana.sch.id");
		MstSchools mstSchoolsFinal = mstSchoolsService.storeSchool(mstSchools);

		MstItems mstItems = new MstItems();
		mstItems.setCode("BTL");
		mstItems.setPrice(100);
		mstItems.setSchool(mstSchoolsFinal);
		mstItemsService.storeItem(mstItems);
	}

}
