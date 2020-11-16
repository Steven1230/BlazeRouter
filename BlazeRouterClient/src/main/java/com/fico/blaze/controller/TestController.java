package com.fico.blaze.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fico.blaze.service.KafkaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*",maxAge = 3600)
public class TestController {

    @Autowired
    KafkaTemplate kafkaTemplate = null;

    private static List<JSONObject> dmpDataList = new ArrayList<>();

    private static List<JSONObject> creditServiceReceiveDataList = new ArrayList<>();

    public static List<JSONObject> creditServiceSendDataList = new ArrayList<>();

    private Logger log = LoggerFactory.getLogger("TestController");

    @RequestMapping(value="/app/innerCreditData/receive")//写法与springMVC有点相似
    public String simulateInner3rdDataSystem(@RequestBody String jsonData){
        JSONObject rawJsonData = JSON.parseObject(jsonData);
        String rawBlazeRequestData = rawJsonData.getString("jsonData");
        kafkaTemplate.send("blaze-request", rawBlazeRequestData);
        return rawBlazeRequestData;
    }


    /**
     * 接收审批系统报文并放入队列
     * @param jsonData 审批系统传来的申请报文
     * @return
     */
    @RequestMapping(value="/app/receive/",method= RequestMethod.POST)//写法与springMVC有点相似
    public String appSystemInputData(@RequestBody String jsonData){
        JSONObject rawJsonData = JSON.parseObject(jsonData);
        String rawBlazeRequestData = rawJsonData.getString("jsonData");
        kafkaTemplate.send("blaze-request", rawBlazeRequestData);
        return rawBlazeRequestData;
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

    @RequestMapping(value="/app/getDMPTempList/",method= RequestMethod.POST)//写法与springMVC有点相似
    @ResponseBody
    public List getDMPTempList(String id){
        return dmpDataList;
    }

    @RequestMapping(value="/app/getCreditTempReceiveList/",method= RequestMethod.POST)//写法与springMVC有点相似
    @ResponseBody
    public List getCreditTempReceiveList(String id){
        return creditServiceReceiveDataList;
    }

    @RequestMapping(value="/app/getCreditTempSendList/",method= RequestMethod.POST)//写法与springMVC有点相似
    @ResponseBody
    public List getCreditTempSendList(String id){
        return creditServiceSendDataList;
    }

    @RequestMapping(value="/dmp/getInfo/")//写法与springMVC有点相似
    @ResponseBody
    public String getDMPInfo(@RequestBody String dmpRequest){

        log.info("DMP Input Request: " + dmpRequest);

        JSONObject dmpJSONResponse = new JSONObject();

        dmpJSONResponse.put("afPhoneToIdnoCnt", 0);
        dmpJSONResponse.put("afIdnoToPhoneCnt", 0);
        dmpJSONResponse.put("afPhoneToSpousePhone", 0);
        dmpJSONResponse.put("afIdnoToSpousePhone", 0);
        dmpJSONResponse.put("afRelPhoneToIdno", 0);
        dmpJSONResponse.put("idNoToPhoneNo", 0);
        dmpJSONResponse.put("idNoToName", 0);
        dmpJSONResponse.put("namePhoneToIdNo", 0);
        dmpJSONResponse.put("custNameToIdNo", 0);
        dmpJSONResponse.put("phoneNoToIdNo", 0);
        dmpJSONResponse.put("threeMonthRefuse", 0);

        log.info("DMP Input Response: " + dmpRequest);

        String rtn = dmpJSONResponse.toJSONString();

        logTempData( dmpDataList, dmpRequest, rtn );

        return rtn;

    }


    public static void logTempData(List<JSONObject> tmpDataList, String input, String output){

        JSONObject tmpDMPData = new JSONObject();
        tmpDMPData.put("inputData" , input);
        tmpDMPData.put("outputData" , output);

        tmpDataList.add(tmpDMPData);
    }



    @RequestMapping(value="/app/clearTmpData/")
    //写法与springMVC有点相似
    @ResponseBody
    public String clearTmpData(@RequestBody String dmpRequest){

        dmpDataList.clear();
        creditServiceSendDataList.clear();
        creditServiceReceiveDataList.clear();
        KafkaService.appOutputList.clear();
        KafkaService.appInputList.clear();

        return "";
    }



    @RequestMapping(value="/appSystem/getInfo/")
    @ResponseBody
    public String getAppSystemData(@RequestBody String dmpRequest){

        JSONObject dmpJSONResponse = new JSONObject();

        dmpJSONResponse.put("AppTest", "AppTest");

        String rtn = dmpJSONResponse.toJSONString();

        return rtn;
    }


    @RequestMapping(value="/creditService/IC_IndReport_00900/")
    //写法与springMVC有点相似
    @ResponseBody
    public String creditService_IC_IndReport_00900(@RequestBody String dmpRequest){

        log.info("creditService_IC_IndReport_00900: " + dmpRequest);

        JSONObject dmpJSONResponse = new JSONObject();

        Thread creditServiceSimulator = new Thread(new CreditServiceSimulator(dmpRequest));

        creditServiceSimulator.start();

        dmpJSONResponse.put("msg", "请求成功");

        dmpJSONResponse.put("code", "0000");

        String rtn = dmpJSONResponse.toJSONString();

        logTempData( creditServiceReceiveDataList, dmpRequest, rtn );

        return rtn;
    }
}
