package com.cinch.kafka.orders.consumer;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.cinch.kafka.orders.model.Orders;
import com.cinch.kafka.orders.repository.OrderRepository;

@Service
public class KafkaOrdersConsumer2 {
	
	@Autowired
	private OrderRepository orderRepository;
	
	private final Logger logger = LoggerFactory.getLogger(KafkaOrdersConsumer2.class);

	@KafkaListener(topics = "${kafka.orders.topic-name}", groupId = "${kafka.orders.consumer.group-id}")
	public void consume(@Payload Orders orders, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
			@Header(KafkaHeaders.OFFSET) String offset,@Header(KafkaHeaders.GROUP_ID) String groupId) throws IOException {

		logger.info("Received Message:  -> %s " + orders + "from partition: " + partition);

		orders.setPartitionNum(partition);
		orders.setOffsetNum(offset);
		orders.setGroupId(groupId);

		orderRepository.save(orders);
	}

}
