package com.fico.blaze.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class CreditServiceSimulator implements Runnable {

    private Logger log = LoggerFactory.getLogger("CreditServiceSimulator");

    private JSONObject jsonInputStrObj = null;

    public CreditServiceSimulator(String jsonInputStr){
        this.jsonInputStrObj = JSON.parseObject(jsonInputStr);
    }

    @Override
    public void run()  {
        try {
            Thread.sleep(5000);

            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:6060/blazeRouter/thirdPartnerAsyncResponse/";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String tmpJSONStr = "{\"bairApplVeriScore\":[\"999\"],\"bairBllistPoint\":[\"999\"],\"bairCourtDetail\":[\"1\"],\"tongdBllistPoint\":[\"999\"],\"entryId\":[\"134654731432523441111\"],\"indctCollectId\":[\"IC_ExtDataAntiFraud_00804\"],\"channelNo\":[\"2\"]}";

            JSONObject rtnJSON = JSON.parseObject(tmpJSONStr);

            rtnJSON.put("entryId" , jsonInputStrObj.getString("entryId"));

            JSONObject json = restTemplate.postForEntity(url, rtnJSON, JSONObject.class).getBody();

            TestController.logTempData(TestController.creditServiceSendDataList, this.jsonInputStrObj.toJSONString(), tmpJSONStr);

            log.info( json.toJSONString() );

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
