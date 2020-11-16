package com.fico.blaze.service;

import com.alibaba.fastjson.JSONObject;
import com.fico.blaze.camel.BlazeRouterNextStepRecipientsBean;
import com.fico.blaze.camel.FirstFetchCreditDataAggregationStrategy;
import com.fico.blaze.camel.RouterKafkaMessageOutputAdapter;
import com.fico.blaze.model.DataProvider;
import com.fico.blaze.model.DataProviderFactory;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.util.toolbox.AggregationStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BlazeRouterCamelBuilder extends RouteBuilder {

    public static final String BLAZE_ROUTER_INPUT_URI = "direct:blaze-router-inputHandler";

    public static final String BLAZE_ROUTER_INVOKE_BLAZE_URI = "direct:blaze-router-invoke-blaze";

    public static final String BLAZE_ROUTER_HANDLE_BLAZE_OUTPUT = "direct:blaze-router-handle-blaze-output";

    public static final String BLAZE_ROUTER_FETCH_CREDIT_DATA_FIRST = "direct:blaze-router-handle-first-fetch-data";

    public static final String BLAZE_ROUTER_NEXT_STEP_HEADER = "blaze-router-next-step";

    public static final String BLAZE_ROUTER_NEXT_CREDIT_DATA = "blaze-router-next-credit-date-step";

    public static final String BLAZE_ROUTER_END_URI = "direct:blaze-router-end";

    public static final String BLAZE_ROUTER_STATE_HEADER = "blaze-router-state";

    public static final String BLAZE_ROUTER_ID_HEADER = "blaze-router-id";

    public static final String BLAZE_ROUTER_STATE_INIT = "router-state-init";

    public static final String BLAZE_ROUTER_STATE_FIRST_FETCH_CREDIT_DATA_COMPLETE = "router-state-first-fetch-credit-date-complete";

    public static final String BLAZE_ROUTER_STATE_BEFORE_CALL_BLAZE = "calling blaze";

    public static final String BLAZE_ROUTER_STATE_CALL_BLAZE_COMPLETE = "calling blaze complete";

    @Autowired
    private DataProviderFactory dataProviderFactory;

    @Override
    public void configure() throws Exception {

        errorHandler(
                defaultErrorHandler()
                .maximumRedeliveries(3)
                .maximumRedeliveryDelay(100000)
        );

        //审批系统传入原始申请数据
        from("kafka:blaze-request?brokers=localhost:9092&groupId=1")
                .convertBodyTo(JSONObject.class)
                .setHeader(BLAZE_ROUTER_STATE_HEADER, constant(BLAZE_ROUTER_STATE_INIT))
                .setHeader(BLAZE_ROUTER_ID_HEADER, method(method(BlazeRouterNextStepRecipientsBean.class, "getAppID")))
                .bean("RouterMessageInputAdapter", "convertInputData")
                .to(BLAZE_ROUTER_FETCH_CREDIT_DATA_FIRST);

        //Todo:首先调用第三方数据源，怎么办，要调用多个怎么办
        from(BLAZE_ROUTER_FETCH_CREDIT_DATA_FIRST)
                .convertBodyTo(JSONObject.class)
                .setHeader(BLAZE_ROUTER_NEXT_STEP_HEADER, method(BlazeRouterNextStepRecipientsBean.class, "firstCallBlazeRecipients"))
                .recipientList( header(BLAZE_ROUTER_NEXT_STEP_HEADER) );


        for(DataProvider dataProvider : dataProviderFactory.getDataProvider()){
//            from(dataProvider.getReceiveURI())
//                    .setHeader(BLAZE_ROUTER_ID_HEADER, method(BlazeRouterNextStepRecipientsBean.class, "getCreditDataAppID"))
//                    .convertBodyTo(JSONObject.class)
//                    .to(dataProvider.getInnerAggregateURI());
            //异步：三方平台
            if(dataProvider.getAsync()){
                //异步发送
                //ToDO : 发送后超过多久没收到响应需要重发
                from(dataProvider.getInnerSendURI())
                        .bean("RouterCreditDataSendAdapter", "process")
                        .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                        .to(dataProvider.getAsyncSendUri())
                        .bean("RouterCreditDataSendAdapter", "checkAsyncSendHttpResponse");

                //异步接收
                from(dataProvider.getAsyncInnerReceiveURI())
                        .setHeader(BLAZE_ROUTER_ID_HEADER, method(method(BlazeRouterNextStepRecipientsBean.class, "getAppID")))
                        .bean("RouterCreditDataSendAdapter", "afterCallingAsync")
                        .to(dataProvider.getInnerAggregateURI());
            }else{
                from(dataProvider.getInnerSendURI())
                        .bean("RouterCreditDataSendAdapter", "process")
                        .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                        .to(dataProvider.getSendURI())
                        .bean("RouterCreditDataSendAdapter", "afterCallingHttp")
                        .to(dataProvider.getInnerAggregateURI());
            }
            from(dataProvider.getInnerAggregateURI())
                    .aggregate( header(BLAZE_ROUTER_ID_HEADER), AggregationStrategies.bean(FirstFetchCreditDataAggregationStrategy.class))
                    .completionSize(2)
                    //.completionPredicate( header(BLAZE_ROUTER_STATE_HEADER).isEqualTo(BLAZE_ROUTER_STATE_FIRST_FETCH_CREDIT_DATA_COMPLETE)  )
                    .to(BLAZE_ROUTER_INVOKE_BLAZE_URI);

        }

        //调用blaze
        from(BLAZE_ROUTER_INVOKE_BLAZE_URI).convertBodyTo(String.class)
                .setHeader(BLAZE_ROUTER_STATE_HEADER, constant(BLAZE_ROUTER_STATE_BEFORE_CALL_BLAZE))
                .bean("RouterService", "handleRouterProcess")
                .to(BLAZE_ROUTER_HANDLE_BLAZE_OUTPUT);

        from(BLAZE_ROUTER_HANDLE_BLAZE_OUTPUT)
                .setHeader(BLAZE_ROUTER_STATE_HEADER, constant(BLAZE_ROUTER_STATE_CALL_BLAZE_COMPLETE))
                .setHeader("CamelHttpMethod", constant("POST"))
                .setHeader(BLAZE_ROUTER_NEXT_STEP_HEADER, method(BlazeRouterNextStepRecipientsBean.class, "afterCallingBlazeRecipients"))
                .convertBodyTo(JSONObject.class)
                .recipientList( header(BLAZE_ROUTER_NEXT_STEP_HEADER) );

        from(BLAZE_ROUTER_END_URI)
                .process(new RouterKafkaMessageOutputAdapter())
                .setHeader(KafkaConstants.KEY, constant("Camel"))
                .to("kafka:blaze-response?brokers=localhost:9092");
    }
}
