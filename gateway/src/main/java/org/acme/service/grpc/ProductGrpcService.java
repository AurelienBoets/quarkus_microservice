package org.acme.service.grpc;

import java.util.ArrayList;
import java.util.List;

import org.acme.dto.product.CreateProductDto;
import org.acme.dto.product.ProductDto;
import org.acme.dto.product.SearchDto;
import org.acme.mapper.ProductMapper;
import org.acme.utils.VerifyLogin;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import aggregate.ListOfProduct;
import aggregate.ListOfSearchProduct;
import aggregate.Product;
import aggregate.AggregateGrpc;
import aggregate.Keyword;
import aggregate.ProductId;
import aggregate.ProductRequest;

@Path("api/product")
public class ProductGrpcService {
    @GrpcClient
    AggregateGrpc productGrpc;

    @Inject
    VerifyLogin verifyLogin;

    @Inject
    ProductMapper mapper;

    @Inject
    @Channel("product-outgoing")
    Emitter<CreateProductDto> productEmitter;

    @GET
    public Uni<Response> getAll() {
        return productGrpc.getAllProduct(null).onItem().transform((ListOfProduct p) -> {
            List<ProductDto> products = new ArrayList<>();
            for (Product product : p.getProductsList()) {
                products.add(mapper.productGrpcToDto(product));
            }
            return Response.ok(products).build();
        });
    }

    @GET
    @Path("/{id}")
    public Uni<Response> getById(@PathParam("id") String id) {
        return productGrpc.findProduct(ProductId.newBuilder().setId(id).build()).onItem()
                .transform((Product p) -> Response.ok(mapper.productGrpcToDto(p)).build());
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Fallback(fallbackMethod = "fallbackCreateProduct")
    public Uni<Response> createProduct(CreateProductDto productDto, @Context HttpHeaders headers) {
        return verifyLogin.isAdmin(headers).onItem().transformToUni(isAdmin -> {
            if (Boolean.FALSE.equals(isAdmin)) {
                return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED).build());
            }
            return productGrpc.createProduct(ProductRequest.newBuilder()
                    .setName(productDto.getName()).setDescription(productDto.getDescription())
                    .setImg(productDto.getImg()).setFormatImg(productDto.getFormatImg())
                    .addAllCategoryId(productDto.getCategory_id())
                    .addAllPlatforms(mapper.platformDtoToGrpc(productDto.getPlatforms())).build())
                    .onItem().transform(p -> Response.ok(mapper.productGrpcToDto(p)).build());
        });
    }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> searchProduct(@QueryParam("q") String keyword,
            @QueryParam("page") int page) {
        return productGrpc
                .searchProduct(Keyword.newBuilder().setKeyword(keyword).setPage(page).build())
                .onItem().transform((ListOfSearchProduct p) -> {
                    List<ProductDto> products = new ArrayList<>();
                    for (Product product : p.getProductsList()) {
                        products.add(mapper.productGrpcToDto(product));
                    }
                    return Response.ok(new SearchDto(products, (int) p.getPageCount())).build();
                });
    }

    public Uni<Response> fallbackCreateProduct(CreateProductDto productDto,
            @Context HttpHeaders headers) {
        return verifyLogin.isAdmin(headers).onItem().transformToUni(isAdmin -> {
            if (Boolean.FALSE.equals(isAdmin)) {
                return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED).build());
            }
            productEmitter.send(productDto);
            return Uni.createFrom()
                    .item(Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(
                            "Le service produit est actuellement indisponible. Le produit sera créé dès que possible.")
                            .build());
        });
    }
}
