package com.fico.blaze.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.fico.blaze.model.DataProvider;
import com.fico.blaze.model.KafkaStrategy;
import com.fico.blaze.model.RestStrategy;

@Component
@ConfigurationProperties(prefix="blaze.router")
public class DataProviderFactory {


	private List<DataProvider> dataProvider = null;
	
	private HashMap<String,DataProvider> providerList = null;
	
	@Autowired
	private KafkaStrategy kafkaStrategy;
	
	
	public List<DataProvider> getDataProvider() {
		return dataProvider;
	}

	public void setDataProvider(List<DataProvider> dataProvider) {
		this.dataProvider = dataProvider;
	}

	public HashMap<String, DataProvider> getProviderList() {
		return providerList;
	}

	public void setProviderList(HashMap<String, DataProvider> providerList) {
		this.providerList = providerList;
	}

	
	public  DataProvider createDataProvider(String name) {
		if(providerList==null) {
			providerList = new HashMap<String,DataProvider>();
			initDataProvider();
		}
		return providerList.get(name);
	}

	private void initDataProvider() {
		for(DataProvider dp:dataProvider) {
			if("rest".equals(dp.getType())) {
				dp.setGetStrategy(new RestStrategy());
			}else if("kafka".equals(dp.getType())) {
				dp.setGetStrategy(kafkaStrategy);
			}
			providerList.put(dp.getName(), dp);
		}
	}
	

}
