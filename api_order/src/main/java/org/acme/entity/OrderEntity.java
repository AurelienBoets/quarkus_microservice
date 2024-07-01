package org.acme.entity;

import java.util.List;
import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@MongoEntity(collection = "order")
public class OrderEntity extends ReactivePanacheMongoEntity {
    private long idUser;
    private String orderDate;
    private double totalAmount;
    private String stripeSession;
    private List<OrderItem> items;
}
