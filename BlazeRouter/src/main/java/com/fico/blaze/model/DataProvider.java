package com.fico.blaze.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fico.blaze.model.outersystem.IOuterSystemAdapter;

public class DataProvider {

	private static final String ADAPTOR_PACKAGE = "com.fico.blaze.model.outersystem";

	private String name;
	private String uri;
	private String type;
	private Boolean async;
	private String adapterName;
	private String strategyName;
	private String childData;
	private String asyncSendUri;
	private String asyncReceiveUri;

	public static final String TYPE_HTTP = "http";

    public static final String TYPE_KAFKA = "kafka";

	public String getStrategyName() {
		return strategyName;
	}

	public void setStrategyName(String strategyName) {
		this.strategyName = strategyName;
	}

	public IOuterSystemAdapter getOuterSystemAdapter() {
		if(outerSystemAdapter == null){
			buildAdapter();
		}
		return outerSystemAdapter;
	}

	private IOuterSystemAdapter outerSystemAdapter;

	private DataGetStrategy getStrategy;

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public Boolean getAsync() {
		return async;
	}

	public void setAsync(Boolean async) {
		this.async = async;
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

		return outerSystemAdapter.assembleResponseData(JSON.parseObject(responseRawData) , jsonObject );
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

	private String getCamelURI(){
	    if (TYPE_HTTP.equalsIgnoreCase( this.getType() )){
            return this.getUri();
        }else if( TYPE_KAFKA.equalsIgnoreCase( this.getType() ) ){
	        return TYPE_KAFKA;
        }
	    return null;
    }

	public String getSendURI(){
		//String rtn = getCamelURI()+ ":" + this.getName() + "-blaze-send?brokers=" + this.getUri().substring(7) + "&groupId=9";
		//return rtn;
		return this.getUri();
	}

	public String getInnerSendURI(){
		String rtn = "direct:" + this.getName() + "-inner-blaze-send";
		return rtn;
		//return getCamelURI();
	}

	public String getAsyncInnerReceiveURI(){
		String rtn = TYPE_KAFKA + ":" + this.getName() + "-async-inner-blaze-receive?brokers=" + this.getUri().substring(7) + "&groupId=9";
		return rtn;
	}

	public String getReceiveURI(){
		String rtn = getCamelURI()+ ":" + this.getName() + "-blaze-receive?brokers=" + this.getUri().substring(7) + "&groupId=9";
		return rtn;
		//return getCamelURI();
	}

	public String getInnerAggregateURI(){
		String rtn = "direct:creditData-" + this.getName() + "-inner-start";
		return rtn;
	}

	public String getChildData() {
		return childData;
	}

	public void setChildData(String childData) {
		this.childData = childData;
	}

	public String[] getChildDataList(){
		if(this.childData != null){
			return this.childData.split(",");
		}
		return null;
	}

	public String getAsyncSendUri() {
		return asyncSendUri;
	}

	public void setAsyncSendUri(String asyncSendUri) {
		this.asyncSendUri = asyncSendUri;
	}

	public String getAsyncReceiveUri() {
		return asyncReceiveUri;
	}

	public void setAsyncReceiveUri(String asyncReceiveUri) {
		this.asyncReceiveUri = asyncReceiveUri;
	}


}
