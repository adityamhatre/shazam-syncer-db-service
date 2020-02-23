package com.adityamhatre.db_service.kafka;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class KafkaTopicConfig {
	@Value(value = "${spring.kafka.producer.bootstrap-servers}")
	private String bootstrapAddress;
	private final ApplicationContext applicationContext;

	public KafkaTopicConfig(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Bean
	public KafkaAdmin kafkaAdmin() {
		Map<String, Object> configs = new HashMap<>();
		configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
		return new KafkaAdmin(configs);
	}


	@PostConstruct
	public void init() {
		ConfigurableListableBeanFactory beanFactory = ((ConfigurableApplicationContext) applicationContext).getBeanFactory();
		AtomicInteger topicPrefix = new AtomicInteger();
		Arrays.stream(Topics.values()).forEach(topic -> beanFactory.registerSingleton("topic" + topicPrefix.incrementAndGet(), new NewTopic(topic.getTopicName(), topic.getNumPartitions(), (short) 1)));
	}


}
