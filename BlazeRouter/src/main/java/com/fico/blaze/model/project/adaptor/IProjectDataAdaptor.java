package com.fico.blaze.model.project.adaptor;

import com.alibaba.fastjson.JSONObject;

public interface IProjectDataAdaptor {

    public JSONObject convertInputData( JSONObject jsonObject );

    public JSONObject convertOutputData( JSONObject jsonObject );
}
