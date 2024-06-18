package org.acme.service;

import java.util.List;

import org.acme.entity.ProductEntity;
import org.acme.repository.ProductRepository;
import org.acme.utils.UploadImg;
import org.bson.types.ObjectId;


import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import product.AddProduct;
import product.CategoryId;
import product.Empty;
import product.ListOfProduct;
import product.PlatformId;
import product.Product;
import product.ProductGrpc;
import product.ProductId;

@GrpcService
public class ProductGrpcService implements ProductGrpc {

    @Inject
    UploadImg uploadImg;

    @Inject
    ProductRepository repository;

    @Override
    public Uni<Product> createProduct(AddProduct request) {
        String fileName=uploadImg.upload(request.getImg(), request.getName());
        if(fileName!=null){
            ProductEntity entity=ProductEntity.builder()
            .name(request.getName())
            .description(request.getDescription())
            .img(fileName)
            .categoryId(request.getCategoryIdList())
            .platformId(request.getPlatformIdList())
            .build();
            return entity.persist().replaceWith(entity).onItem().transform(p->{
                return Product.newBuilder()
                              .setId(entity.getId().toString())
                              .setName(entity.getName())
                              .setDescription(entity.getDescription())
                              .setImg(request.getImg())
                              .addAllCategoryId(entity.getCategoryId())
                              .addAllPlatformId(entity.getPlatformId())
                              .build();
            });
        }
        throw new RuntimeException("Error with upload image");
    }

    @Override
    public Uni<Product> findProduct(ProductId request) {
        try{
        return ProductEntity.findById(new ObjectId(request.getId())).onItem().transform(p->{
            ProductEntity entity=(ProductEntity) p;
            String img=uploadImg.getImg(entity.getImg());
            return Product.newBuilder()
                          .setId(request.getId())
                          .setName(entity.getName())
                          .setDescription(entity.getDescription())
                          .setImg(img)
                          .addAllCategoryId(entity.getCategoryId())
                          .addAllPlatformId(entity.getPlatformId())
                          .build();  
        });
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        throw new RuntimeException("Product doesn't exist");
    }

    @Override
    public Uni<ListOfProduct> getAllProduct(Empty request) {
        return ProductEntity.listAll().onItem().transform(list->{
            List<Product> products=list.stream().map(p->{
                ProductEntity entity=(ProductEntity) p;
                String img=uploadImg.getImg(entity.getImg());
                return Product.newBuilder()
                          .setId(entity.getId().toString())
                          .setName(entity.getName())
                          .setDescription(entity.getDescription())
                          .setImg(img)
                          .addAllCategoryId(entity.getCategoryId())
                          .addAllPlatformId(entity.getPlatformId())
                          .build(); 
            }).toList();
            return ListOfProduct.newBuilder().addAllProducts(products).build();
        });
    }

    @Override
    public Uni<ListOfProduct> getByCategory(CategoryId request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getByCategory'");
    }

    @Override
    public Uni<ListOfProduct> getByPlatform(PlatformId request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getByPlatform'");
    }
    
}
