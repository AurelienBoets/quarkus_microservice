package org.acme.service;

import category.Category;
import category.CategoryGrpc;
import category.CategoryId;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;

import jakarta.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class CategoryService {

    @GrpcClient
    private CategoryGrpc categoryGrpc;

    public List<Uni<Category>> fetchCategories(List<String> categoryIds) {
        return categoryIds.stream()
                .map(categoryId -> categoryGrpc.getCategory(CategoryId.newBuilder().setId(categoryId).build()))
                .collect(Collectors.toList());
    }
}
