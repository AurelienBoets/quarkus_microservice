package org.acme.utils.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.acme.utils.UniProcess;

import aggregate.Category;
import aggregate.ListOfProduct;
import aggregate.ListOfSearchProduct;
import aggregate.Platform;
import aggregate.Product;
import category.CategoryGrpc;
import category.CategoryId;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import platform.PlatformGrpc;
import platform.PlatformId;
import price.Price;
import price.PriceGrpc;
import price.PriceRequest;
import product.ProductGrpc;
import io.quarkus.grpc.GrpcClient;


@ApplicationScoped
public class ProductMapper {
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

    public Product buildProduct(product.Product product,List<category.Category> category,List<platform.Platform> platform,List<Price> price){
        return Product.newBuilder()
                .setId(product.getId())
                .setName(product.getName())
                .setDescription(product.getDescription())
                .setImg(product.getImg())
                .setFormatImg(product.getFormatImg())
                .addAllCategories(convertCategory(category))
                .addAllPlatforms(convertPlatform(platform, price))
                .build();
    }

    private List<Category> convertCategory(List<category.Category> categories){
        List<Category> aggregateCategory=new ArrayList<>();
        for(category.Category c:categories){
            aggregateCategory.add(Category.newBuilder()
                                    .setId(c.getId())
                                    .setName(c.getName())
                                    .build());
        }
        return aggregateCategory;
    }

    private List<Platform> convertPlatform(List<platform.Platform> platforms, List<Price> prices){
        List<Platform> aggregatPlatforms=new ArrayList<>();
        for (int i = 0; i < prices.size(); i++) {
            aggregatPlatforms.add(Platform.newBuilder()
                                        .setId(platforms.get(i).getId())
                                        .setName(platforms.get(i).getName())
                                        .setPrice(prices.get(i).getPrice())
                                        .build());                                
        }
        return aggregatPlatforms;
    }

    public ListOfProduct buildList(List<Product> productList){
        return ListOfProduct.newBuilder().addAllProducts(productList).build();
    }

    public ListOfSearchProduct buildSearchList(List<Product> productList,Long totalCount){
        return ListOfSearchProduct.newBuilder().addAllProducts(productList).setPageCount(totalCount).build();
    }

    public Uni<Product> buildProduct(product.Product p) {
        List<Uni<category.Category>> categoryUnis = p.getCategoryIdList().stream()
            .map(categoryId -> categoryGrpc.getCategory(CategoryId.newBuilder().setId(categoryId).build()))
            .collect(Collectors.toList());

        List<Uni<Price>> priceUnis = p.getPlatformIdList().stream()
            .map(platformId -> priceGrpc.getPrice(PriceRequest.newBuilder()
                .setPlatformId(platformId)
                .setProductId(p.getId())
                .build()))
            .collect(Collectors.toList());

        List<Uni<platform.Platform>> platformUnis = p.getPlatformIdList().stream()
            .map(platformId -> platformGrpc.getPlatform(PlatformId.newBuilder().setId(platformId).build()))
            .collect(Collectors.toList());

        CompletableFuture<List<Price>> priceFuture = process.convertPriceToFuture(priceUnis);
        CompletableFuture<List<category.Category>> categoryFuture = process.convertCategoryToFuture(categoryUnis);
        CompletableFuture<List<platform.Platform>> platformFuture = process.convertPlatformToFuture(platformUnis);

        Uni<Product> aggregateResponse = Uni.combine().all().unis(
            Uni.createFrom().completionStage(priceFuture),
            Uni.createFrom().completionStage(categoryFuture),
            Uni.createFrom().completionStage(platformFuture)
        ).asTuple().onItem().transform(tuple -> buildProduct(p, tuple.getItem2(), tuple.getItem3(), tuple.getItem1()));

        return aggregateResponse;
    }

    
}
