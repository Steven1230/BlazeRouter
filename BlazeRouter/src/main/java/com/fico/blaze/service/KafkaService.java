package com.fico.blaze.service;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;

@Service
public class KafkaService {

	@Value("${tongdun.uri}")
	private String tongdunURI;
	
	@Value("${bairong.uri}")
	private String bairongURI;
	
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	@Autowired
	private BlazeService blazeService;
	
	
	private Logger log = LoggerFactory.getLogger("KafkaService");
	
	@KafkaListener(id = "001", topics = "blaze-request")
    public void listen1(String message) throws Exception {
		log.info("message content [{}]", message);
		JSONObject jsonObject = JSONObject.parseObject(message);
		jsonObject.put("CallType", "Call1");
		
		process("Tongdun",jsonObject);
		
    }
	
	@KafkaListener(id = "002", topics = "Call2")
    public void listen2(String message) throws Exception {
		log.info("message content [{}]", message);
		JSONObject jsonObject = JSONObject.parseObject(message);
		jsonObject.put("CallType", "Call2");
		process("Bairong",jsonObject);
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
	
	private void process(String dataProvider,JSONObject jsonObject) throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		//RequestEntity<String> request = null;
		
		RequestEntity<String> request = RequestEntity
			     .post(getURI(dataProvider))
			     .accept(MediaType.APPLICATION_JSON)
			     .body(jsonObject.toJSONString());
		ResponseEntity<String> response = restTemplate.exchange(request, String.class);
		
		String tongdunData = response.getBody();
		
		
		jsonObject.put(dataProvider, tongdunData);
		
		String blazeResponse = blazeService.invokeRuleService(jsonObject.toJSONString());
		log.info(blazeResponse);
		
		JSONObject blazeObj = JSONObject.parseObject(blazeResponse);
		String nextstep = blazeObj.getString("NextStep");
		
		log.info("NextStep:"+nextstep);
		if("blaze-response".equals(nextstep)) {
			sendMessage("blaze-response",blazeResponse);
		}else {
			sendMessage(nextstep,jsonObject.toJSONString());
		}
	}
	
	private URI getURI(String dataprovider) throws URISyntaxException {
		if("Tongdun".equals(dataprovider)) {
			return new URI(tongdunURI);
		}else if ("Bairong".equals(dataprovider)) {
			return new URI(bairongURI);
		}
		return null;
	}
}
