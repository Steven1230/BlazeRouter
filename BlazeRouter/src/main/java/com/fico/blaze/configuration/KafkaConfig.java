package com.fico.blaze.configuration;

//
public class KafkaConfig {
//
//	@Bean
//	public ReplyingKafkaTemplate<String, String, String> replyKafkaTemplate(ProducerFactory<String, String> pf,
//			KafkaMessageListenerContainer<String, String> container) {
//		ReplyingKafkaTemplate template = new ReplyingKafkaTemplate<>(pf, container);
//		return template;
//	}
//
//	// 配件：监听器容器Listener Container to be set up in ReplyingKafkaTemplate
//	@Bean
//	public KafkaMessageListenerContainer<String, String> replyContainer(ConsumerFactory<String, String> cf) {
//		ContainerProperties containerProperties = new ContainerProperties("Tongdun-request");
//		return new KafkaMessageListenerContainer<>(cf, containerProperties);
//	}
//
//	// 配件：生产者工厂Default Producer Factory to be used in ReplyingKafkaTemplate
//	@Bean
//	public ProducerFactory<String, String> producerFactory() {
//		return new DefaultKafkaProducerFactory<>(producerConfigs());
//	}
//
//	@Bean
//	public Map<String, Object> producerConfigs() {
//		Map<String, Object> props = new HashMap<>();
//		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//		return props;
//	}
//
//	// 消费者工厂 Default Consumer Factory
//	@Bean
//	public ConsumerFactory<String, String> consumerFactory() {
//		Map<String, Object> props = new HashMap<>();
//		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//		props.put(ConsumerConfig.GROUP_ID_CONFIG,"blaze-group");
//		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//		// set more properties
//		return new DefaultKafkaConsumerFactory<>(props);
//	}
//
//
//
//	// 并发监听器容器Concurrent Listner container factory
//	@Bean
//	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
//		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
//		factory.setConsumerFactory(consumerFactory());
//		// NOTE - set up of reply template 设置响应模板
//		factory.setReplyTemplate(kafkaTemplate());
//		return factory;
//	}
//
//	// Standard KafkaTemplate
//	@Bean
//	public KafkaTemplate<String, String> kafkaTemplate() {
//		return new KafkaTemplate<>(producerFactory());
//	}
}
