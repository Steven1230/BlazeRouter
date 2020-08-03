package rma;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.alibaba.fastjson.JSONObject;

public class JsonUtils {

	
	public static String readJson(String path) throws IOException {

		FileReader fr =new FileReader(new File(path));
		BufferedReader br = new BufferedReader(fr);
		String line =br.readLine();
		StringBuffer sb = new StringBuffer();
		while(line!=null) {
			sb.append(line);
			line=br.readLine();
		}
		br.close();
		fr.close();
		br=null;
		fr=null;
		return sb.toString();
	}
	
	public static int getJsonIntValue(JSONObject jsonObject,String nodepath) {
		String[] nodes = nodepath.split("\\.");
		for(int i=0;i<nodes.length-1;i++) {
			jsonObject = jsonObject.getJSONObject(nodes[i]);
		}
		return jsonObject.getIntValue(nodes[nodes.length-1]);
	}
	
	public static double getJsonRealValue(JSONObject jsonObject,String nodepath) {
		String[] nodes = nodepath.split("\\.");

		for(int i=0;i<nodes.length-1;i++) {
			jsonObject = jsonObject.getJSONObject(nodes[i]);
		}
		return jsonObject.getDoubleValue(nodes[nodes.length-1]);
	}
	
	public static String getJsonStringValue(JSONObject jsonObject,String nodepath) {
		String[] nodes = nodepath.split("\\.");
		
		for(int i=0;i<nodes.length-1;i++) {
			jsonObject = jsonObject.getJSONObject(nodes[i]);
		}
		return jsonObject.getString(nodes[nodes.length-1]);
	}
}
