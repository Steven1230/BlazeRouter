package com.fico.blaze.service;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.stereotype.Component;

@Component
public class DataProvider {

	private String name;
	private String uri;
	private String type;
	private String async;

	private DataGetStrategy getStrategy;

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getAsync() {
		return async;
	}

	public void setAsync(String async) {
		this.async = async;
	}

	public DataGetStrategy getGetStrategy() {
		return getStrategy;
	}

	public void setGetStrategy(DataGetStrategy getStrategy) {
		this.getStrategy = getStrategy;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return this.name;
	}

	public String getType() {
		return this.type;
	}

	public void setDataGetStrategy(DataGetStrategy strategy) {
		this.getStrategy = strategy;
	}

	public String getData(String requestMessage) throws URISyntaxException {

		String response = getStrategy.getData(new URI(uri), requestMessage);

		return response;
	}

}
