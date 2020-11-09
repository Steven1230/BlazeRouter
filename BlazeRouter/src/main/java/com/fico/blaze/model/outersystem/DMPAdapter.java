package com.fico.blaze.model.outersystem;

import com.alibaba.fastjson.JSONObject;
import com.fico.blaze.model.DataProviderUtils;

public class DMPAdapter implements IOuterSystemAdapter {

    @Override
    public JSONObject assembleResponseData(JSONObject jsonObject, JSONObject rawResponseData) {
        jsonObject.put("DMP", rawResponseData);
        DataProviderUtils.updateOuterSystemDataState(jsonObject,"DMP");
        return jsonObject;
    }

    @Override
    public Object assembleRequestData(JSONObject tmpBlazeResponseJSONData) {
        //TODO:先用application/json传输做测试，转化逻辑后续也得改，后续需要改成application/x-www-form-urlencoded
        JSONObject toSendDMPJSON = new JSONObject();

        toSendDMPJSON.put("idNo", tmpBlazeResponseJSONData.getString("idNumber"));
        toSendDMPJSON.put("phoneNo", tmpBlazeResponseJSONData.getString("accountMobile"));
        toSendDMPJSON.put("name", tmpBlazeResponseJSONData.getString("accountName"));
        toSendDMPJSON.put("birth", tmpBlazeResponseJSONData.getString("accountBirthdate"));
        toSendDMPJSON.put("type", tmpBlazeResponseJSONData.getString("productCode"));
        toSendDMPJSON.put("relPhoneNo", tmpBlazeResponseJSONData.getString("S_DC_VB_CONTACTMBLNO"));

        return toSendDMPJSON;
    }
}
