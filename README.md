# BlazeRouter
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





系统示意 （第三方数据同步调用）
-----------------------
### Application System
	* 1. send to topic : "blaze-request"
	* 4. listent to topic : "blaze-response"
### BlazeRouter
	* 2. listen to topic : "blaze-request"
	  - getTongdunData 
	  - invoke blaze service
	  - send to topic : "Call2" or "blaze-response"
	* 3. listen to topic : "Call2"
	  - getBairongData
	  - invoke blaze service
	  - send to topic "blaze-response"
### DataProvider 
	* Tongdun
	* Bairong
### Blaze Service
	* Call1 反欺诈策略
	* Call2 风险审批策略
	
系统示意 （第三方数据异步调用）
-----------------------
### Application System
	* 1. send to topic : "blaze-request"
	* 7. listent to topic : "blaze-response"
### BlazeRouter
	* 2. listen to topic : "blaze-request"
	  - send to topic :"tongdun-request"
	* 4. listen to topic : "tongdun-response"
	  - invoke blaze service
	  - send to topic : "bairong-request" or "blaze-response"
	* 6. listen to topic : "bairong-response"
	  - invoke blaze service
	  - send to topic "blaze-response"
### DataProvider 
	* Tongdun
		- 3 .listen to topic : "tongdun-request"
		     send to topic "tongdun-response"  
	* Bairong
	    - 5 .listen to topic : "bairong-request"
			 send to topic : "bairong-response:
### Blaze Service
	* Call1 反欺诈策略
	* Call2 风险审批策略





											