package org.acme.utils;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import aggregate.Product;
import category.Category;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import platform.Platform;
import price.Price;

@ApplicationScoped
public class UniProcess {

    public CompletableFuture<List<Price>> convertPriceToFuture(List<Uni<Price>> uniList) {
        List<CompletableFuture<Price>> completableFutures = uniList.stream()
                .map(uni -> uni.subscribe().asCompletionStage().toCompletableFuture())
                .toList();

        CompletableFuture<Void> allOf = CompletableFuture.allOf(
                completableFutures.toArray(new CompletableFuture[0])
        );

        return allOf.thenApply(ignored ->
                completableFutures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList())
        );
    }

    public CompletableFuture<List<Category>> convertCategoryToFuture(List<Uni<Category>> uniList) {
        List<CompletableFuture<Category>> completableFutures = uniList.stream()
            .map(uni -> uni.subscribe().asCompletionStage().toCompletableFuture())
            .toList();

        CompletableFuture<Void> allOf = CompletableFuture.allOf(
            completableFutures.toArray(new CompletableFuture[0])
        );

        return allOf.thenApply(ignored ->
            completableFutures.stream()
                .map(CompletableFuture::join) 
                .collect(Collectors.toList())
        );
    }

    public CompletableFuture<List<Platform>> convertPlatformToFuture(List<Uni<Platform>> uniList) {
        List<CompletableFuture<Platform>> completableFutures = uniList.stream()
            .map(uni -> uni.subscribe().asCompletionStage().toCompletableFuture())
            .toList();

        CompletableFuture<Void> allOf = CompletableFuture.allOf(
            completableFutures.toArray(new CompletableFuture[0])
        );

        return allOf.thenApply(ignored ->
            completableFutures.stream()
                .map(CompletableFuture::join) 
                .collect(Collectors.toList())
        );
    }

        public CompletableFuture<List<Product>> convertProductToFuture(List<Uni<Product>> uniList) {
        List<CompletableFuture<Product>> completableFutures = uniList.stream()
            .map(uni -> uni.subscribe().asCompletionStage().toCompletableFuture())
            .toList();

        CompletableFuture<Void> allOf = CompletableFuture.allOf(
            completableFutures.toArray(new CompletableFuture[0])
        );

        return allOf.thenApply(ignored ->
            completableFutures.stream()
                .map(CompletableFuture::join) 
                .collect(Collectors.toList())
        );
    }


}
