package com.fico.blaze.camel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.kafka.KafkaConstants;
import org.springframework.stereotype.Component;

@Component
public class RouterKafkaMessageOutputAdapter implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        JSONObject jsonObject = JSON.parseObject( exchange.getIn().getBody().toString() );
        exchange.getIn().setHeader(KafkaConstants.KEY, jsonObject.getString("appId"));
        System.out.println("RouterKafkaMessageOutputAdapter:" + exchange.getIn().getBody().toString());
    }
}
