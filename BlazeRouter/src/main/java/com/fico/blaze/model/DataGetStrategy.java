package com.fico.blaze.model;

public interface DataGetStrategy {

	
	public String getData(String uri,Message request,int timeout) throws Exception;
	
	public String getData(String uri,Message request) throws Exception;
	
}
