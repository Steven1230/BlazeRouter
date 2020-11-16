package com.fico.blaze.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class DataProviderUtils {

	public static void updateOuterSystemDataState(JSONObject afterCombinedJSONData, String dataProviderName){
		JSONArray outerSystemQueryDetailList = afterCombinedJSONData.getJSONObject("OuterSystemQueSummary").getJSONArray("OuterSystemQueryDetail");

		for(int i=0; i<outerSystemQueryDetailList.size(); i++){
			JSONObject tmpJSONObject = (JSONObject)(outerSystemQueryDetailList.get(i));
			if( dataProviderName.equalsIgnoreCase(tmpJSONObject.getString("Name")) ){
				tmpJSONObject.put("Status", "2");
			}
		}
	}
}
