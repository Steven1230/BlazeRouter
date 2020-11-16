package com.fico.blaze.camel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fico.blaze.model.DataProvider;
import com.fico.blaze.model.DataProviderFactory;
import com.fico.blaze.service.BlazeRouterCamelBuilder;
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
            //oldHeaders.remove(BlazeRouterCamelBuilder.BLAZE_ROUTER_ID_HEADER);
            //newHeaders.remove(BlazeRouterCamelBuilder.BLAZE_ROUTER_ID_HEADER);
            return combinData(oldBodyBlazeJSON, creditDataReturnJSON, newDataProviderName);
        } else {
            return oldBody;
        }
    }

    private JSONObject combinData(JSONObject oldBody, JSONObject newBody, String dataProviderName){

        DataProvider dataProvider = dataProviderFactory.createDataProvider(dataProviderName);

        JSONObject afterCombinedJSONObj = dataProvider.getOuterSystemAdapter().assembleResponseData(oldBody, newBody);

        updateQueryDetailStatus(afterCombinedJSONObj, dataProviderName);

        return afterCombinedJSONObj;
    }

    private void updateQueryDetailStatus( JSONObject oldBody, String dataProviderName ){
        JSONArray outerSystemQueryDetailArr = oldBody.getJSONObject("OuterSystemQueSummary").getJSONArray("OuterSystemQueryDetail");
        for(int i=0; i<outerSystemQueryDetailArr.size(); i++){
            JSONObject tmpJSON = (JSONObject)outerSystemQueryDetailArr.get(i);
            if( tmpJSON.getString("Name").equals(dataProviderName) ){
                tmpJSON.put("Status", "2");
            }
        }
    }

}
