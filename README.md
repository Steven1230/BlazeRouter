# BlazeRouter
Blaze Router
===========

使用Docker Hub 
-------------

### Step 1:Install Docker Hub     ->  https://hub.docker.com/
### Step 2:Getting Started with Docker
    * >> git clone https://github.com/docker/getting-started.git
	* >> cd getting-started
	* >> docker build -t docker101tutorial .
	* >> docker run -d -p 80:80 --name docker-tutorial docker101tutorial
	* >> docker tag docker101tutorial ficoshaobo/docker101tutorial
	* >> docker push ficoshaobo/dockertutorial

	
### Step 3: Deploy BlazeRouter





System 

Application System                      BlazeRouter                                         DataProvider           Blaze Service      

1) send to topic:"blaze-request"        2)listent to topic:"blaze-request"                                          Call1 NextStep:(  "blaze-response","Call2")
										   invoke "Tongdun"
										   invoke "RuleService"
										   sendto topic "Call2" or "blaze-response"                                 Call2  NextStep :  blaze-response
																													
																													  {NextStep:
																															topic: call2
																															data-provider: pboc
																															data-provider: bairong}
										
										3)listent to topic :"Call2"
										    invoke "Bairong"
											invoke "RuleService"
										    sendto topic "blaze-response"
4)listent to topic "blaze-response"



Xiangbin:

Application System                      BlazeRouter                                         DataProvider           Blaze Service      

1) send to topic:"blaze-request"        2)listent to topic:"blaze-request" 
											send to topic:"tongdun"
										3)listent to topic:"tongdun"
											get tongdundata
											invoker "ruleservice"
										    send to "Call2"
										4)listent to topic "Call2"
										     send to toptic "bairong"
										5)listent to topic "bairong"
											 get bairong data
											 invoke "ruleservice"
											 sendto "blaze-response"
								
6)listent to topic "blaze-response"





											