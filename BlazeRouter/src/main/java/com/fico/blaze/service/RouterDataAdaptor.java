package com.fico.blaze.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class RouterDataAdaptor implements IRouterDataAdaptor {

    @Override
    public JSONObject convertInputData(String input) {
        JSONObject jsonObject = JSONObject.parseObject(input);
        jsonObject.put("CallStage", 1);
        return jsonObject;
    }

    @Override
    public String convertOutputData(JSONObject output) {
        return output.toJSONString();
    }

    @Override
    public String getProjectName(JSONObject jsonObject) {
        return jsonObject.getString("productCode");
    }

    @Override
    public String getNextStepOperation(JSONObject jsonObject) {
        JSONObject creditDataInfoJSON = jsonObject.getJSONObject("OuterSystemQueSummary");
        JSONArray jsonArray = creditDataInfoJSON.getJSONArray("OuterSystemQueryDetail");
        for(int i=0; i<jsonArray.size(); i++){
            JSONObject tmpOutterJSON = (JSONObject)jsonArray.get(i);
            String outerSystemName = tmpOutterJSON.getString("Name");
            if("1".equals(tmpOutterJSON.getString("Status"))  ){
                return outerSystemName;
            }
        }
        return null;
    }

    @Override
    public boolean isBlazeResponseHasFinalDecision(JSONObject jsonObject) {
        if(jsonObject.getJSONObject("DecisionResponse") != null && jsonObject.getJSONObject("DecisionResponse").getString("FinalDecision") != null){
            return true;
        }
        return false;
    }
}
