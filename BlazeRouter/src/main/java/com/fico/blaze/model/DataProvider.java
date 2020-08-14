package com.fico.blaze.model;

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


	public String getData(Message requestMessage) throws Exception {

		String response = getStrategy.getData(uri, requestMessage);

		return response;
	}

}
