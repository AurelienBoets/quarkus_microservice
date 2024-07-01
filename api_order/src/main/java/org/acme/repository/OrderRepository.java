package org.acme.repository;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.acme.entity.OrderEntity;


@ApplicationScoped
public class OrderRepository implements ReactivePanacheMongoRepository<OrderEntity> {
}
