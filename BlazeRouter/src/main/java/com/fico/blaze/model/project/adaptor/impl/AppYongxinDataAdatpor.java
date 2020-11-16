package com.fico.blaze.model.project.adaptor.impl;

import com.alibaba.fastjson.JSONObject;
import com.fico.blaze.model.project.adaptor.IProjectDataAdaptor;

public class AppYongxinDataAdatpor implements IProjectDataAdaptor {

    @Override
    public JSONObject convertInputData(JSONObject jsonObject) {
        return jsonObject;
    }

    @Override
    public JSONObject convertOutputData(JSONObject jsonObject) {
        return jsonObject;
    }
}
