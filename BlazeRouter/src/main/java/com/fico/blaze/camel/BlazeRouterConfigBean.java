package com.fico.blaze.camel;

import com.alibaba.fastjson.JSONObject;
import com.fico.blaze.model.DataProvider;
import com.fico.blaze.model.DataProviderFactory;
import com.fico.blaze.model.project.ProjectExecutor;
import com.fico.blaze.model.project.ProjectFactory;
import com.fico.blaze.service.BlazeRouterCamelBuilder;
import com.fico.blaze.service.IRouterDataAdaptor;
import org.apache.camel.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BlazeRouterConfigBean {

    @Autowired
    private IRouterDataAdaptor routerDataAdaptor;

    @Autowired
    private DataProviderFactory dataProviderFactory;

    @Autowired
    private ProjectFactory projectFactory;

    @Value("${blaze.router.outerAppIDAttributeName}")
    private String outerAppIDAttributeName;

    @Value("${blaze.router.outerServiceIDAttributeName}")
    private String outerServiceIDAttributeName;

    public String[] afterCallingBlazeRecipients( String blazeOutputJSONStr, Exchange exchange){

        JSONObject tmpBlazeJsonResponse = JSONObject.parseObject(blazeOutputJSONStr);

        if(routerDataAdaptor.isBlazeResponseHasFinalDecision(tmpBlazeJsonResponse)){
            return new String[] {BlazeRouterCamelBuilder.BLAZE_ROUTER_END_URI};
        } else {
            String nextOperationName = routerDataAdaptor.getNextStepOperation( tmpBlazeJsonResponse );
            DataProvider dataProvider = dataProviderFactory.createDataProvider(nextOperationName);
            //DataProvider dataProvider = dataProviderFactory.getDataProvider().get(0);
            exchange.getIn().setHeader(BlazeRouterCamelBuilder.BLAZE_ROUTER_NEXT_CREDIT_DATA, dataProvider.getName());
            return new String[] { dataProvider.getInnerAggregateURI(), dataProvider.getInnerSendURI() };
        }
    }

    public String[] firstCallBlazeRecipients( JSONObject tmpBlazeJsonResponse){

        ProjectExecutor projectExecutor = getFirstCallingCreditDataCamelURIFromRequest(tmpBlazeJsonResponse);

        String firstCallingCreditData = projectExecutor.getFirstCallingCreditData();

        if(null == firstCallingCreditData || "".equals(firstCallingCreditData)){
            return new String[] {BlazeRouterCamelBuilder.BLAZE_ROUTER_INVOKE_BLAZE_URI};
        }

        DataProvider dataProvider = dataProviderFactory.createDataProvider(firstCallingCreditData);

        String sendURI = dataProvider.getSendURI();

        String innerURI = dataProvider.getInnerAggregateURI();

        return new String[] {innerURI, sendURI };
    }

    public String getAppID(JSONObject jsonObject){
        return jsonObject.getString(outerAppIDAttributeName);
    }

    public String getCreditDataAppID(JSONObject jsonObject){
        return jsonObject.getString(outerServiceIDAttributeName);
    }

    public ProjectExecutor getFirstCallingCreditDataCamelURIFromRequest(JSONObject jsonRequest){
        String projectName = jsonRequest.getString("productCode");
        try {
            ProjectExecutor projectExecutor = projectFactory.getProjectExecutor(projectName);
            return projectExecutor;
            //            String creditDataName = projectExecutor.getFirstCallingCreditData();
//            if(null != creditDataName && !"".equals(creditDataName)){
//                return dataProviderFactory.createDataProvider(creditDataName).getSendURI();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String generateCamelURI(DataProvider dataProvider){
        String dataProviderURI = dataProvider.getSendURI();
        System.out.println("sending : " + dataProviderURI);
        return dataProviderURI;
    }



}
