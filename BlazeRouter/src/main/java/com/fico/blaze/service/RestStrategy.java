package com.fico.blaze.service;

import java.net.URI;

import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.*;

public class RestStrategy implements DataGetStrategy {

	@Override
	public String getData(URI uri, String requestMessage) {
		
		RestTemplate restTemplate = new RestTemplate();
		
		org.springframework.http.
		RequestEntity<String> request = RequestEntity
			     .post(uri)
			     .accept(MediaType.APPLICATION_JSON)
			     .body(requestMessage);
		
		ResponseEntity<String> response = restTemplate.exchange(request, String.class);
		
		// TODO 异常处理
		
		return response.getBody();
	}
}
