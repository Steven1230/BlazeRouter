package com.fico.blaze.camel;

import com.alibaba.fastjson.JSONObject;
import org.apache.camel.Exchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("BlazeRouterErrorHandler")
public class BlazeRouterErrorHandler {

    private Logger log = LoggerFactory.getLogger("BlazeRouterErrorHandler");

    //错误日志发送到Kafka异常队列

    public void process(Exchange exchange) throws Exception {
        JSONObject errorData = (JSONObject)exchange.getIn().getBody();
        exchange.getIn().getHeaders();
        Exception e = exchange.getProperty(Exchange.EXCEPTION_CAUGHT,Exception.class);
        if(null != e){
            log.info("++++++++++++++++++++++++++++++++++++++");
            log.info("[路由]errorMSG=" + e.getMessage());
            log.info("++++++++++++++++++++++++++++++++++++++");
            e.printStackTrace();
        }else{
            log.info("++++++++++++++++++++++++++++++++++++++");
            log.info("[路由][TO]exchangeId=" + exchange.getExchangeId() + "|fromRouteId=" +exchange.getFromRouteId());
        }

    }
}
