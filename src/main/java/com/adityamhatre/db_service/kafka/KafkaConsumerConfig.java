package com.adityamhatre.db_service.kafka;

import dto.SongDTO;
import dto.UserDTO;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
public class KafkaConsumerConfig {
	private final KafkaProperties kafkaProperties;

	public KafkaConsumerConfig(KafkaProperties kafkaProperties) {
		this.kafkaProperties = kafkaProperties;
	}


	//	---- USER DTO CONFIG
	@Bean
	public ConsumerFactory<String, UserDTO> consumerFactory() {
		final JsonDeserializer<UserDTO> jsonDeserializer = new JsonDeserializer<>();
		jsonDeserializer.addTrustedPackages("*");
		return new DefaultKafkaConsumerFactory<>(
				kafkaProperties.buildConsumerProperties(), new StringDeserializer(), jsonDeserializer
		);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, UserDTO> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, UserDTO> factory =
				new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());

		return factory;
	}


	//	---- SONG DTO CONFIG
	@Bean
	public ConsumerFactory<String, SongDTO> songConsumerFactory() {
		final JsonDeserializer<SongDTO> jsonDeserializer = new JsonDeserializer<>();
		jsonDeserializer.addTrustedPackages("*");
		return new DefaultKafkaConsumerFactory<>(
				kafkaProperties.buildConsumerProperties(), new StringDeserializer(), jsonDeserializer
		);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, SongDTO> kafkaListenerSongContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, SongDTO> factory =
				new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(songConsumerFactory());

		return factory;
	}
}
