package org.acme.service;

import java.util.List;
import java.util.stream.Collectors;

import org.acme.entity.CategoryEntity;
import org.bson.types.ObjectId;

import category.AddCategory;
import category.Category;
import category.CategoryGrpc;
import category.CategoryId;
import category.Empty;
import category.ListOfCategory;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;

@GrpcService
public class CategoryServiceGrpc implements CategoryGrpc {

    @Override
    public Uni<Category> createCategory(AddCategory request) {
        CategoryEntity category = CategoryEntity.builder().name(request.getName()).build();
        return category.persist().replaceWith(category).onItem().transform(
                c -> Category.newBuilder().setName(c.getName()).setId(c.id.toString()).build());
    }

    @Override
    public Uni<Category> getCategory(CategoryId request) {
        return CategoryEntity.findById(new ObjectId(request.getId())).onItem().ifNotNull()
                .transform(c -> {
                    CategoryEntity categoryEntity = (CategoryEntity) c;
                    return Category.newBuilder().setId(categoryEntity.id.toString())
                            .setName(categoryEntity.getName()).build();
                });

    }

    @Override
    public Uni<ListOfCategory> getAllCategory(Empty request) {
        return CategoryEntity.listAll().onItem().transform(list -> {
            List<Category> categories = list.stream().map(c -> {
                CategoryEntity categoryEntity = (CategoryEntity) c;
                return Category.newBuilder().setId(categoryEntity.id.toString())
                        .setName(categoryEntity.getName()).build();
            }).collect(Collectors.toList());
            return ListOfCategory.newBuilder().addAllCategories(categories).build();
        });
    }
}
