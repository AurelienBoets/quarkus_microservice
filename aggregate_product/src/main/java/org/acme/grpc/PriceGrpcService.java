package org.acme.grpc;

import io.quarkus.grpc.GrpcClient;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import price.PriceGrpc;
import price.PriceRequest;
import price.Price;

@GrpcService
public class PriceGrpcService implements PriceGrpc {
    
    @GrpcClient
    PriceGrpc priceGrpc;

    @Override
    public Uni<Price> getPrice(PriceRequest request){
        return priceGrpc.getPrice(request);
    }

    @Override
    public Uni<Price> addPrice(Price request){
        return priceGrpc.addPrice(request);
    }
}
