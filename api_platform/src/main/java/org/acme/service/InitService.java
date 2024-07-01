package org.acme.service;

import java.util.logging.Logger;

import org.acme.entity.PlatformEntity;
import org.acme.repository.PlatformRepository;
import org.bson.types.ObjectId;
import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;

@Startup
@ApplicationScoped
public class InitService {

    public InitService() {
        PlatformRepository repository = new PlatformRepository();
        if (repository.listAll().isEmpty()) {
            PlatformEntity ps4 = PlatformEntity.builder().name("PS4").build();
            PlatformEntity ps5 = PlatformEntity.builder().name("PS5").build();
            PlatformEntity xbox = PlatformEntity.builder().name("Xbox").build();
            PlatformEntity steam = PlatformEntity.builder().name("Steam").build();
            PlatformEntity nintendo = PlatformEntity.builder().name("Switch").build();
            ps4.setId(new ObjectId("60d5ec41f2e6d70f8b000009"));
            ps5.setId(new ObjectId("60d5ec41f2e6d70f8b000010"));
            xbox.setId(new ObjectId("60d5ec41f2e6d70f8b000011"));
            steam.setId(new ObjectId("60d5ec41f2e6d70f8b000012"));
            nintendo.setId(new ObjectId("60d5ec41f2e6d70f8b000013"));
            repository.persist(ps4, ps5, xbox, steam, nintendo);
            Logger logger = Logger.getLogger(getClass().getName());
            logger.info("CREATE PLATFORM");
        }
    }
}
