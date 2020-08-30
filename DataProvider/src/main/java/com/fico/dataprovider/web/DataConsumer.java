package com.fico.dataprovider.web;

import java.util.Locale;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.javafaker.Faker;

@Service
public class DataConsumer {

	@KafkaListener(id = "td", topics = "tongdun")
	@SendTo("tongdun_reply")
	public String onMessage(String request) {
	
		Faker faker = new Faker(Locale.CHINA);
		int anti_fraud_mode_score = faker.number().numberBetween(0, 100);
		int anti_fraud_score = faker.number().numberBetween(0, 25);
		int score = faker.number().numberBetween(0, 25);

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("anti_fraud_mode_score", anti_fraud_mode_score);
		jsonObject.put("anti_fraud_score", anti_fraud_score);
		jsonObject.put("third_party_score", score);
		String response = jsonObject.toJSONString();
		System.out.println("request:"+request);
		System.out.println("response:" + response);
		return response;
	}

	@KafkaListener(id = "br", topics = "bairong")
	@SendTo("bairong_reply")
	public String handleBairongRequest(String request) {
		Faker faker = new Faker(Locale.CHINA);

		double bigdata_score = faker.number().randomDouble(2, 300, 900);
		int income = faker.number().numberBetween(1, 99);
		double average_discount = faker.number().numberBetween(1, 99) / 100.0;
		int f_cabin_count = faker.number().numberBetween(0, 99);
		int c_cabin_count = faker.number().numberBetween(0, 99);
		int y_cabin_count = faker.number().numberBetween(0, 999);
		int foreign_count = faker.number().numberBetween(0, 999);
		double average_price = faker.number().randomDouble(2, 100, 10000);

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("bigdata_score", bigdata_score);
		jsonObject.put("income", income);
		jsonObject.put("average_discount", average_discount);
		jsonObject.put("f_cabin_count", f_cabin_count);
		jsonObject.put("c_cabin_count", c_cabin_count);
		jsonObject.put("y_cabin_count", y_cabin_count);
		jsonObject.put("foreign_count", foreign_count);
		jsonObject.put("average_price", average_price);
		
		System.out.println("bairong request:"+request);
		System.out.println("bairong response:" + jsonObject.toJSONString());
		return jsonObject.toJSONString();
	}
}
