package rma;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.QName;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
import org.dom4j.xpath.DefaultXPath;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class XMLJSONConverter {
	
	private Document xsdDocument = null;
	
	private Element xsdRoot = null;
	
	private String nameSpaceURI = "";
	
	private String nameSpacePrefix = "";
	
	private Namespace namespace = new Namespace("", "");
	
	private boolean hasNameSpace = true;
	
	private String applicationRootStr = null;
	
	private String jsonRootString = "{}";
	
	private List<XSDRelation> xsdRelations = new ArrayList<>();
	
	class XSDRelation{
		 String parentElName;
		 boolean isChildElPrimaryType = false;
		 List<String> childNameList = new ArrayList<String>();
	}
	
	public String getApplicationRootStr() {
		return applicationRootStr;
	}

	/**
	 * @param xsdFilePathString 调用Blaze服务的对应的XSD文件路径
	 * @throws Exception
	 */
	public XMLJSONConverter(String xsdFilePathString) throws Exception {
		SAXReader reader = new SAXReader();
		xsdDocument = reader.read(xsdFilePathString);
		init();
	}

	/**
	 *
	 * @param xsdFile BlazeXSD文件
	 * @throws Exception
	 */
	public XMLJSONConverter(File xsdFile) throws Exception {
		SAXReader reader = new SAXReader();
		xsdDocument = reader.read(xsdFile);
		init();
	}

	private void init() throws Exception {

		xsdRoot = xsdDocument.getRootElement();
		
		String xsdTargetNamespaceString = xsdRoot.attributeValue("targetNamespace");
		
		if(xsdTargetNamespaceString == null) {
			hasNameSpace = false;
		}else {
			generateNameSpaceMap(xsdTargetNamespaceString);
		}
		
		generateApplicationRootStr();
		
		findOneToManyRelationShipInXSD();
	}
	
	private void findOneToManyRelationShipInXSD() {
		
		String xpathRep = "//" + this.nameSpacePrefix + ":attribute";
		
		String preFixString = this.xsdDocument.getRootElement().getNamespace().getPrefix();
		
		XPath xpath = this.xsdDocument.getRootElement().createXPath( "//xs:element[@maxOccurs]" );
		
		List<Node> oneToManyMaxOccursList =  xpath.selectNodes( this.xsdDocument );
		
		for(Node node : oneToManyMaxOccursList) {
			
			Element tmpElement = (Element) node;
			
			XSDRelation xsdRelation = new XSDRelation();
			
			String manySideElNameString = tmpElement.attributeValue("ref");
			
			//primary Type
			if(manySideElNameString == null) {
				xsdRelation.isChildElPrimaryType = true;
				manySideElNameString = tmpElement.attributeValue("name");
			}
			
			xsdRelation.childNameList.add(manySideElNameString);
			
			Element parentXSDElementEL = tmpElement.getParent().getParent().getParent();
			
			String parentELName = parentXSDElementEL.attributeValue("name");
			
			xsdRelation.parentElName = parentELName;
			
			xsdRelations.add(xsdRelation);
		}
	}
	
	/**
	 * 默认所有项目的跟节点都是Application
	 */
	private void generateApplicationRootStr() {
		Document xmlDocument = DocumentHelper.createDocument();
		
		QName appQName = QName.get("Application", namespace);
		
		Element appElement = xmlDocument.addElement( appQName );
		
		appElement.add(namespace);
		
		this.applicationRootStr = appElement.asXML();
	}
	
	/**
	 * 初始化XSD中的Namespace
	 * 假设namespace不允许客户瞎改瞎输
	 * @param xsdTargetNamespaceString
	 */
	private void generateNameSpaceMap(String xsdTargetNamespaceString) {
		String prefixString = xsdTargetNamespaceString.substring( xsdTargetNamespaceString.lastIndexOf("/")+1 );
		this.nameSpacePrefix = prefixString;
		this.nameSpaceURI = xsdTargetNamespaceString;
		namespace = new Namespace(nameSpacePrefix, nameSpacePrefix);
	}

	/**
	 * 外部调用接口，从输入JSON字符串
	 * @param jsonString 调用Blaze的JSON格式的报文
	 * @return 对应的XML格式的字符串
	 * @throws Exception
	 */
	public String convertJSONToXML(String jsonString) throws Exception {
		
		JSONObject rootJsonObject = JSON.parseObject(jsonString);
		
		Document rootDocument = DocumentHelper.parseText(this.applicationRootStr);
		
		Element rootElement = rootDocument.getRootElement();
		
		convertJSONtoXML(rootJsonObject, rootElement);
		
		return rootElement.asXML();
	}
	
	/**
	 * 
	 * @param xmlString Blaze返回的XML格式的报文
	 * @return 对应的JSON格式的报文
	 * @throws Exception
	 */
	public String convertXMLToJSON(String xmlString) throws Exception{
		
		Document rootDoc = DocumentHelper.parseText(xmlString);
		
		Element appRootElement = rootDoc.getRootElement();
		
		JSONObject jsonObject = JSONObject.parseObject(jsonRootString);
		
		convertXMLToJSON(appRootElement, jsonObject);
		
		return jsonObject.toJSONString();
	}
	
	private void convertXMLToJSON(Element el, JSONObject jsonObject) {

		for( Attribute attribute : el.attributes() ) {
			jsonObject.put(attribute.getName(), attribute.getValue());
		}
		
		Map<String, List> tmpChildMapListMap = new HashMap<>();
		
		for(Element element : el.elements()) {
			String elementNameString = element.getName();
			
			XSDRelation xRelation = findOneToManyRel( el.getName(), element.getName() );
			
			//一对一，直接put
			if(xRelation == null) {
				JSONObject tmpJsonObject = JSONObject.parseObject(jsonRootString);
				convertXMLToJSON( element, tmpJsonObject );
				jsonObject.put(elementNameString, tmpJsonObject);
			}else {
				if( jsonObject.get(elementNameString) == null ) {
					jsonObject.put(elementNameString, new JSONArray());
				}
				
				JSONArray tmpArray = (JSONArray)jsonObject.get(elementNameString);
				if(xRelation.isChildElPrimaryType) {
					tmpArray.add( element.getText() );
				}else {
					JSONObject tmpJsonObject = JSONObject.parseObject(jsonRootString);
					convertXMLToJSON( element, tmpJsonObject );
					tmpArray.add(tmpJsonObject);
				}
			}
		}
	}
	
	private XSDRelation findOneToManyRel(String parentName, String childName) {
		for(XSDRelation xsdRelation : this.xsdRelations) {
			if(parentName.equals(xsdRelation.parentElName) && xsdRelation.childNameList.contains(childName) ) {
				return xsdRelation;
			}
		}
		return null;
	}
	
	private void convertJSONtoXML(JSONObject jsonObject, Element element) {
		for(String key : jsonObject.keySet()) {
			Object keyVal = jsonObject.get(key);
			if(keyVal instanceof JSONObject) {
				Element tmpNewElement = addElement(element, key);
				JSONObject keyValJSON = (JSONObject)keyVal;
				convertJSONtoXML(keyValJSON ,tmpNewElement);
			}else if (keyVal instanceof JSONArray) {
				JSONArray keyValJSONArr = (JSONArray)keyVal;
				
				for(Iterator iterator = keyValJSONArr.iterator(); iterator.hasNext(); ) {
					JSONObject tmpJsonObject = (JSONObject)iterator.next();
					Element tmpNewElement = addElement(element, key);
					convertJSONtoXML(tmpJsonObject ,tmpNewElement);
				}
			}else {
				setElementAttrValue( element, key, keyVal.toString() );
			}
		}
	}
	
	private void setElementAttrValue(Element element, String attrName, String attrVal) {
		//element.attributeValue(attrName, attrVal);
		element.setAttributeValue(attrName, attrVal);
	}
	
	private Element addElement(Element parentElement, String newEleName) {
		return parentElement.addElement(QName.get(newEleName, namespace) );
	}
}
