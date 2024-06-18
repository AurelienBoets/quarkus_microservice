package org.acme.service.grpc;

import java.util.ArrayList;
import java.util.List;

import org.acme.Dto.Category.CategoryDto;
import org.acme.Dto.Platform.PlatformProductDto;
import org.acme.Dto.Platform.SendPlatformDto;
import org.acme.Dto.Product.CreateProductDto;
import org.acme.Dto.Product.ProductDto;
import org.acme.utils.VerifyLogin;


import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import aggregate.Category;
import aggregate.ListOfProduct;
import aggregate.Platform;
import aggregate.Product;
import aggregate.AggregateGrpc;
import aggregate.ProductId;
import aggregate.ProductRequest;
import aggregate.SendPlatform;

@Path("api/product")
public class ProductGrpcService {
    @GrpcClient
    AggregateGrpc productGrpc;
    
    @Inject
    VerifyLogin verifyLogin;

    @GET
    public Uni<Response> getAll(){
        return productGrpc.getAllProduct(null).onItem().transform((ListOfProduct p)->{
            List<ProductDto> products=new ArrayList<>();
            for(Product product:p.getProductsList()){
                products.add(productGrpcToDto(product));
            }
            return Response.ok(products).build();
        });
    }

    @GET
    @Path("/{id}")
    public Uni<Response> getById(@PathParam("id") String id){
        return productGrpc.findProduct(ProductId.newBuilder().setId(id).build()).onItem().transform((Product p)->{
            return Response.ok(productGrpcToDto(p)).build();
        });
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> createProduct(CreateProductDto productDto,@Context HttpHeaders headers){
        return verifyLogin.isAdmin(headers)
                .onItem().transformToUni(isAdmin -> {
                    if (!isAdmin) {
                        return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED).build());
                    }
                    return productGrpc.createProduct(ProductRequest.newBuilder()
                            .setName(productDto.getName())
                            .setDescription(productDto.getDescription())
                            .setImg(productDto.getImg())
                            .addAllCategoryId(productDto.getCategory_id())
                            .addAllPlatforms(platformDtoToGrpc(productDto.getPlatforms()))
                            .build())
                            .onItem().transform(p -> Response.ok(productGrpcToDto(p)).build())
                            ;
                });
    }


    private ProductDto productGrpcToDto(Product product){
        List<PlatformProductDto> platforms=new ArrayList<>();
        for(Platform platform:product.getPlatformsList()){
            platforms.add(new PlatformProductDto(platform.getId(),platform.getName(),platform.getPrice()));
        }
        List<CategoryDto> categories=new ArrayList<>();
        for(Category category:product.getCategoriesList()){
            categories.add(new CategoryDto(category.getId(),category.getName()));
        }
        return new ProductDto(product.getId(), product.getName(), product.getDescription(), product.getImg(), categories,platforms);
    }

    private List<SendPlatform> platformDtoToGrpc(List<SendPlatformDto> platformDto){
        List<SendPlatform> platforms=new ArrayList<>();
        for(SendPlatformDto dto:platformDto){
            platforms.add(SendPlatform.newBuilder().setId(dto.getId()).setPrice(dto.getPrice()).build());
        }
        return platforms;
    }

}  
