package com.fico.blaze.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@Configuration
@EnableKafka
public class BaiRongKafkaProducerConfig {

//    /**
//     * 同步的kafka需要ReplyingKafkaTemplate,指定repliesContainer
//     * @return
//     */
//    @Bean
//    public ReplyingKafkaTemplate<String, String, String> baiRongReplyingTemplate(
//            ProducerFactory<String, String> baiRongProducerFactory,
//            ConcurrentMessageListenerContainer<String, String> baiRongRepliesContainer) {
//
//        ReplyingKafkaTemplate template = new ReplyingKafkaTemplate<>(baiRongProducerFactory, baiRongRepliesContainer);
//        //同步相应超时时间：10s
//        //template.setReplyTimeout(10000);
//        return template;
//    }
//
//    @Bean
//    public ProducerFactory<String,String> baiRongProducerFactory() {
//        return new DefaultKafkaProducerFactory<>(baiRongProducerConfigs());
//    }
//
//
//    @Bean
//    public KafkaTemplate baiRongKafkaTemplate(){
//        return new KafkaTemplate(baiRongProducerFactory());
//    }
//
//    @Bean
//    public Map<String, Object> baiRongProducerConfigs() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
//        props.put(ProducerConfig.RETRIES_CONFIG, 3);
//        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
//        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
//        //发送一次message最大大小，默认是1M,这里设置为20M
//        //props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, 20971520);
//        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        return props;
//    }
//
//    /**
//     * 指定consumer返回数据到指定的topic
//     * @return
//     */
//    @Bean
//    public ConcurrentMessageListenerContainer<String, String> baiRongRepliesContainer(ConcurrentKafkaListenerContainerFactory<String, String> baiRongContainerFactory) {
//        ConcurrentMessageListenerContainer<String, String> repliesContainer =
//                baiRongContainerFactory.createContainer("REPLY_ASYN_MESSAGE_BaiRong");
//        repliesContainer.getContainerProperties().setGroupId("replies_message_group_bairong");
//        repliesContainer.setAutoStartup(false);
//        return repliesContainer;
//    }

}
