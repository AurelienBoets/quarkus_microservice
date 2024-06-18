package org.acme.repository;

import org.acme.entity.PriceEntity;

import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PriceRepository implements PanacheMongoRepository<PriceEntity>{

}