package org.acme.service.grpc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.acme.dto.category.CategoryDto;
import org.acme.utils.VerifyLogin;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import category.AddCategory;
import category.Category;
import category.CategoryGrpc;
import category.CategoryId;
import category.ListOfCategory;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
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

    private final VerifyLogin verifyLogin;

    @Inject
    @Channel("category-outgoing")
    Emitter<JsonObject> categoryEmitter;

    @Inject
    public CategoryGrpcService(VerifyLogin verifyLogin) {
        this.verifyLogin = verifyLogin;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getAll() {
        return categoryGrpc.getAllCategory(null).onItem().transform((ListOfCategory c) -> {
            List<CategoryDto> categories = new ArrayList<>();
            for (Category category : c.getCategoriesList()) {
                categories.add(categoryGrpcToDto(category));
            }
            return Response.ok(categories).build();
        });
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Fallback(fallbackMethod = "fallbackCreateCategory")
    public Uni<Response> createCategory(Map<String, String> request, @Context HttpHeaders headers) {
        return verifyLogin.isAdmin(headers).onItem().transformToUni(isAdmin -> {
            if (Boolean.FALSE.equals(isAdmin)) {
                return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED).build());
            }
            return categoryGrpc
                    .createCategory(AddCategory.newBuilder().setName(request.get("name")).build())
                    .onItem().transform((Category c) -> Response.status(201)
                            .entity(categoryGrpcToDto(c)).build());
        });
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getById(@PathParam("id") String id) {
        return categoryGrpc.getCategory(CategoryId.newBuilder().setId(id).build()).onItem()
                .transform((Category c) -> Response.ok(categoryGrpcToDto(c)).build());
    }


    private CategoryDto categoryGrpcToDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }

    public Uni<Response> fallbackCreateCategory(Map<String, String> request,
            @Context HttpHeaders headers) {
        return verifyLogin.isAdmin(headers).onItem().transformToUni(isAdmin -> {
            if (Boolean.FALSE.equals(isAdmin)) {
                return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED).build());
            }
            categoryEmitter.send(JsonObject.mapFrom(request));
            return Uni.createFrom()
                    .item(Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(
                            "Le service category est actuellement indisponible. La category sera créé dès que possible.")
                            .build());
        });
    }

}
