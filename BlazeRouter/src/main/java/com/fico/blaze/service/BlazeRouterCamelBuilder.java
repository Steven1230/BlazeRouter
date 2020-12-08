package com.fico.blaze.service;

import com.alibaba.fastjson.JSONObject;
import com.fico.blaze.camel.BlazeRouterConfigBean;
import com.fico.blaze.camel.FirstFetchCreditDataAggregationStrategy;
import com.fico.blaze.camel.RouterKafkaMessageOutputAdapter;
import com.fico.blaze.model.DataProvider;
import com.fico.blaze.model.DataProviderFactory;
import org.apache.camel.Exchange;
import org.apache.camel.builder.AggregationStrategies;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.spi.ExecutorServiceManager;
import org.apache.camel.spi.ThreadPoolProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.stereotype.Component;

@Component
public class BlazeRouterCamelBuilder extends RouteBuilder {

    public static final String BLAZE_ROUTER_INPUT_URI = "blaze-router-inputHandler";

    public static final String CAMEL_INNER_PROXY = "direct";

    public static final String CAMEL_INNER_PROXY_PREFIX = CAMEL_INNER_PROXY + ":";

    public static final String BLAZE_ROUTER_INVOKE_BLAZE_URI = CAMEL_INNER_PROXY_PREFIX + "blaze-router-invoke-blaze";

    public static final String BLAZE_ROUTER_HANDLE_BLAZE_OUTPUT = CAMEL_INNER_PROXY_PREFIX + "blaze-router-handle-blaze-output";

    public static final String BLAZE_ROUTER_FETCH_CREDIT_DATA_FIRST = CAMEL_INNER_PROXY_PREFIX + "blaze-router-handle-first-fetch-data";

    public static final String BLAZE_ROUTER_NEXT_STEP_HEADER = "blaze-router-next-step";

    public static final String BLAZE_ROUTER_NEXT_CREDIT_DATA = "blaze-router-next-credit-date-step";

    public static final String BLAZE_ROUTER_END_URI = CAMEL_INNER_PROXY_PREFIX + "blaze-router-end";

    public static final String BLAZE_ROUTER_STATE_HEADER = CAMEL_INNER_PROXY_PREFIX + "blaze-router-state";

    public static final String BLAZE_ROUTER_ID_HEADER = "blaze-router-id";

    public static final String BLAZE_ROUTER_STATE_INIT = "router-state-init";

    public static final String BLAZE_ROUTER_STATE_FIRST_FETCH_CREDIT_DATA_COMPLETE = "router-state-first-fetch-credit-date-complete";

    public static final String BLAZE_ROUTER_STATE_BEFORE_CALL_BLAZE = "calling blaze";

    public static final String BLAZE_ROUTER_STATE_CALL_BLAZE_COMPLETE = "calling blaze complete";

    @Autowired
    private DataProviderFactory dataProviderFactory;

    @Autowired
    private BlazeRouterConfigBean blazeRouterConfigBean;

    @Value("${blaze.router.maximumRedeliveries}")
    private Integer maximumRedeliveries;

    @Value("${blaze.router.maximumRedeliveryDelay}")
    private Integer maximumRedeliveryDelay;

    @Value("${blaze.router.maxPoolSize}")
    private Integer maxPoolSize;

    @Value("${blaze.router.routerErrorEndPoint}")
    private String routerErrorEndPoint;

    @Value("${blaze.router.routerInputEndPoint}")
    private String routerInputEndPoint;

    @Value("${blaze.router.routerOutputEndPoint}")
    private String routerOutputEndPoint;

    @Override
    public void configure() throws Exception {

        //设置最大并发数
        ExecutorServiceManager manager = this.getContext().getExecutorServiceManager();
        ThreadPoolProfile profile = manager.getDefaultThreadPoolProfile();
        profile.setMaxPoolSize( maxPoolSize );

        //当路由中发生异常时处理，应该还可以按不同的异常类型进行分类
//        onException(Exception.class)                 // 捕获所有异常
//                .handled(true)                       // 路由停止将错误信息返回给最初的消费者
//                .maximumRedeliveries( maximumRedeliveries )              // 路由尝试返还2次
//                .maximumRedeliveryDelay( maximumRedeliveryDelay )               // 每次返还间隔10秒
//                .retryAttemptedLogLevel( LoggingLevel.INFO )   // 返还时日志级别设为INFO
//                .retriesExhaustedLogLevel( LoggingLevel.INFO ) // 返还失败时日志级别设为INFO
//                .bean( "BlazeRouterErrorHandler", "process" ) //记录异常时路由以执行的轨迹
//                .to( routerErrorEndPoint ) //发送到路由异常队列
//                .end();

        //审批系统传入原始申请数据
        from( routerInputEndPoint )
                .convertBodyTo(JSONObject.class)
                .setHeader(BLAZE_ROUTER_STATE_HEADER, constant(BLAZE_ROUTER_STATE_INIT))
                .setHeader(BLAZE_ROUTER_ID_HEADER, method(method(BlazeRouterConfigBean.class, "getAppID")))
                .bean("RouterMessageInputAdapter", "convertInputData")
                .to(BLAZE_ROUTER_FETCH_CREDIT_DATA_FIRST);

//        from( routerInputEndPoint )
//                //.convertBodyTo(String.class)
//                .setBody(constant("123"))
//                .setHeader("dsURL",constant("datasource-url"))
//                .setHeader(KafkaHeaders.CORRELATION_ID, simple("876"))
//                .setHeader("dsURL",constant("datasource-url"))
//                .setHeader("dsURL1",constant("datasource-url"))
//                .setHeader("dsURL2",constant("datasource-url"))
//                .to("kafka:router-data-request?brokers=localhost:9092&groupId=1");


        //Todo:首先调用第三方数据源
        from(BLAZE_ROUTER_FETCH_CREDIT_DATA_FIRST)
                .convertBodyTo(JSONObject.class)
                .setHeader(BLAZE_ROUTER_NEXT_STEP_HEADER, method(BlazeRouterConfigBean.class, "firstCallBlazeRecipients"))
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
                        .setHeader("dsURL",constant("datasource-url"))
                        .setHeader(KafkaHeaders.CORRELATION_ID, simple("876"))
                        .setHeader("dsURL",constant("datasource-url"))
                        .setHeader("dsURL1",constant("datasource-url"))
                        .setHeader("dsURL2",constant("datasource-url"))
                        .bean("RouterCreditDataSendAdapter", "process")
                        //.setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                        //.setHeader(KafkaConstants.KEY, constant("application/json"))
                        .to(dataProvider.getAsyncSendUri())
                        .bean("RouterCreditDataSendAdapter", "checkAsyncSendHttpResponse");

                //异步接收
                //from(dataProvider.getAsyncInnerReceiveURI())
                from(dataProvider.getAsyncReceiveUri())
                        .setHeader(BLAZE_ROUTER_ID_HEADER, method(method(BlazeRouterConfigBean.class, "getAppID")))
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
                    //.aggregate( header(BLAZE_ROUTER_ID_HEADER), Bean(FirstFetchCreditDataAggregationStrategy.class))
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
                .setHeader(BLAZE_ROUTER_NEXT_STEP_HEADER, method(BlazeRouterConfigBean.class, "afterCallingBlazeRecipients"))
                .convertBodyTo(JSONObject.class)
                .recipientList( header(BLAZE_ROUTER_NEXT_STEP_HEADER) );

        from(BLAZE_ROUTER_END_URI)
                .process(new RouterKafkaMessageOutputAdapter())
                .setHeader(KafkaConstants.KEY, constant("Camel"))
                .to( routerOutputEndPoint )
                .end();
    }
}
