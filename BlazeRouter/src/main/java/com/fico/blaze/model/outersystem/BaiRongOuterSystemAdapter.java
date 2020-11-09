package com.fico.blaze.model.outersystem;

import com.alibaba.fastjson.JSONObject;
import com.fico.blaze.model.Message;

import java.util.HashMap;

public class BaiRongOuterSystemAdapter implements IOuterSystemAdapter {
    @Override
    public JSONObject assembleResponseData(JSONObject rawResponseData, JSONObject jsonObject) {

        JSONObject newCreditData = new JSONObject();

        newCreditData.put("Sl_Id_Bank_Lost", jsonObject.getString("IsBlackList"));

        rawResponseData.put("BIZ_BR", rawResponseData);

        rawResponseData.remove("DecisionResponse");

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
