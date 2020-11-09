package com.fico.blaze.camel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fico.blaze.model.DataProvider;
import com.fico.blaze.service.BlazeRouterCamelBuilder;
import com.fico.blaze.service.DataProviderFactory;
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
        InputStream inputStream = (InputStream)exchange.getIn().getBody();

        byte[] bytes = new byte[inputStream.available()];

        inputStream.read(bytes);

        String dmpJSONResponseStr = new String(bytes);

        JSONObject dmpJSONResponse = JSON.parseObject(dmpJSONResponseStr);

        exchange.getIn().setBody(dmpJSONResponse);
    }
}
