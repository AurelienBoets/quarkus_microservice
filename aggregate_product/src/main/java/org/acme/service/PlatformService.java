package org.acme.service;

import platform.Platform;
import platform.PlatformGrpc;
import platform.PlatformId;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;

import jakarta.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class PlatformService {

    @GrpcClient
    private PlatformGrpc platformGrpc;

    public List<Uni<Platform>> fetchPlatforms(List<String> platformIds) {
        return platformIds.stream()
                .map(platformId -> platformGrpc.getPlatform(PlatformId.newBuilder().setId(platformId).build()))
                .collect(Collectors.toList());
    }
}
