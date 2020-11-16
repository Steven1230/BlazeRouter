package com.fico.blaze.router;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class TestXML {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
//		SAXReader reader = new SAXReader();
//		StringReader sr = new StringReader("<Application></Application>");
//		Document doc = reader.read(sr);
//
//		Element root =doc.getRootElement();
//
//		//root.addAttribute("DecisionArea", "01");
//
//		root.addAttribute("DecisionArea", "01");
//		root.addAttribute("DecisionArea", "02");
//		StringWriter sw=new StringWriter();
//		doc.write(sw);
//
//		System.out.println(sw.toString());
		String a = "{\"productCode\":\"Ant\",\n" +
				"\t\"appId\":\"123\",\n" +
				"\t\"CallStage\":\"1\",\n" +
				"\t\"CreditDataInfo\":{\n" +
				"\t\"DataInfoQueryDetail\":\n" +
				"\t[{\"Name\":\"TongDun\", \"Status\":\"0\"},{\"Name\":\"BaiRong\", \"Status\":\"0\"}]\n" +
				"\t}\n" +
				"\t}";

		String sss = "{\"addressCode\":\"510000510300510322\", \"liveRmk\":\"其他\", \"ifcarnoblacklist\":\"D\", \"entryOccurTime\":\"2020-10-19 07:42:02\", \"dueDay\":\"\", \"loanFreq\":\"1M\", \"loanCcy\":\"CNY\", \"dueDayOpt\":\"1\", \"maritalSta\":\"90\", \"pptyLive\":\"1\", \"applAcTyp1\":\"01\", \"indivPosition\":\"9\", \"bankCardName\":\"赵兵\", \"typVer\":\"1\", \"ifdwaddressblacklist\":\"D\", \"ifipblacklist\":\"D\", \"appId\":\"22\", \"purpose\":\"OTH\", \"applAcKind\":\"02\", \"ContactAddressProvince\":\"510000\", \"dueDt\":\"\", \"firstPayDate\":\"\", \"appOrigin\":\"22\", \"companyName\":\"null\", \"docChannel\":\"XSTY\", \"entryId\":\"1022202010190001270\", \"zipCode\":\"\", \"accountMobile\":\"18582025751\", \"applAcKind1\":\"01\", \"loanKindCode\":\"Z-PPD-06\", \"ifliveaddressblacklist\":\"N\", \"indivOccu\":\"Y\", \"idExpDt\":\"20271130\", \"companyAddress\":\"null\", \"liveAddressProvince\":\"510000\", \"liveInfo\":\"99\", \"bankMblNo\":\"18582025751\", \"contactRelation\":\"child\", \"ifqqblacklist\":\"D\", \"applyTnrTyp\":\"6\", \"idNumber\":\"510322199112185819\", \"ifaddressblacklist\":\"D\", \"contactName1\":\"\", \"mtdCde\":\"SYS005\", \"nation\":\"\", \"contactName2\":\"\", \"contactName3\":\"\", \"ifmailblacklist\":\"D\", \"contactName4\":\"\", \"contactName\":\"肖鹏\", \"workflowCode\":\"ppdzd001\", \"loanPeriod\":\"6\", \"coordinateType\":\"2\", \"typSeq\":\"73480426\", \"ifdwmcblacklist\":\"D\", \"accountName\":\"赵兵\", \"contactMblNo\":\"13534382230\", \"contactMblNo1\":\"\", \"typGrp\":\"02\", \"contactMblNo2\":\"\", \"contactMblNo3\":\"\", \"contactMblNo4\":\"\", \"accountCard\":\"6217000210021882369\", \"applAcTyp\":\"01\", \"cusSex\":\"1\", \"entryOccurDate\":\"null\", \"indivMthInc\":\"null\", \"applyTnr\":\"6\", \"otherPurpose\":\"其他\", \"ifsdidblacklist\":\"D\", \"address\":\"四川省富顺县安溪镇马山村4组20号\", \"accountCard1\":\"6217000210021882369\", \"idType\":\"10\", \"bankMblNo1\":\"18582025751\", \"contactRelation1\":\"\", \"iftelephoneblacklist\":\"D\", \"contactRelation2\":\"\", \"contactRelation3\":\"\", \"contactRelation4\":\"\", \"ifwechatblacklist\":\"D\", \"moreurl\":\"A\", \"bankCardName1\":\"赵兵\", \"pplyAmt\":\"3000.00\", \"productCode\":\"ppdzd\", \"ifmobileblacklist\":\"N\", \"ifsfzhblacklist\":\"N\"}";

		JSONObject jsonObject = JSON.parseObject( sss );



		//
//		JSONObject js = new JSONObject();
//		js.put("productCode", "Ant");
//		js.put("appId", "123");

		System.out.println(jsonObject.toJSONString());
	}

}
