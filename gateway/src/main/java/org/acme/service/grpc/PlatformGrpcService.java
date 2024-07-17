package org.acme.service.grpc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.acme.dto.platform.PlatformDto;
import org.acme.utils.VerifyLogin;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
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
import platform.AddPlatform;
import platform.ListOfPlatform;
import platform.Platform;
import platform.PlatformGrpc;
import platform.PlatformId;

@Path("api/platform")
public class PlatformGrpcService {
    @GrpcClient
    PlatformGrpc platformGrpc;

    @Inject
    VerifyLogin verifyLogin;

    @Inject
    @Channel("platform-outgoing")
    Emitter<JsonObject> platformEmitter;


    @GET
    public Uni<Response> getAll() {
        return platformGrpc.getAllPlatform(null).onItem().transform((ListOfPlatform p) -> {
            List<PlatformDto> platforms = new ArrayList<>();
            for (Platform platform : p.getPlatformsList()) {
                platforms.add(platformGrpcToDto(platform));
            }
            return Response.ok(platforms).build();
        });
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Fallback(fallbackMethod = "fallbackCreatePlatform")
    @Timeout(value = 5000)
    public Uni<Response> createPlatform(Map<String, String> request, @Context HttpHeaders headers) {
        return verifyLogin.isAdmin(headers).onItem().transformToUni(isAdmin -> {
            if (Boolean.FALSE.equals(isAdmin)) {
                return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED).build());
            }
            return platformGrpc
                    .createPlatform(AddPlatform.newBuilder().setName(request.get("name")).build())
                    .onItem().transform((Platform p) -> Response.ok(platformGrpcToDto(p)).build());
        });
    }

    @Path("/{id}")
    @GET
    public Uni<Response> getById(@PathParam("id") String id) {
        return platformGrpc.getPlatform(PlatformId.newBuilder().setId(id).build()).onItem()
                .transform((Platform p) -> Response.ok(platformGrpcToDto(p)).build());
    }

    private PlatformDto platformGrpcToDto(Platform platform) {
        return new PlatformDto(platform.getId(), platform.getName());
    }

    public Uni<Response> fallbackCreatePlatform(Map<String, String> request,
            @Context HttpHeaders headers) {
        return verifyLogin.isAdmin(headers).onItem().transformToUni(isAdmin -> {
            if (Boolean.FALSE.equals(isAdmin)) {
                return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED).build());
            }
            platformEmitter.send(JsonObject.mapFrom(request));
            return Uni.createFrom()
                    .item(Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(
                            "Le service plateforme est actuellement indisponible. La plateforme sera créé dès que possible.")
                            .build());
        });
    }
}
