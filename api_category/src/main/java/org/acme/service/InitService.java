package org.acme.service;

import org.acme.entity.CategoryEntity;
import org.acme.repository.CategoryRepository;
import org.bson.types.ObjectId;

import io.quarkus.runtime.Startup;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Startup
public class InitService {
    public InitService(){
        CategoryRepository repository=new CategoryRepository();
        if(repository.listAll().size()==0){
            CategoryEntity action=CategoryEntity.builder().id(new ObjectId("666ab46bef9a230206d64a71")).name("Action").build(); 
            CategoryEntity aventure=CategoryEntity.builder().id(new ObjectId("666ab46bef9a230206d64a72")).name("Aventure").build(); 
            CategoryEntity multi=CategoryEntity.builder().id(new ObjectId("666ab46bef9a230206d64a73")).name("Multijoueur").build(); 
            CategoryEntity rpg=CategoryEntity.builder().id(new ObjectId("666ab46bef9a230206d64a74")).name("RPG").build(); 
            CategoryEntity fps=CategoryEntity.builder().id(new ObjectId("666ab46bef9a230206d64a75")).name("FPS").build(); 
            CategoryEntity strategy=CategoryEntity.builder().id(new ObjectId("666ab46bef9a230206d64a76")).name("Stratégie").build(); 
            CategoryEntity sport=CategoryEntity.builder().id(new ObjectId("666ab46bef9a230206d64a7a")).name("Sport").build(); 
            CategoryEntity tir=CategoryEntity.builder().id(new ObjectId("666ab46bef9a230206d64a7b")).name("Tir").build();
            repository.persist(action,aventure,multi,rpg,fps,strategy,sport,tir);
        }
    }
}
