package org.acme.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.acme.utils.UniProcess;
import org.acme.utils.mapper.ProductMapper;

import aggregate.AggregateGrpc;
import aggregate.Empty;
import aggregate.ListOfProduct;
import aggregate.Product;
import aggregate.ProductId;
import aggregate.ProductRequest;
import aggregate.SendPlatform;
import category.Category;
import category.CategoryGrpc;
import category.CategoryId;
import io.quarkus.grpc.GrpcClient;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import platform.Platform;
import platform.PlatformGrpc;
import platform.PlatformId;
import price.Price;
import price.PriceGrpc;
import price.PriceRequest;
import product.AddProduct;
import product.ProductGrpc;

@GrpcService
public class AggregateGrpcService implements AggregateGrpc {

    @GrpcClient
    CategoryGrpc categoryGrpc;

    @GrpcClient
    PlatformGrpc platformGrpc;

    @GrpcClient
    PriceGrpc priceGrpc;

    @GrpcClient
    ProductGrpc productGrpc;

    @Inject
    UniProcess process;

    @Inject
    ProductMapper productMapper;

    @Override
    public Uni<Product> createProduct(ProductRequest request) {
        List<String> platformsId = new ArrayList<>();
        for (SendPlatform platform : request.getPlatformsList()) {
            platformsId.add(platform.getId());
        }
        AddProduct product = AddProduct.newBuilder()
            .setName(request.getName())
            .setDescription(request.getDescription())
            .setImg(request.getImg())
            .setFormatImg(request.getFormatImg())
            .addAllPlatformId(platformsId)
            .addAllCategoryId(request.getCategoryIdList())
            .build();

        return productGrpc.createProduct(product).onItem().transformToUni(p -> {
            return buildProductOnCreate(p,request);
        });
    }

    @Override
    public Uni<Product> findProduct(ProductId request) {

        return productGrpc.findProduct(product.ProductId.newBuilder().setId(request.getId()).build()).onItem()
                .transformToUni(p -> {
                    return buildProduct(p);
                });
    }

    @Override
    public Uni<ListOfProduct> getAllProduct(Empty request) {

        return productGrpc.getAllProduct(null).onItem().transformToUni(productList -> {
            List<Uni<Product>> productUnis = productList.getProductsList().stream().map(p -> {
                return buildProduct(p);
            }).collect(Collectors.toList());

            CompletableFuture<List<Product>> productFuture = process.convertProductToFuture(productUnis);

            Uni<ListOfProduct> listOfProduct = Uni.createFrom().completionStage(productFuture).onItem()
                .transform(list -> productMapper.buildList(list));
            return listOfProduct;
        });

    }

    private Uni<Product> buildProduct(product.Product p) {
        List<Uni<Category>> categoryUnis = p.getCategoryIdList().stream()
            .map(categoryId -> categoryGrpc.getCategory(CategoryId.newBuilder().setId(categoryId).build()))
            .collect(Collectors.toList());

        List<Uni<Price>> priceUnis = p.getPlatformIdList().stream()
            .map(platformId -> priceGrpc.getPrice(PriceRequest.newBuilder()
                .setPlatformId(platformId)
                .setProductId(p.getId())
                .build()))
            .collect(Collectors.toList());

        List<Uni<Platform>> platformUnis = p.getPlatformIdList().stream()
            .map(platformId -> platformGrpc.getPlatform(PlatformId.newBuilder().setId(platformId).build()))
            .collect(Collectors.toList());

        CompletableFuture<List<Price>> priceFuture = process.convertPriceToFuture(priceUnis);
        CompletableFuture<List<Category>> categoryFuture = process.convertCategoryToFuture(categoryUnis);
        CompletableFuture<List<Platform>> platformFuture = process.convertPlatformToFuture(platformUnis);

        Uni<Product> aggregateResponse = Uni.combine().all().unis(
            Uni.createFrom().completionStage(priceFuture),
            Uni.createFrom().completionStage(categoryFuture),
            Uni.createFrom().completionStage(platformFuture)
        ).asTuple().onItem().transform(tuple -> productMapper.buildProduct(p, tuple.getItem2(), tuple.getItem3(), tuple.getItem1()));

        return aggregateResponse;
    }

        private Uni<Product> buildProductOnCreate(product.Product p,ProductRequest request) {
        List<Uni<Category>> categoryUnis = p.getCategoryIdList().stream()
            .map(categoryId -> categoryGrpc.getCategory(CategoryId.newBuilder().setId(categoryId).build()))
            .collect(Collectors.toList());

        List<Uni<Price>> priceUnis = request.getPlatformsList().stream()
            .map(platform -> priceGrpc.addPrice(Price.newBuilder()
                .setPlatformId(platform.getId())
                .setProductId(p.getId())
                .setPrice(platform.getPrice())
                .build()))
            .collect(Collectors.toList());

        List<Uni<Platform>> platformUnis = p.getPlatformIdList().stream()
            .map(platformId -> {
                return platformGrpc.getPlatform(PlatformId.newBuilder().setId(platformId).build());
            })
            .collect(Collectors.toList());

        CompletableFuture<List<Price>> priceFuture = process.convertPriceToFuture(priceUnis);
        CompletableFuture<List<Category>> categoryFuture = process.convertCategoryToFuture(categoryUnis);
        CompletableFuture<List<Platform>> platformFuture = process.convertPlatformToFuture(platformUnis);

        Uni<Product> aggregateResponse = Uni.combine().all().unis(
            Uni.createFrom().completionStage(priceFuture),
            Uni.createFrom().completionStage(categoryFuture),
            Uni.createFrom().completionStage(platformFuture)
        ).asTuple().onItem().transform(tuple -> productMapper.buildProduct(p, tuple.getItem2(), tuple.getItem3(), tuple.getItem1()));
        return aggregateResponse;
    }
}
