package org.acme.entity;



import org.bson.types.ObjectId;
import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@MongoEntity(collection = "platform")
public class PlatformEntity extends ReactivePanacheMongoEntity {
    private String name;

    public void setId(ObjectId id) {
        this.id = id;
    }
}
