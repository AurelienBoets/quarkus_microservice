package org.acme.mapper;

import java.util.ArrayList;
import java.util.List;

import org.acme.dto.category.CategoryDto;
import org.acme.dto.platform.PlatformProductDto;
import org.acme.dto.platform.SendPlatformDto;
import org.acme.dto.product.ProductDto;

import aggregate.Category;
import aggregate.Platform;
import aggregate.Product;
import aggregate.SendPlatform;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProductMapper {
    public ProductDto productGrpcToDto(Product product){
        List<PlatformProductDto> platforms=new ArrayList<>();
        for(Platform platform:product.getPlatformsList()){
            platforms.add(new PlatformProductDto(platform.getId(),platform.getName(),platform.getPrice()));
        }
        List<CategoryDto> categories=new ArrayList<>();
        for(Category category:product.getCategoriesList()){
            categories.add(new CategoryDto(category.getId(),category.getName()));
        }
        return new ProductDto(product.getId(), product.getName(), product.getDescription(), product.getImg(),product.getFormatImg(), categories,platforms);
    }

    public List<SendPlatform> platformDtoToGrpc(List<SendPlatformDto> platformDto){
        List<SendPlatform> platforms=new ArrayList<>();
        for(SendPlatformDto dto:platformDto){
            platforms.add(SendPlatform.newBuilder().setId(dto.getId()).setPrice(dto.getPrice()).build());
        }
        return platforms;
    }
}
