package org.acme.service.grpc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.acme.Dto.Platform.PlatformDto;
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

    @GET
    public Uni<Response> getAll(){
        return platformGrpc.getAllPlatform(null).onItem().transform((ListOfPlatform p)->{
            List<PlatformDto>platforms=new ArrayList<>();
            for(Platform platform:p.getPlatformsList()){
                platforms.add(platformGrpcToDto(platform));
            }
            return Response.ok(platforms).build();
        });
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> createCategory(Map<String,String> request,@Context HttpHeaders headers){
        return verifyLogin.isAdmin(headers)
                .onItem().transformToUni(isAdmin -> {
                    if (!isAdmin) {
                        return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED).build());
                    }
                return platformGrpc.createPlatform(AddPlatform.newBuilder().setName(request.get("name")).build())
                .onItem().transform((Platform p)->{
                    return Response.ok(platformGrpcToDto(p)).build();
                });
            });
        }
        
    @Path("/{id}")
    @GET
    public Uni<Response> getById(@PathParam("id") String id){
        return platformGrpc.getPlatform(PlatformId.newBuilder().setId(id).build()).onItem().transform((Platform p)->{
            return Response.ok(platformGrpcToDto(p)).build();
        });
    }

    private PlatformDto platformGrpcToDto(Platform platform){
        return new PlatformDto(platform.getId(), platform.getName());
    }

}
