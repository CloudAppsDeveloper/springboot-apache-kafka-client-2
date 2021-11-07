package com.cinch.kafka.orders.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

import com.cinch.kafka.orders.model.Orders;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

	@Value(value = "${kafka.bootstrap.address}")
	private String bootstrapAddress;

	@Value(value = "${kafka.orders.consumer.group-id}")
	private String orderGroupId;

	@Value(value = "${kafka.customer.consumer.group-id}")
	private String customerGroupId;

	@Value(value = "${spring.kafka.consumer.auto-offset-reset}")
	private String autoOffsetReset;

	@Bean
	public Map<String, Object> orderConsumerConfigs() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, orderGroupId);
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		return props;
	}

	@Bean
	public Map<String, Object> customerConsumerConfigs() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, customerGroupId);
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		return props;
	}

	@Bean
	public ConsumerFactory<String, Orders> consumerFactory() {

		return new DefaultKafkaConsumerFactory<>(orderConsumerConfigs());
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, Orders> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, Orders> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		factory.setMessageConverter(new StringJsonMessageConverter());
		return factory;
	}

	@Bean
	public ConsumerFactory<String, String> requestConsumerFactory() {
		return new DefaultKafkaConsumerFactory<>(customerConsumerConfigs(), new StringDeserializer(),
				new StringDeserializer());
	}

	@Bean
	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> requestListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(requestConsumerFactory());
		factory.setReplyTemplate(replyTemplate());
		return factory;
	}

	@Bean
	public Map<String, Object> producerConfigs() {
		Map<String, Object> props = new HashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

		return props;
	}

	@Bean
	public ProducerFactory<String, String> replyProducerFactory() {
		return new DefaultKafkaProducerFactory<>(producerConfigs());
	}

	@Bean
	public KafkaTemplate<String, String> replyTemplate() {
		return new KafkaTemplate<>(replyProducerFactory());
	}

}
