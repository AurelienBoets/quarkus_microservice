package org.acme.service;

import org.acme.entity.PlatformEntity;
import org.acme.repository.PlatformRepository;

import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;

@Startup
@ApplicationScoped
public class InitService {
    
    public InitService(){
        PlatformRepository repostory=new PlatformRepository();
        if(repostory.listAll().size()==0){
            PlatformEntity ps4=PlatformEntity.builder().name("PS4").build();
            PlatformEntity ps5=PlatformEntity.builder().name("PS5").build();
            PlatformEntity xbox=PlatformEntity.builder().name("Xbox").build();
            PlatformEntity steam=PlatformEntity.builder().name("Steam").build();
            PlatformEntity nintendo=PlatformEntity.builder().name("Switch").build();
            repostory.persist(ps4,ps5,xbox,steam,nintendo);
            System.out.println("CREATE PLAFORM");
        }
    }
}
