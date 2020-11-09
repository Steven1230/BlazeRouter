package com.fico.blaze.model.outersystem;

import com.alibaba.fastjson.JSONObject;
import com.fico.blaze.model.Message;

import java.util.HashMap;

public class TongDunOuterSystemAdapter implements IOuterSystemAdapter {
    @Override
    public JSONObject assembleResponseData(JSONObject rawResponseData, JSONObject jsonObject) {
        JSONObject creditDataInfo = jsonObject.getJSONObject("CreditDataInfo");
        creditDataInfo.put("TongDunDataInfo", rawResponseData);

        return jsonObject;
    }

    @Override
    public Object assembleRequestData(JSONObject tmpBlazeData) {
        Message message = new Message();
        HashMap<String,String> meta=new HashMap<>();
		meta.put("topic", "TongDun");
		message.setMetadata(meta);
		message.setMessage(tmpBlazeData.toJSONString());
        return message;
    }
}
