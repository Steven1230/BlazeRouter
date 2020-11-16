package com.fico.blaze.model.outersystem;

import com.alibaba.fastjson.JSONObject;
import com.fico.blaze.model.DataProviderUtils;

public class AppSystemAdapter implements IOuterSystemAdapter {

    @Override
    public JSONObject assembleResponseData(JSONObject jsonObject, JSONObject rawResponseData) {
        jsonObject.put("AppSystem", rawResponseData);
        DataProviderUtils.updateOuterSystemDataState(jsonObject,"AppSystem");
        return jsonObject;
    }

    @Override
    public Object assembleRequestData(JSONObject tmpBlazeResponseJSONData) {
        //TODO:先用application/json传输做测试，转化逻辑后续也得改，后续需要改成application/x-www-form-urlencoded
        JSONObject toSendDMPJSON = new JSONObject();

        toSendDMPJSON.put("AppSystemRequest", "AppSystemRequest");

        return toSendDMPJSON;
    }
}
