package org.acme.service;

import price.Price;
import price.PriceGrpc;
import price.PriceRequest;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;

import jakarta.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;

import aggregate.SendPlatform;

@Singleton
public class PriceService {

    @GrpcClient
    private PriceGrpc priceGrpc;

    public List<Uni<Price>> addPrices(List<SendPlatform> platforms, String productId) {
        return platforms.stream()
                .map(platform -> priceGrpc.addPrice(Price.newBuilder()
                        .setPlatformId(platform.getId())
                        .setProductId(productId)
                        .setPrice(platform.getPrice())
                        .build()))
                .collect(Collectors.toList());
    }

    public List<Uni<Price>> fetchPrices(List<String> platformIds, String productId) {
        return platformIds.stream()
                .map(platformId -> priceGrpc.getPrice(PriceRequest.newBuilder()
                        .setPlatformId(platformId)
                        .setProductId(productId)
                        .build()))
                .collect(Collectors.toList());
    }
}
