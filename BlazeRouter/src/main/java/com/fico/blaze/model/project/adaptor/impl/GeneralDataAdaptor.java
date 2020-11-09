package com.fico.blaze.model.project.adaptor.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fico.blaze.model.project.adaptor.IProjectDataAdaptor;
import org.springframework.stereotype.Service;

@Service
public class GeneralDataAdaptor implements IProjectDataAdaptor {

    @Override
    public JSONObject convertInputData(JSONObject jsonObject) {
        return add3rdCreditList( jsonObject ) ;
    }

    private JSONObject add3rdCreditList(JSONObject jsonObject){
        JSONObject outerSystemQueSummaryJSON = new JSONObject();
        jsonObject.put("OuterSystemQueSummary", outerSystemQueSummaryJSON);

        jsonObject.put("CallStage", 1);

        JSONArray outerSystemQueryDetailJSONArr = new JSONArray();
        JSONObject tongdunJSON = new JSONObject();
        tongdunJSON.put("Name", "DMP");
        tongdunJSON.put("Status", "0");

        JSONObject bairongJson = new JSONObject();
        bairongJson.put("Name", "AppSystem");
        bairongJson.put("Status", "0");

        outerSystemQueryDetailJSONArr.add(tongdunJSON);
        outerSystemQueryDetailJSONArr.add(bairongJson);

        outerSystemQueSummaryJSON.put("OuterSystemQueryDetail", outerSystemQueryDetailJSONArr);
        return jsonObject;
    }

    @Override
    public JSONObject convertOutputData(JSONObject jsonObject) {
        return null;
    }
}
