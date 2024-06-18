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
@Builder
@Data
public class ProductEntity extends ReactivePanacheMongoEntity {
    private ObjectId id;
    private String name;
    private String description;
    private String img;
    private String formatImg;
    private List<String> categoryId;
    private List<String> platformId;
}
