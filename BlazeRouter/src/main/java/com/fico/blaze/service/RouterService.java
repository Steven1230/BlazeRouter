package com.fico.blaze.service;

import com.alibaba.fastjson.JSONObject;
import com.fico.blaze.com.fico.blaze.data.com.fico.blaze.data.service.BlazeDataService;
import com.fico.blaze.model.DataProvider;
import com.fico.blaze.model.com.fico.blaze.model.project.ProjectExecutor;
import com.fico.blaze.model.com.fico.blaze.model.project.ProjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rma.XMLJSONConverter;

/**
 * 核心调度逻辑从KafkaService中移动到这里
 */

@Service
public class RouterService {

    private Logger log = LoggerFactory.getLogger("RouterService");

    @Autowired
    private KafkaService kafkaService;

    @Autowired
    private ProjectFactory projectFactory;

    @Autowired
    private DataProviderFactory  dataproviderFactory;

    @Autowired
    private IRouterDataAdaptor routerDataAdaptor;

    @Autowired
    private BlazeDataService blazeDataService;

    private XMLJSONConverter xmljsonConverter;

    public String handleAppInput(String message){

        //审批系统输入报文的格式转换
        JSONObject jsonObject = routerDataAdaptor.convertInputData(message);

        //有些场景，客户可能需要在第一次调用blaze之前先调用某几个点三方数据平台
        JSONObject firstCallingJsonObject =  firstCallingOuterSystem(jsonObject);

        JSONObject tmpJsonObject = handleRouterProcess( firstCallingJsonObject );

        return routerDataAdaptor.convertOutputData( tmpJsonObject );
    }

    /**
     * 路由核心逻辑处理
     * @param jsonObject 经过初始加工后的Blaze输入报文
     * @return 最终决策结果
     */
    private JSONObject handleRouterProcess( JSONObject jsonObject ){

        String projectName = routerDataAdaptor.getProjectName(jsonObject);

        ProjectExecutor projectExecutor = null;

        JSONObject tmpBlazeJsonResponse = null;

        try {
            projectExecutor = projectFactory.getProjectExecutor(projectName);

            //第一次调用
            tmpBlazeJsonResponse  = invokeBlazeService(jsonObject, projectExecutor);

            //如果blaze没有得到最终返回结果（通过或者拒绝），就一直循环
            while( !routerDataAdaptor.isBlazeResponseHasFinalDecision( tmpBlazeJsonResponse ) ){

                //获取Blaze返回结果中的下一步指令
                String nextOperationName = routerDataAdaptor.getNextStepOperation( tmpBlazeJsonResponse );

                DataProvider dataProvider = dataproviderFactory.createDataProvider(nextOperationName);

                tmpBlazeJsonResponse = dataProvider.fetchAndAssembleData( tmpBlazeJsonResponse );

                tmpBlazeJsonResponse = updateNextStepFlag( tmpBlazeJsonResponse );

                tmpBlazeJsonResponse = invokeBlazeService(tmpBlazeJsonResponse, projectExecutor);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return tmpBlazeJsonResponse;
    }

    private JSONObject updateNextStepFlag(JSONObject jsonObject){
        int callStage = jsonObject.getInteger("CallStage");
        callStage = callStage + 1;
        jsonObject.put("CallStage", callStage);
        return jsonObject;
    }

    private JSONObject firstCallingOuterSystem(JSONObject jsonObject){
        return jsonObject;
    }

    private JSONObject invokeBlazeService(JSONObject jsonObject, ProjectExecutor projectExecutor) throws Exception{
        //json转换成xml
        String appID = jsonObject.getString( "appId" );
        String jsonInputString = jsonObject.toJSONString();
        String blazeJSONOutputString = projectExecutor.getBlazeService().invokeRuleServiceJSON( jsonInputString );

        blazeDataService.insertBlazeData( appID, jsonInputString, blazeJSONOutputString );

        return JSONObject.parseObject( blazeJSONOutputString );
    }
}
