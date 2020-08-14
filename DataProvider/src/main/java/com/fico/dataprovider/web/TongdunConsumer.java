package com.fico.dataprovider.web;

import java.util.Locale;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.javafaker.Faker;

@Service
public class TongdunConsumer {

	@KafkaListener(id = "tongdun", topics = "Tongdun-request")
	@SendTo
	public String onMessage(String request) {
		System.out.println("request:"+request);
		Faker faker = new Faker(Locale.CHINA);
		int anti_fraud_mode_score = faker.number().numberBetween(0, 100);
		int anti_fraud_score = faker.number().numberBetween(0, 25);
		int score = faker.number().numberBetween(0, 25);
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("anti_fraud_mode_score", anti_fraud_mode_score);
		jsonObject.put("anti_fraud_score", anti_fraud_score);
		jsonObject.put("third_party_score", score);
		String response = jsonObject.toJSONString();
		System.out.println("response:"+response);
		return response;
	}
}
