package org.acme.grpc;

import aggregate.*;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import price.Price;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.acme.service.CategoryService;
import org.acme.service.PlatformService;
import org.acme.service.PriceService;
import org.acme.service.ProductService;
import org.acme.utils.UniProcess;
import org.acme.utils.mapper.ProductMapper;

@GrpcService
public class AggregateGrpcService implements AggregateGrpc {

        private final UniProcess process;
        private final ProductMapper productMapper;
        private final PriceService priceService;
        private final PlatformService platformService;
        private final CategoryService categoryService;
        private final ProductService productService;

        @Inject
        public AggregateGrpcService(UniProcess process, ProductMapper productMapper,
                        ProductService productService, PriceService priceService,
                        PlatformService platformService, CategoryService categoryService) {
                this.process = process;
                this.productMapper = productMapper;
                this.productService = productService;
                this.categoryService = categoryService;
                this.platformService = platformService;
                this.priceService = priceService;
        }

        @Override
        public Uni<Product> createProduct(ProductRequest request) {
                return productService.createProduct(request).onItem()
                                .transformToUni(p -> buildProductOnCreate(p, request));
        }

        @Override
        public Uni<Product> findProduct(ProductId request) {
                return productService.findProduct(request.getId()).onItem()
                                .transformToUni(this::getInformationFromOtherService);
        }

        @Override
        public Uni<ListOfProduct> getAllProduct(Empty request) {
                return productService.getAllProducts().onItem().transformToUni(productList -> {
                        List<Uni<Product>> productUnis = productList.stream()
                                        .map(this::getInformationFromOtherService)
                                        .collect(Collectors.toList());

                        CompletableFuture<List<Product>> productFuture =
                                        process.convertProductToFuture(productUnis);

                        return Uni.createFrom().completionStage(productFuture).onItem()
                                        .transform(productMapper::buildList);
                });
        }

        @Override
        public Uni<ListOfSearchProduct> searchProduct(Keyword request) {
                return productService.searchProducts(request.getKeyword(), request.getPage())
                                .onItem().transformToUni(productList -> {
                                        List<Uni<Product>> productUnis = productList.stream()
                                                        .map(this::getInformationFromOtherService)
                                                        .collect(Collectors.toList());

                                        CompletableFuture<List<Product>> productFuture =
                                                        process.convertProductToFuture(productUnis);

                                        return Uni.createFrom().completionStage(productFuture)
                                                        .onItem()
                                                        .transform(list -> productMapper
                                                                        .buildSearchList(list,
                                                                                        (long) productList
                                                                                                        .size()));
                                });
        }

        private Uni<Product> buildProductOnCreate(product.Product p, ProductRequest request) {
                CompletableFuture<List<category.Category>> categoryFuture =
                                process.convertCategoryToFuture(categoryService
                                                .fetchCategories(p.getCategoryIdList()));
                CompletableFuture<List<Price>> priceFuture = process.convertPriceToFuture(
                                priceService.addPrices(request.getPlatformsList(), p.getId()));
                CompletableFuture<List<platform.Platform>> platformFuture =
                                process.convertPlatformToFuture(platformService
                                                .fetchPlatforms(p.getPlatformIdList()));

                return Uni.combine().all()
                                .unis(Uni.createFrom().completionStage(priceFuture),
                                                Uni.createFrom().completionStage(categoryFuture),
                                                Uni.createFrom().completionStage(platformFuture))
                                .asTuple().onItem()
                                .transform(tuple -> productMapper.buildProduct(p, tuple.getItem2(),
                                                tuple.getItem3(), tuple.getItem1()));
        }

        private Uni<Product> getInformationFromOtherService(product.Product p) {
                CompletableFuture<List<category.Category>> categoryFuture =
                                process.convertCategoryToFuture(categoryService
                                                .fetchCategories(p.getCategoryIdList()));
                CompletableFuture<List<Price>> priceFuture = process.convertPriceToFuture(
                                priceService.fetchPrices(p.getPlatformIdList(), p.getId()));
                CompletableFuture<List<platform.Platform>> platformFuture =
                                process.convertPlatformToFuture(platformService
                                                .fetchPlatforms(p.getPlatformIdList()));

                return Uni.combine().all()
                                .unis(Uni.createFrom().completionStage(priceFuture),
                                                Uni.createFrom().completionStage(categoryFuture),
                                                Uni.createFrom().completionStage(platformFuture))
                                .asTuple().onItem()
                                .transform(tuple -> productMapper.buildProduct(p, tuple.getItem2(),
                                                tuple.getItem3(), tuple.getItem1()));
        }
}
