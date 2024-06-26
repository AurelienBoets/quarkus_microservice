package org.acme.entity;

import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class PriceEntity extends ReactivePanacheMongoEntity {
    private String productId;
    private String platformId;
    private double price;
}
