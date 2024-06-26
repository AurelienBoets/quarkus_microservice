package org.acme.service;

import java.util.List;
import java.util.stream.Collectors;

import org.acme.entity.PlatformEntity;
import org.bson.types.ObjectId;

import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import platform.*;

@GrpcService
public class PlatformGrpcService implements PlatformGrpc{
    @Override
    public Uni<Platform> createPlatform(AddPlatform request) {
        PlatformEntity entity=PlatformEntity.builder().name(request.getName()).build();
        return entity.persist().replaceWith(entity).onItem().transform(p->Platform.newBuilder()
                            .setId(entity.id.toString())
                            .setName(entity.getName())
                            .build());
    }

    @Override
    public Uni<Platform> getPlatform(PlatformId request) {
        return PlatformEntity.findById(new ObjectId(request.getId())).onItem().transform(p->{
            PlatformEntity entity=(PlatformEntity) p;
            return Platform.newBuilder()
                            .setId(entity.id.toString())
                            .setName(entity.getName())
                            .build();
        });
    }

    @Override
    public Uni<ListOfPlatform> getAllPlatform(Empty request) {
        return PlatformEntity.listAll().onItem().transform(list->{
            List<Platform> platforms=list.stream().map(p->{
                PlatformEntity entity=(PlatformEntity) p;
                return Platform.newBuilder()
                            .setId(entity.id.toString())
                            .setName(entity.getName())
                            .build();
            }).collect(Collectors.toList());
            return ListOfPlatform.newBuilder().addAllPlatforms(platforms).build();
        });
    }


        
}
