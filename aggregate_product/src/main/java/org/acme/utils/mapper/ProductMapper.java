package org.acme.utils.mapper;

import java.util.ArrayList;
import java.util.List;


import aggregate.Category;
import aggregate.ListOfProduct;
import aggregate.Platform;
import aggregate.Product;
import jakarta.enterprise.context.ApplicationScoped;
import price.Price;

@ApplicationScoped
public class ProductMapper {
    

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
}
