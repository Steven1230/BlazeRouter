package com.fico.blaze.model.outersystem;

import com.alibaba.fastjson.JSONObject;

public interface IOuterSystemAdapter {

    //接收数据
    JSONObject assembleResponseData(JSONObject rawResponseData, JSONObject jsonObject);

    //发送数据
    Object assembleRequestData(JSONObject tmpBlazeData);
}