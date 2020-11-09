package com.fico.blaze.camel;

import com.alibaba.fastjson.JSONObject;
import com.fico.blaze.model.project.ProjectExecutor;
import com.fico.blaze.model.project.ProjectFactory;
import com.fico.blaze.model.project.adaptor.IProjectDataAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("RouterMessageInputAdapter")
public class RouterMessageInputAdapter {

    @Autowired
    private ProjectFactory projectFactory;

    @Autowired
    private IProjectDataAdaptor generalDataAdaptor;

    public JSONObject convertInputData(JSONObject jsonObject) throws Exception {
        //JSONObject jsonObject = (JSONObject)exchange.getIn().getBody();

        JSONObject generalDataAdaptorConvertedJSONObj = generalDataAdaptor.convertInputData(jsonObject);

        String productCode = generalDataAdaptorConvertedJSONObj.getString("productCode");

        ProjectExecutor projectExecutor = projectFactory.getProjectExecutor(productCode);

        JSONObject finalConvertedJSONObject = generalDataAdaptorConvertedJSONObj;

        if(projectExecutor.getProjectDataAdaptor() != null){
            finalConvertedJSONObject = projectExecutor.getProjectDataAdaptor().convertInputData( generalDataAdaptorConvertedJSONObj );
        }

        return finalConvertedJSONObject;
    }


}
