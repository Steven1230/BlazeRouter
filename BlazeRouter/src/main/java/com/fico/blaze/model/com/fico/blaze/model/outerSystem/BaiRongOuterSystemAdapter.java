package com.fico.blaze.model.com.fico.blaze.model.outerSystem;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fico.blaze.model.Message;

import java.util.HashMap;

public class BaiRongOuterSystemAdapter implements IOuterSystemAdapter {
    @Override
    public JSONObject assembleResponseData(String rawResponseData, JSONObject jsonObject) {
        JSONObject creditDataInfo = jsonObject.getJSONObject("CreditDataInfo");
        creditDataInfo.put("BaiRongDataInfo", JSON.parseObject(rawResponseData));

        return jsonObject;
    }

    @Override
    public Object assembleRequestData(JSONObject tmpBlazeData) {
        Message message = new Message();
        HashMap<String,String> meta=new HashMap<>();
		meta.put("topic", "BaiRong");
		message.setMetadata(meta);
		message.setMessage(tmpBlazeData.toJSONString());
        return message;
    }
}
