package com.fico.blaze.model;

import com.alibaba.fastjson.JSONObject;
import com.fico.blaze.model.com.fico.blaze.model.outerSystem.IOuterSystemAdapter;

public class DataProvider {

	private static final String ADAPTOR_PACKAGE = "com.fico.blaze.model.com.fico.blaze.model.outerSystem";

	private String name;
	private String uri;
	private String type;
	private String async;
	private String adapterName;
	private String strategyName;

	public String getStrategyName() {
		return strategyName;
	}

	public void setStrategyName(String strategyName) {
		this.strategyName = strategyName;
	}

	private IOuterSystemAdapter outerSystemAdapter;

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

	public String getAdapterName() {
		return adapterName;
	}

	public void setAdapterName(String adapterName) {
		this.adapterName = adapterName;
	}

	public String getData(Message requestMessage) throws Exception {

		String response = getStrategy.getData(uri, requestMessage);

		return response;
	}

	public JSONObject fetchAndAssembleData(JSONObject jsonObject) throws Exception {
		if(outerSystemAdapter == null){
			buildAdapter();
		}
		//根据输入报文组装调用第三方平台接口的数据
		Message msg = (Message)outerSystemAdapter.assembleRequestData(jsonObject);

		String responseRawData = getData(msg);

		return outerSystemAdapter.assembleResponseData( responseRawData, jsonObject );
	}

	private void buildAdapter(){
		Object adapterIns = null;
		try {
			Class adapterClsName = Class.forName(ADAPTOR_PACKAGE + "." + this.adapterName);
			adapterIns = adapterClsName.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}

		this.outerSystemAdapter = (IOuterSystemAdapter)adapterIns;
	}

}
