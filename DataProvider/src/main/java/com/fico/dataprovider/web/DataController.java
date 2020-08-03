package com.fico.dataprovider.web;

import java.util.Locale;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.github.javafaker.Faker;

@RestController
public class DataController {

	@RequestMapping(value = "/getBairongData", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	/*
	 * bigdata_score double 互联网大数评分
	 * income string 收入等级 01~-99 
	 * average_discount double 1年内平均折扣 如：0.3 
	 * f_cabin_count Integer 商务舱乘机次数 如：2
	 * c_cabin_count Integer 公务舱乘机次数 如：0 
	 * y_cabin_count Integer 经济舱乘机次数 如：3 
	 * foreign_count Integer 国外飞行次数 如：0 
	 * average_price double 平均票价 如：738 
	 * last_fly_date String 最后飞行时间 如：2017年1月22日
	 * fly_total_milage Integer 总共飞行旅程数 如：2864km 
	 * flag_applyloanstr string 百融申请核查标志 1有；0无 
	 * als_m12_id_nbank_orgnum Integer 百融申请核查次数
	 * 
	 */
	public String getBairongData(@RequestBody String inputText) {

		Faker faker = new Faker(Locale.CHINA);
		
		double  bigdata_score = faker.number().randomDouble(2, 300, 900);
		int income = faker.number().numberBetween(1, 99);
		double average_discount = faker.number().numberBetween(1, 99)/100.0;
		int f_cabin_count = faker.number().numberBetween(0,99);
		int c_cabin_count = faker.number().numberBetween(0,99);
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
		return jsonObject.toJSONString();
	}
	
	@RequestMapping(value = "/getTongdunData", method = RequestMethod.POST, produces = {
			"application/json;charset=UTF-8" })
	@ResponseBody
	/*
	 * anti_fraud_mode_score integer [0,100]  反欺诈模型分
	 * anti_fraud_score integer[0,100] 反欺诈规则分 
	 * third_party_score integer [0,100]  三方规则分
	 */
	public String getTongdunData(@RequestBody String inputText) {
		Faker faker = new Faker(Locale.CHINA);
		int anti_fraud_mode_score = faker.number().numberBetween(0, 100);
		int anti_fraud_score = faker.number().numberBetween(0, 25);
		int score = faker.number().numberBetween(0, 25);
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("anti_fraud_mode_score", anti_fraud_mode_score);
		jsonObject.put("anti_fraud_score", anti_fraud_score);
		jsonObject.put("third_party_score", score);
		return jsonObject.toJSONString();
	}
}
