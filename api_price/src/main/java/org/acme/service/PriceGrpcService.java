package org.acme.service;

import org.acme.entity.PriceEntity;

import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import price.Price;
import price.PriceGrpc;
import price.PriceRequest;

@GrpcService    
public class PriceGrpcService implements PriceGrpc{

    @Override
    public Uni<Price> getPrice(PriceRequest request) {
        
        return PriceEntity.find("productId = ?1 and platformId = ?2",request.getProductId(),request.getPlatformId())
                          .firstResult().onItem().transform(p->{
                            PriceEntity entity=(PriceEntity) p;
                            return Price.newBuilder()
                                .setPlatformId(entity.getPlatformId())
                                .setProductId(entity.getProductId())
                                .setPrice(entity.getPrice())
                                .build();  
        });
    }

    @Override
    public Uni<Price> addPrice(Price request) {
        PriceEntity entity=new PriceEntity(request.getProductId(), request.getPlatformId(), request.getPrice());
        return entity.persist().replaceWith(entity).onItem().transform(p->Price.newBuilder()
                        .setPlatformId(p.getPlatformId())
                        .setProductId(p.getProductId())
                        .setPrice(p.getPrice())
                        .build());
    }
    
}
