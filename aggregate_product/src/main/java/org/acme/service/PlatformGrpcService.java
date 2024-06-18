package org.acme.service;

import io.quarkus.grpc.GrpcClient;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import platform.AddPlatform;
import platform.Empty;
import platform.ListOfPlatform;
import platform.Platform;
import platform.PlatformGrpc;
import platform.PlatformId;

@GrpcService
public class PlatformGrpcService implements PlatformGrpc {
    
    @GrpcClient
    PlatformGrpc platformGrpc;

    @Override
    public Uni<Platform> getPlatform(PlatformId id){
        return platformGrpc.getPlatform(id);
    }

    @Override
    public Uni<ListOfPlatform> getAllPlatform(Empty request){
        return platformGrpc.getAllPlatform(request);
    }

    @Override
    public Uni<Platform> createPlatform(AddPlatform platform){
        return platformGrpc.createPlatform(platform);
    }
}
