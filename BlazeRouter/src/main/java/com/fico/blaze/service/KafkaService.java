package com.fico.blaze.service;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.alibaba.fastjson.JSONObject;
import com.fico.blaze.model.DataProvider;
import com.fico.blaze.model.Message;

@Service
public class KafkaService {

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	@Autowired
	private BlazeService blazeService;
	
	@Autowired
	private DataProviderFactory  dataproviderFactory;
	
	
	private Logger log = LoggerFactory.getLogger("KafkaService");
	

	
	@KafkaListener(id = "001", topics = "blaze-request")
    public void listen1(String message) throws Exception {
		log.info("message content [{}]", message);
		


		JSONObject jsonObject = JSONObject.parseObject(message);
		jsonObject.put("CallType", "Call1");
		
		DataProvider dataProvider = dataproviderFactory.createDataProvider("Tongdun");
		Message msg = new Message(message);
		HashMap<String,String> meta=new HashMap<>();
		meta.put("topic", "Tongdun-request");
		msg.setMetadata(meta);
		msg.setMessage(message);
		
		String tongdunData = dataProvider.getData(msg);
		
		jsonObject.put("Tongdun", tongdunData);
		
		String blazeResponse = blazeService.invokeRuleService(jsonObject.toJSONString());
		log.info(blazeResponse);
		
		JSONObject blazeObj = JSONObject.parseObject(blazeResponse);
		String nextstep = blazeObj.getString("NextStep");
		
		
		if("blaze-response".equals(nextstep)) {
			sendMessage("blaze-response",blazeResponse);
		}else {
			sendMessage(nextstep,jsonObject.toJSONString());
		}
    }
	
	@KafkaListener(id = "002", topics = "Call2")
    public void listen2(String message) throws Exception {
		log.info("message content [{}]", message);
		JSONObject jsonObject = JSONObject.parseObject(message);
		jsonObject.put("CallType", "Call2");
		
		DataProvider dataProvider = dataproviderFactory.createDataProvider("Bairong");
		Message msg = new Message(message);
		String bairongData = dataProvider.getData(msg);
		
		jsonObject.put("Bairong", bairongData);
		
		String blazeResponse = blazeService.invokeRuleService(jsonObject.toJSONString());
		log.info(blazeResponse);
		
		JSONObject blazeObj = JSONObject.parseObject(blazeResponse);
		
		sendMessage("blaze-response",blazeResponse);
		
    }
	
	@KafkaListener(id = "003", topics = "blaze-response")
    public void listen3(String message) {
		log.info("Blaze Response: [{}]", message);
    }
	
	
	public void sendMessage(String topic, String message) throws InterruptedException {
		/**
		 * <p>
		 * SendResult:如果消息成功写入kafka就会返回一个RecordMetaData对象;result. getRecordMetadata()
		 * 他包含主题信息和分区信息，以及集成在分区里的偏移量。 查看RecordMetaData属性字段就知道了
		 * </p>
		 *
		 */

		ListenableFuture<SendResult<String, String>> send = kafkaTemplate.send(topic, message);
		send.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

			@Override
			public void onSuccess(SendResult<String, String> result) {
				log.info("async send message success partition [{}]", result.getRecordMetadata().partition());
				log.info("async send message success offest[{}]", result.getRecordMetadata().offset());
			}

			@Override
			public void onFailure(Throwable ex) {
				log.error("async send message fail [{}]", ex.getMessage());

			}
		});
		
	}
	

}
