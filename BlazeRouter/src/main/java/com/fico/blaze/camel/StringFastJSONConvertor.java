package com.fico.blaze.camel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.camel.Converter;
import org.apache.camel.Exchange;

@Converter
public class StringFastJSONConvertor {

    @Converter
    public static JSONObject toPurchaseOrder(String jsonStr,
                                             Exchange exchange){
        return JSON.parseObject(jsonStr);
    }

}
