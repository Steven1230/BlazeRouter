package com.fico.blaze.camel;

import com.alibaba.fastjson.JSONObject;
import com.fico.blaze.model.project.ProjectExecutor;
import com.fico.blaze.model.project.ProjectFactory;
import com.fico.blaze.model.project.adaptor.IProjectDataAdaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 审批报文初始进入路由系统时初始化操作处理的Bean
 */

@Component("RouterMessageInputAdapter")
public class RouterMessageInputAdapter {

    @Autowired
    private ProjectFactory projectFactory;

    @Autowired
    private IProjectDataAdaptor generalDataAdaptor;

    /**
     *
     * @param jsonObject 初始审批JSON格式报文
     * @return 经过通用初始化加工和针对每个项目个性化加工的JSON Object
     * @throws Exception
     */
    public JSONObject convertInputData(JSONObject jsonObject) throws Exception {

        //先做些通用转换，比如所有Blaze项目，增加与路由交互的外部系统节点
        JSONObject generalDataAdaptorConvertedJSONObj = generalDataAdaptor.convertInputData(jsonObject);

        //对于不同的Blaze产品，做个性化的初始报文加工处理
        String productCode = generalDataAdaptorConvertedJSONObj.getString("productCode");

        ProjectExecutor projectExecutor = projectFactory.getProjectExecutor(productCode);

        JSONObject finalConvertedJSONObject = generalDataAdaptorConvertedJSONObj;

        if(projectExecutor.getProjectDataAdaptor() != null){
            finalConvertedJSONObject = projectExecutor.getProjectDataAdaptor().convertInputData( generalDataAdaptorConvertedJSONObj );
        }

        return finalConvertedJSONObject;
    }


}
