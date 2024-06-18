package org.acme.service.grpc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.acme.Dto.Category.CategoryDto;
import org.acme.utils.VerifyLogin;

import category.AddCategory;
import category.Category;
import category.CategoryGrpc;
import category.CategoryId;
import category.ListOfCategory;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("api/category")
public class CategoryGrpcService {
    @GrpcClient
    CategoryGrpc categoryGrpc;

    @Inject
    VerifyLogin verifyLogin;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getAll(){
        return categoryGrpc.getAllCategory(null).onItem().transform((ListOfCategory c)->{
            List<CategoryDto>categories=new ArrayList<>();
            for(Category category:c.getCategoriesList()){
                categories.add(categoryGrpcToDto(category));
            }
            return Response.ok(categories).build();
        });
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> createCategory(Map<String, String> request, @Context HttpHeaders headers) {
        return verifyLogin.isAdmin(headers)
                .onItem().transformToUni(isAdmin -> {
                    if (!isAdmin) {
                        return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED).build());
                    }
                    return categoryGrpc.createCategory(AddCategory.newBuilder().setName(request.get("name")).build())
                            .onItem().transform((Category c) -> {
                                return Response.status(201).entity(categoryGrpcToDto(c)).build();
                            });
                });
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getById(@PathParam("id") String id){
        return categoryGrpc.getCategory(CategoryId.newBuilder().setId(id).build()).onItem().transform((Category c)->{
            return Response.ok(categoryGrpcToDto(c)).build();
        });
    }

    

    private CategoryDto categoryGrpcToDto(Category category){
        return new CategoryDto(category.getId(), category.getName());
    }


}
