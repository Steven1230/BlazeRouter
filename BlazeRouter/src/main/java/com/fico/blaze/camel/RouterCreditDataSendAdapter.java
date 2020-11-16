package com.fico.blaze.camel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fico.blaze.model.DataProvider;
import com.fico.blaze.model.DataProviderFactory;
import com.fico.blaze.service.BlazeRouterCamelBuilder;
import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Component("RouterCreditDataSendAdapter")
public class RouterCreditDataSendAdapter {

    @Autowired
    private DataProviderFactory dataProviderFactory;

    public void process(Exchange exchange) throws Exception {
        JSONObject jsonObject = (JSONObject) exchange.getIn().getBody();

        Object headerObj = exchange.getIn().getHeader(BlazeRouterCamelBuilder.BLAZE_ROUTER_NEXT_CREDIT_DATA);

        String nextStepValue = (String)exchange.getIn().getHeader(BlazeRouterCamelBuilder.BLAZE_ROUTER_NEXT_CREDIT_DATA);

        DataProvider dataProvider = dataProviderFactory.createDataProvider(nextStepValue);

        JSONObject toSendData = (JSONObject)dataProvider.getOuterSystemAdapter().assembleRequestData(jsonObject);

        exchange.getIn().setBody(  new ByteArrayInputStream(toSendData.toJSONString().getBytes()) );
    }

    public void afterCallingHttp(Exchange exchange) throws Exception {

        String dmpJSONResponseStr = getResponseDataStrFromExchange( exchange );

        JSONObject dmpJSONResponse = JSON.parseObject(dmpJSONResponseStr);

        exchange.getIn().setBody(dmpJSONResponse);
    }

    public void afterCallingAsync(Exchange exchange) throws Exception {
        String fromEndPointURI = exchange.getFromEndpoint().getEndpointUri();

        for (DataProvider dataProvider: dataProviderFactory.getDataProvider()){
            String tmp  = (String)exchange.getIn().getHeader("kafka.TOPIC");
            if(dataProvider.getAsync() && tmp != null && tmp.contains( dataProvider.getName() ) ){
                exchange.getIn().setHeader(BlazeRouterCamelBuilder.BLAZE_ROUTER_NEXT_CREDIT_DATA, dataProvider.getName() );
            }
        }

        System.out.println("");
    }

    public void checkAsyncSendHttpResponse( Exchange exchange ) throws Exception {
//        String dmpJSONResponseStr = (String)exchange.getIn().getBody();
//        JSONObject dmpJSONResponse = JSON.parseObject(dmpJSONResponseStr);
//        if(!"0000".equals( dmpJSONResponse.getString("code")) ){
//            throw new Exception("异步发送http报文，获取同步响应异常");
//        }
    }

    private String getResponseDataStrFromExchange(Exchange exchange) throws Exception {

        InputStream inputStream = (InputStream)exchange.getIn().getBody();

        byte[] bytes = new byte[inputStream.available()];

        inputStream.read(bytes);

        String dmpJSONResponseStr = new String(bytes);

        return dmpJSONResponseStr;
    }
}
