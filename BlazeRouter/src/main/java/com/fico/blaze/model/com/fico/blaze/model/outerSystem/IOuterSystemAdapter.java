package com.fico.blaze.model.com.fico.blaze.model.outerSystem;

import com.alibaba.fastjson.JSONObject;

public interface IOuterSystemAdapter {

    //接收数据
    JSONObject assembleResponseData(String rawResponseData, JSONObject jsonObject);

    //发送数据
    Object assembleRequestData(JSONObject tmpBlazeData);
}