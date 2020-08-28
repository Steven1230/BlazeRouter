package com.fico.blaze.model;

import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;


public class KafkaStrategy implements DataGetStrategy{

	private ReplyingKafkaTemplate<String, String, String>  template;
	
	private final static int timeout=30;
	
	private Logger log = LoggerFactory.getLogger("KafkaStrategy");
	
	@Override
	public String getData(String uri, Message msg,int timeout) throws Exception {
		// TODO Auto-generated method stub
		String topic = msg.getMetadata().get("topic");
		String msgBody = msg.getMessage();
		ProducerRecord<String, String> record = new ProducerRecord<>(topic, msgBody);
		record.headers().add( new RecordHeader(KafkaHeaders.REPLY_TOPIC, "REPLY_ASYN_MESSAGE".getBytes()) );
		RequestReplyFuture<String, String, String> replyFuture = template.sendAndReceive(record);
		SendResult<String, String> sendResult = replyFuture.getSendFuture().get(90, TimeUnit.SECONDS);
		log.info("send ok..." + sendResult.getRecordMetadata());
		System.out.println("send ok..." + sendResult.getRecordMetadata());
		ConsumerRecord<String, String> consumerRecord = replyFuture.get(timeout, TimeUnit.SECONDS);
		log.info("return value ... " + consumerRecord.value());
		System.out.println("return value ... " + consumerRecord.value());
		return consumerRecord.value();
	}

	@Override
	public String getData(String uri, Message msg) throws Exception {
		// TODO Auto-generated method stub
		return getData(uri,msg,timeout);
	}

	public ReplyingKafkaTemplate<String, String, String> getTemplate() {
		return template;
	}

	public void setTemplate(ReplyingKafkaTemplate<String, String, String> template) {
		this.template = template;
	}
}
