package com.cinch.kafka.orders.repository;

import org.springframework.data.repository.CrudRepository;
import com.cinch.kafka.orders.model.Orders;

public interface OrderRepository extends CrudRepository<Orders, Long> {

}
