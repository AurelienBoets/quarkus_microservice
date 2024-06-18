package org.acme.service;

import io.quarkus.grpc.GrpcClient;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import product.AddProduct;
import product.CategoryId;
import product.Empty;
import product.Keyword;
import product.ListOfProduct;
import product.ListOfSearchProduct;
import product.PlatformId;
import product.Product;
import product.ProductGrpc;
import product.ProductId;

@GrpcService
public class ProductGrpcService implements ProductGrpc{

    @GrpcClient
    ProductGrpc productGrpc;

    @Override
    public Uni<ListOfProduct> getAllProduct(Empty request){
        return productGrpc.getAllProduct(request);
    }

    @Override
    public Uni<Product> createProduct(AddProduct request){
        return productGrpc.createProduct(request);
    }

    @Override
    public Uni<Product> findProduct(ProductId request) {
        return productGrpc.findProduct(request);
    }

    @Override
    public Uni<ListOfProduct> getByCategory(CategoryId request) {
        return productGrpc.getByCategory(request);
    }

    @Override
    public Uni<ListOfProduct> getByPlatform(PlatformId request) {
        return productGrpc.getByPlatform(request);
    }

    @Override
    public Uni<ListOfSearchProduct> searchProduct(Keyword request) {
        return productGrpc.searchProduct(request);
    }
}