package com.fico.blaze.service;

import com.alibaba.fastjson.JSONObject;

public interface IRouterDataAdaptor {

    JSONObject convertInputData(String input);

    String convertOutputData(JSONObject output);

    String getProjectName(JSONObject jsonObject);

    String getNextStepOperation(JSONObject jsonObject);

    boolean isBlazeResponseHasFinalDecision(JSONObject jsonObject);


}
