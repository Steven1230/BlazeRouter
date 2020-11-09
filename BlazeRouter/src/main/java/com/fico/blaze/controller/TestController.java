package com.fico.blaze.controller;

import com.alibaba.fastjson.JSONObject;
import com.fico.blaze.service.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

//@RestController
//@CrossOrigin(origins = "*",maxAge = 3600)
public class TestController {

    @Autowired
    KafkaTemplate kafkaTemplate = null;

    /**
     * 接收审批系统报文并放入队列
     * @param jsonData 审批系统传来的申请报文
     * @return
     */
    @RequestMapping(value="/app/receive/",method= RequestMethod.POST)//写法与springMVC有点相似
    public String appSystemInputData(@RequestBody JSONObject jsonData){
        //JSONObject rawJsonData = JSON.parseObject(jsonData);
        //String rawBlazeRequestData = rawJsonData.getString("jsonData");
        kafkaTemplate.send("blaze-request", jsonData);
        return jsonData.toJSONString();
    }


    /**
     * 传回测试页面路由返回到审批系统的最终报文
     * @param id
     * @return
     */
    @RequestMapping(value="/app/getAppOutputList/",method= RequestMethod.POST)//写法与springMVC有点相似
    @ResponseBody
    public List getAppOutputList(String id){
        
        return KafkaService.appOutputList;
    }

    @RequestMapping(value="/app/getAppInputList/",method= RequestMethod.POST)//写法与springMVC有点相似
    @ResponseBody
    public List getAppInputList(String id){
        
        return KafkaService.appInputList;
    }

    @RequestMapping(value="/app/getTongdunInputList/",method= RequestMethod.POST)//写法与springMVC有点相似
    @ResponseBody
    public List getTongDunInputList(String id){
        
        return KafkaService.tongdunInputList;
    }

    @RequestMapping(value="/app/getTongdunOutputList/",method= RequestMethod.POST)//写法与springMVC有点相似
    @ResponseBody
    public List getTongDunOutputList(String id){
        
        return KafkaService.tongdunOutputList;
    }

    @RequestMapping(value="/app/getBairongInputList/",method= RequestMethod.POST)//写法与springMVC有点相似
    @ResponseBody
    public List getBaiRongInputList(String id){
        
        return KafkaService.bairongInputList;
    }

    @RequestMapping(value="/app/getBairongOutputList/",method= RequestMethod.POST)//写法与springMVC有点相似
    @ResponseBody
    public List getBaiRongOutputList(String id){
        
        return KafkaService.bairongOutputList;
    }
}
