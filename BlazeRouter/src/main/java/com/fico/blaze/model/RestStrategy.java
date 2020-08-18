package com.fico.blaze.model;

import java.net.URI;
import java.nio.charset.StandardCharsets;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RestStrategy implements DataGetStrategy {

	private final static int timeout=30;
	
	@Override
	public String getData(String uri, Message requestMessage,int timeout) throws Exception {
		
		RestTemplate restTemplate = new RestTemplate();
		
		RequestEntity<String> request = RequestEntity
			     .post(new URI(uri))
			     .accept(MediaType.APPLICATION_JSON,MediaType.APPLICATION_XML)
			     .acceptCharset(StandardCharsets.UTF_8)
			     .body(requestMessage.getMessage());
		
		ResponseEntity<String> response = restTemplate.exchange(request, String.class);
		
		// TODO 异常处理
		
		return response.getBody();
	}

	@Override
	public String getData(String uri, Message request) throws Exception {
		// TODO Auto-generated method stub
		return getData(uri,request,timeout);
	}
}
