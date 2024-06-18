package org.acme.repository;

import org.acme.entity.CategoryEntity;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CategoryRepository implements PanacheMongoRepository<CategoryEntity> {
    
}
