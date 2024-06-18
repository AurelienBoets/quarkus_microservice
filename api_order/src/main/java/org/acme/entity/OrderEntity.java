package org.acme.entity;

import java.util.List;

import org.bson.types.ObjectId;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderEntity extends ReactivePanacheMongoEntity {
    private ObjectId id;
    private long id_user;
    private String orderDate;
    private double totalAmount;
    private String stripeSession;
    private List<OrderItem> items;
}
