package org.acme.service;

import org.acme.entity.PlatformEntity;
import org.acme.repository.PlatformRepository;
import org.bson.types.ObjectId;

import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;

@Startup
@ApplicationScoped
public class InitService {
    
    public InitService(){
        PlatformRepository repository=new PlatformRepository();
        if(repository.listAll().size()==0){
            PlatformEntity ps4=PlatformEntity.builder().name("PS4").id(new ObjectId("6662da54204dce0c72371122")).build();
            PlatformEntity ps5=PlatformEntity.builder().name("PS5").id(new ObjectId("6662da54204dce0c72371123")).build();
            PlatformEntity xbox=PlatformEntity.builder().name("Xbox").id(new ObjectId("6662da54204dce0c72371124")).build();
            PlatformEntity steam=PlatformEntity.builder().name("Steam").id(new ObjectId("6662da54204dce0c72371125")).build();
            PlatformEntity nintendo=PlatformEntity.builder().name("Switch").id(new ObjectId("6662da54204dce0c72371126")).build();
            repository.persist(ps4,ps5,xbox,steam,nintendo);
            System.out.println("CREATE PLAFORM");
        }
    }
}
