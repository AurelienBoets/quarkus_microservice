package org.acme.entity;


import org.bson.types.ObjectId;

import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@MongoEntity(collection = "platform")
public class PlatformEntity extends ReactivePanacheMongoEntity {
    private ObjectId id;
    private String name;
}
