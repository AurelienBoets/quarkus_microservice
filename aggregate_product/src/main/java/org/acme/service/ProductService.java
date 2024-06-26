package org.acme.service;

import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import product.AddProduct;
import product.ListOfProduct;
import product.ListOfSearchProduct;
import product.Product;
import product.ProductGrpc;

import jakarta.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;

import aggregate.ProductRequest;
import aggregate.SendPlatform;

@Singleton
public class ProductService {

    @GrpcClient
    private ProductGrpc productGrpc;

    public Uni<product.Product> createProduct(ProductRequest request) {
        List<String> platformsId = request.getPlatformsList().stream()
                .map(SendPlatform::getId)
                .collect(Collectors.toList());

        AddProduct product = AddProduct.newBuilder()
                .setName(request.getName())
                .setDescription(request.getDescription())
                .setImg(request.getImg())
                .setFormatImg(request.getFormatImg())
                .addAllPlatformId(platformsId)
                .addAllCategoryId(request.getCategoryIdList())
                .build();

        return productGrpc.createProduct(product);
    }

    public Uni<product.Product> findProduct(String productId) {
        return productGrpc.findProduct(product.ProductId.newBuilder().setId(productId).build());
    }

    public Uni<List<product.Product>> getAllProducts() {
        return productGrpc.getAllProduct(null).map(ListOfProduct::getProductsList);
    }

    public Uni<List<Product>> searchProducts(String keyword, int page) {
        return productGrpc.searchProduct(product.Keyword.newBuilder().setKeyword(keyword).setPage(page).build())
                .map(ListOfSearchProduct::getProductsList);
    }
}

