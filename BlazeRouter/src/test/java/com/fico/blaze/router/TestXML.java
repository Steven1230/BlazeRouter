package com.fico.blaze.router;

import java.io.StringReader;
import java.io.StringWriter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.QName;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
import org.dom4j.xpath.DefaultXPath;

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

		JSONObject js = new JSONObject();
		js.put("productCode", "Ant");
		js.put("appId", "123");

		System.out.println(JSON.parseObject(a));
	}

}
