package com.fico.blaze.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class KafkaService {

	public static JSONArray appInputList = new JSONArray();
	public static JSONArray appOutputList = new JSONArray();

	public static JSONArray tongdunInputList = new JSONArray();
	public static JSONArray tongdunOutputList = new JSONArray();

	public static JSONArray bairongInputList = new JSONArray();
	public static JSONArray bairongOutputList = new JSONArray();

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Resource
	private KafkaTemplate<String, String> kafkaTemplate;


	private Logger log = LoggerFactory.getLogger("KafkaService");

	/**
	 * 监听审批系统异步发送过来的JSON报文
	 * @param message JSON原始报文
	 * @throws Exception
	 */
	@KafkaListener( topics = "blaze-request")
    public void listen1(String message) throws Exception {
		appendToList(appInputList, message);
    }

    private void appendToList(JSONArray jsonArray, String data){
		JSONObject appInputEl = new JSONObject();
		appInputEl.put("creationDate", sdf.format(new Date()));
		appInputEl.put("content", data);
		jsonArray.add(appInputEl);
	}

	/**
	 * 模拟同盾外部征信，接收router发送过来的报文，随机返回是否为黑名单
	 * @param message
	 * @return
	 */
	@KafkaListener(topics = "InnerCreditCenter-blaze-send")
	@SendTo("InnerCreditCenter-blaze-receive")
	public String TongDunServer(String message) {
		System.out.println("TongDunServer ： Listen TongDun received..." + message);
		appendToList(tongdunInputList, message);
		int blazeRes = Math.random()>0.5?1:0;
		JSONObject messageJSON = JSON.parseObject(message);
		JSONObject res = new JSONObject();
		res.put("IsBlackList", blazeRes);
		res.put("creditDataName", "BaiRong");
		res.put("appId", messageJSON.getString("appId"));
		String rtn = res.toJSONString();
		appendToList(tongdunOutputList, rtn);
		return rtn;
	}

	/**
	 * 模拟百融外部征信，接收router发送过来的报文，随机返回是否为黑名单
	 * @param message
	 * @return
	 */
	//@KafkaListener(topics = "BaiRong", containerFactory ="baiRongContainerFactory")
	//@SendTo("REPLY_ASYN_MESSAGE_BaiRong")
	public String BaiRongServer(String message) {
		System.out.println("BaiRongServer ： Listen Bairong received..." + message);
		appendToList(bairongInputList, message);
		int blazeRes = Math.random()>0.5?1:0;
		JSONObject res = new JSONObject();
		res.put("IsBlackList", blazeRes);
		String rtn = res.toJSONString();
		appendToList(bairongOutputList, rtn);
		return rtn;
	}

	@KafkaListener( topics = "blaze-response")
    public void listen3(String message) {
		log.info("Blaze Response: [{}]", message);
		appendToList(appOutputList, message);
    }
	

}
