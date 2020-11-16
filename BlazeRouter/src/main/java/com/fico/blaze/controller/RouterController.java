package com.fico.blaze.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*",maxAge = 3600)
public class RouterController {

    @Autowired
    KafkaTemplate kafkaTemplate = null;

    /**
     * 接收审批系统报文并放入队列
     * @param jsonData 审批系统传来的申请报文
     * @return
     */
    @RequestMapping(value="/blazeRouter/invokeBlazeService/",method= RequestMethod.POST)//写法与springMVC有点相似
    @ResponseBody
    public String appSystemInputData(@RequestBody String jsonData){
        JSONObject appResponseJson = new JSONObject();
        try{
            //JSONObject rawJsonData = new JSONObject(jsonData);

            JSONObject rawJsonData = JSON.parseObject(jsonData);

            //System.out.println( rawJsonData.toJSONString() );

            kafkaTemplate.send("blaze-request", rawJsonData.toJSONString());
        }catch (Exception e){
            e.printStackTrace();
            appResponseJson.put("succ", false);
        }
        appResponseJson.put("succ", true);
        return appResponseJson.toJSONString();
    }


    @RequestMapping(value="/blazeRouter/thirdPartnerAsyncResponse/",method= RequestMethod.POST)//写法与springMVC有点相似
    @ResponseBody
    public String thirdPartnerAsyncResponse(@RequestBody String jsonData){
        JSONObject appResponseJson = new JSONObject();
        try{
            //JSONObject rawJsonData = new JSONObject(jsonData);

            JSONObject rawJsonData = JSON.parseObject(jsonData);

            //System.out.println( rawJsonData.toJSONString() );

            kafkaTemplate.send("ThirdPartnerCredit-async-inner-blaze-receive", rawJsonData.toJSONString());
        }catch (Exception e){
            e.printStackTrace();
            appResponseJson.put("succ", false);
        }
        appResponseJson.put("succ", true);
        return appResponseJson.toJSONString();
    }
}
