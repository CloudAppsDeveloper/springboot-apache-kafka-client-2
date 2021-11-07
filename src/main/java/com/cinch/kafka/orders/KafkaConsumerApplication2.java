package com.cinch.kafka.orders;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class KafkaConsumerApplication2 {

	public static void main(String[] args) {
		SpringApplication.run(KafkaConsumerApplication2.class, args);
	}

}
