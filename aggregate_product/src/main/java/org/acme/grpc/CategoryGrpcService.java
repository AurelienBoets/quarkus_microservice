package org.acme.grpc;

import category.AddCategory;
import category.Category;
import category.CategoryGrpc;
import category.CategoryId;
import category.Empty;
import category.ListOfCategory;
import io.quarkus.grpc.GrpcClient;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;

@GrpcService
public class CategoryGrpcService implements CategoryGrpc {
    
    @GrpcClient
    CategoryGrpc categoryGrpc;

    @Override
    public Uni<Category> createCategory(AddCategory category){
        return categoryGrpc.createCategory(category);
    }

    @Override
    public Uni<Category> getCategory(CategoryId request) {
        return categoryGrpc.getCategory(request);
    }

    @Override
    public Uni<ListOfCategory> getAllCategory(Empty request) {
        return categoryGrpc.getAllCategory(null);
    }
}
