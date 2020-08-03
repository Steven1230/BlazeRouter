package rma;

import java.io.IOException;

import com.alibaba.fastjson.JSONObject;

public class TestBlaze {

	public static void main(String[] args) throws IOException {
		
		 String str = JsonUtils.readJson("C:\\Apps\\json\\request.json");
		    JSONObject  jsonObject = JSONObject.parseObject(str);
		    
		    System.out.println("Tongdun.anti_fraud_score:"+JsonUtils.getJsonIntValue(jsonObject, "Tongdun.anti_fraud_score"));
}

}
