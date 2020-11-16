package com.fico.blaze.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
@ConfigurationProperties(prefix="blaze.router")
public class DataProviderFactory {

    @Autowired
    private ApplicationContext appContext;

    private InputSystem inputSystem;

    private List<DataProvider> dataProvider = null;

    private HashMap<String,DataProvider> providerList = null;

    private KafkaStrategy kafkaStrategy;

    public List<DataProvider> getDataProvider() {
        return dataProvider;
    }

    public void setDataProvider(List<DataProvider> dataProvider) {
        this.dataProvider = dataProvider;
    }

    public HashMap<String, DataProvider> getProviderList() {
        return providerList;
    }

    public void setProviderList(HashMap<String, DataProvider> providerList) {
        this.providerList = providerList;
    }


    public  DataProvider createDataProvider(String name) {
        if(providerList==null) {
            providerList = new HashMap<String,DataProvider>();
            initDataProvider();
        }
        return providerList.get(name);
    }

    private void initDataProvider() {

        for(DataProvider dp:dataProvider) {
            if("rest".equals(dp.getType())) {
                dp.setGetStrategy(new RestStrategy());
            }else if("kafka".equals(dp.getType())) {
                dp.setGetStrategy( buildKafkaStrategy(dp) );
            }
            providerList.put(dp.getName(), dp);
            if(dp.getChildDataList() != null && dp.getChildDataList().length>0){
                for(String tmpChild : dp.getChildDataList()){
                    providerList.put(tmpChild, dp);
                }
            }
        }
    }

    private KafkaStrategy buildKafkaStrategy(DataProvider dataProvider){
        KafkaStrategy kafkaStrategy = new KafkaStrategy();

        return kafkaStrategy;
    }

}