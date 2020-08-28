package com.fico.blaze.service;

import com.blazesoft.server.base.NdServiceStatistics;
import com.blazesoft.server.deploy.NdStatelessServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rma.XMLJSONConverter;

public class BlazeService {

	private Logger log = LoggerFactory.getLogger("BlazeService");

	private NdStatelessServer __server = null;

	private String serverPath;

	private String serviceName;

	private String entryPoint;

	private String xsdPath = null;

	private XMLJSONConverter xmljsonConverter = null;

	public BlazeService( String serverPath, String serviceName, String entryPoint, String xsdPath ){
		this.serverPath = serverPath;
		this.serviceName = serviceName;
		this.entryPoint = entryPoint;
		this.xsdPath = xsdPath;
	}

	public void init() {

		try {
			__server = NdStatelessServer.createStatelessServer(serverPath);

			log.info(__server.getServerId().getIdentifierString());
			log.info(__server.getServerId().getHostName());
			log.info(__server.getServerId().getUniqueIdentifierString());

			xmljsonConverter = new XMLJSONConverter(xsdPath);

			warmup();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		log.info("Blaze Service is ready........");

	}

	private void warmup() throws Exception  {
//		String request = readInputRequest();
//		for (int i = 0; i < 10; i++) {
//			invokeRuleService(request);
//			Thread.sleep(50);
//		}
	}
	
	private String readInputRequest() throws Exception {
//		FileReader fr = new FileReader(testRequest);
//		BufferedReader br = new BufferedReader(fr);
//		String line = br.readLine();
//		StringBuffer sb = new StringBuffer();
//		while (line != null) {
//			sb.append(line);
//			line = br.readLine();
//		}
//		br.close();
//		fr.close();
//		return sb.toString();
		return null;
	}
	
	public String invokeRuleService(String requestXML) throws Exception {

		Object[] applicationArgs = new Object[1];
		applicationArgs[0] = requestXML;

		// Invoke the entry point
		String retVal = (String) __server.invokeService(serviceName, entryPoint, null, applicationArgs);

		return retVal;
	}

	public String invokeRuleServiceJSON(String requestJSON) throws Exception {

		String requestXML = xmljsonConverter.convertJSONToXML(requestJSON);

		String responseXML = invokeRuleService(requestXML);

		String responseJSON = xmljsonConverter.convertXMLToJSON(responseXML);

		return responseJSON;
	}

	public String status() {

		NdServiceStatistics[] statistics = __server.getStatistics().getServiceStatistics();
		StringBuffer sb = new StringBuffer();

		for (NdServiceStatistics stat : statistics) {
			sb.append("<p>****************************************************</p>");
			sb.append("Service Id:" + stat.getServiceId() + "<br/>");
			sb.append("Processed sessions:" + stat.getNumProcessedSessions() + "<br/>");
			sb.append("NumAgents:" + stat.getNumAgents() + "<br/>");
			sb.append("Executing NumAgents:" + stat.getNumExecutingAgents() + "<br/>");
		}

		return sb.toString();
	}

	public void reset() {
		try {
			if (__server == null) {
				__server = NdStatelessServer.createStatelessServer(serverPath);
			} else {
				__server.resetService(serviceName);
			}
			warmup();
			log.info("Blaze Service is ready........");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
