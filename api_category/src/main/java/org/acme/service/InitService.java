package org.acme.service;

import org.acme.entity.CategoryEntity;
import org.acme.repository.CategoryRepository;

import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Startup
public class InitService {
    public InitService(){
        CategoryRepository repository=new CategoryRepository();
        if(!repository.listAll().isEmpty()){
            CategoryEntity action=new CategoryEntity("Action"); 
            CategoryEntity adventure=new CategoryEntity("Aventure"); 
            CategoryEntity multi=new CategoryEntity("Multijoueur"); 
            CategoryEntity rpg=new CategoryEntity("RPG"); 
            CategoryEntity fps=new CategoryEntity("FPS"); 
            CategoryEntity strategy=new CategoryEntity("Stratégie");  
            CategoryEntity sport=new CategoryEntity("Sport"); 
            CategoryEntity shoot=new CategoryEntity("Tir"); 
            repository.persist(action,adventure,multi,rpg,fps,strategy,sport,shoot);
        }
    }
}
