package com.fico.blaze.camel;

import com.alibaba.fastjson.JSONObject;
import com.fico.blaze.model.DataProvider;
import com.fico.blaze.service.BlazeRouterCamelBuilder;
import com.fico.blaze.service.DataProviderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class FirstFetchCreditDataAggregationStrategy {

    @Autowired
    private DataProviderFactory dataProviderFactory;

    public JSONObject concat(JSONObject oldBody, Map oldHeaders,
                             JSONObject newBody, Map newHeaders) {

        if (newBody != null) {
            String newDataProviderName = (String)newHeaders.get(BlazeRouterCamelBuilder.BLAZE_ROUTER_NEXT_CREDIT_DATA);
            JSONObject oldBodyBlazeJSON = oldBody;
            JSONObject creditDataReturnJSON = newBody;
            oldHeaders.put(BlazeRouterCamelBuilder.BLAZE_ROUTER_STATE_HEADER, BlazeRouterCamelBuilder.BLAZE_ROUTER_STATE_FIRST_FETCH_CREDIT_DATA_COMPLETE);
            newHeaders.put(BlazeRouterCamelBuilder.BLAZE_ROUTER_STATE_HEADER, BlazeRouterCamelBuilder.BLAZE_ROUTER_STATE_FIRST_FETCH_CREDIT_DATA_COMPLETE);
            return combinData(oldBodyBlazeJSON, creditDataReturnJSON, newDataProviderName);
        } else {
            return oldBody;
        }
    }

    private JSONObject combinData(JSONObject oldBody, JSONObject newBody, String dataProviderName){

        DataProvider dataProvider = dataProviderFactory.createDataProvider(dataProviderName);

        return dataProvider.getOuterSystemAdapter().assembleResponseData(oldBody, newBody);

    }

}
