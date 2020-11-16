package com.fico.blaze.model.project.adaptor.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fico.blaze.model.DataProvider;
import com.fico.blaze.model.DataProviderFactory;
import com.fico.blaze.model.project.adaptor.IProjectDataAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeneralDataAdaptor implements IProjectDataAdaptor {

    @Autowired
    private DataProviderFactory dataProviderFactory;

    @Override
    public JSONObject convertInputData(JSONObject jsonObject) {
        return add3rdCreditList( jsonObject ) ;
    }

    private JSONObject add3rdCreditList(JSONObject jsonObject){
        JSONObject outerSystemQueSummaryJSON = new JSONObject();
        jsonObject.put("OuterSystemQueSummary", outerSystemQueSummaryJSON);

        jsonObject.put("CallStage", 0);

        JSONArray outerSystemQueryDetailJSONArr = new JSONArray();

        for(DataProvider dataProvider : dataProviderFactory.getDataProvider() ){
            if(dataProvider.getChildDataList() != null && dataProvider.getChildDataList().length>0){
                for(String tmpChildData : dataProvider.getChildDataList()){
                    JSONObject tmpDataProviderJSON = new JSONObject();
                    tmpDataProviderJSON.put("Name", tmpChildData);
                    tmpDataProviderJSON.put("Status", "0");
                    outerSystemQueryDetailJSONArr.add(tmpDataProviderJSON);
                }
            }else{
                JSONObject tmpDataProviderJSON = new JSONObject();
                tmpDataProviderJSON.put("Name", dataProvider.getName());
                tmpDataProviderJSON.put("Status", "0");
                outerSystemQueryDetailJSONArr.add(tmpDataProviderJSON);
            }
        }

        outerSystemQueSummaryJSON.put("OuterSystemQueryDetail", outerSystemQueryDetailJSONArr);
        return jsonObject;
    }

    @Override
    public JSONObject convertOutputData(JSONObject jsonObject) {
        return null;
    }
}
