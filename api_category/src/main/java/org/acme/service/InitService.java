package org.acme.service;

import org.acme.entity.CategoryEntity;
import org.acme.repository.CategoryRepository;
import org.bson.types.ObjectId;
import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Startup
public class InitService {
    public InitService() {
        CategoryRepository repository = new CategoryRepository();
        if (repository.listAll().isEmpty()) {
            CategoryEntity action = new CategoryEntity("Action");
            CategoryEntity adventure = new CategoryEntity("Aventure");
            CategoryEntity multi = new CategoryEntity("Multijoueur");
            CategoryEntity rpg = new CategoryEntity("RPG");
            CategoryEntity fps = new CategoryEntity("FPS");
            CategoryEntity strategy = new CategoryEntity("Stratégie");
            CategoryEntity sport = new CategoryEntity("Sport");
            CategoryEntity shoot = new CategoryEntity("Tir");

            action.setId(new ObjectId("60d5ec41f2e6d70f8b000001"));
            adventure.setId(new ObjectId("60d5ec41f2e6d70f8b000002"));
            multi.setId(new ObjectId("60d5ec41f2e6d70f8b000003"));
            rpg.setId(new ObjectId("60d5ec41f2e6d70f8b000004"));
            fps.setId(new ObjectId("60d5ec41f2e6d70f8b000005"));
            strategy.setId(new ObjectId("60d5ec41f2e6d70f8b000006"));
            sport.setId(new ObjectId("60d5ec41f2e6d70f8b000007"));
            shoot.setId(new ObjectId("60d5ec41f2e6d70f8b000008"));
            repository.persist(action, adventure, multi, rpg, fps, strategy, sport, shoot);

        }
    }
}
