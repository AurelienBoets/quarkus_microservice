package org.acme.entity;

import java.util.List;


import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class ProductEntity extends ReactivePanacheMongoEntity {
    private String name;
    private String description;
    private String img;
    private String formatImg;
    private List<String> categoryId;
    private List<String> platformId;
}
