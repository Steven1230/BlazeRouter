package com.fico.blaze.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="blaze.router")
public class DataProviderFactory {


	private List<DataProvider> dataProvider = null;
	
	private HashMap<String,DataProvider> providerList = null;
	
	
	
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
				dp.setDataGetStrategy(new RestStrategy());
			}
				
			providerList.put(dp.getName(), dp);
		}
	}
}
