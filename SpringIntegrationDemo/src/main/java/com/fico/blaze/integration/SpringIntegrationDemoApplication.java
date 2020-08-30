package com.fico.blaze.integration;

import java.util.concurrent.TimeUnit;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.SendTo;

@SpringBootApplication
public class SpringIntegrationDemoApplication implements ApplicationRunner{

	@Autowired
	private ReplyingKafkaTemplate<String,String,String>  template;
	
	private Logger log = LoggerFactory.getLogger(SpringIntegrationDemoApplication.class);
	
	public static void main(String[] args) {
		SpringApplication.run(SpringIntegrationDemoApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		// TODO Auto-generated method stub


		ProducerRecord<String, String> record = new ProducerRecord<>("tongdun", "{appid:\"aa\"}");
		
		record.headers().add( new RecordHeader(KafkaHeaders.REPLY_TOPIC, "tongdun_reply".getBytes()) );
		RequestReplyFuture<String, String, String> replyFuture = template.sendAndReceive(record);
		SendResult<String, String> sendResult = replyFuture.getSendFuture().get(90, TimeUnit.SECONDS);
		log.info("send tongdun ok..." + sendResult.getRecordMetadata());
		ConsumerRecord<String, String> consumerRecord = replyFuture.get();
		log.info("return tongdun result ... " + consumerRecord.value());

		
		ProducerRecord<String, String> record2 = new ProducerRecord<>("bairong", "{appid:\"bb\"}");
		record2.headers().add( new RecordHeader(KafkaHeaders.REPLY_TOPIC, "bairong_reply".getBytes()) );
		RequestReplyFuture<String, String, String> replyFuture2 = template.sendAndReceive(record2);
		SendResult<String, String> sendResult2 = replyFuture2.getSendFuture().get(90, TimeUnit.SECONDS);
		log.info("send bairong ok..." + sendResult2.getRecordMetadata());
		ConsumerRecord<String, String> consumerRecord2 = replyFuture2.get();
		log.info("return bairong data ... " + consumerRecord2.value());
		
	}

}
