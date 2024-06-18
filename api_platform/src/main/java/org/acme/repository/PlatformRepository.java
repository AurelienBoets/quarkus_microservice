package org.acme.repository;

import org.acme.entity.PlatformEntity;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PlatformRepository implements PanacheMongoRepository<PlatformEntity> {
    
}
