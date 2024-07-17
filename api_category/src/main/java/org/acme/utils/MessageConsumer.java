package org.acme.utils;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.acme.entity.CategoryEntity;
import org.acme.repository.CategoryRepository;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class MessageConsumer {
    private static final Logger LOGGER = Logger.getLogger(MessageConsumer.class.getName());

    @Inject
    CategoryRepository repository;

    @Incoming("category-incoming")
    public void consume(JsonObject message) {
        LOGGER.info("Traitement du message: " + message.getString("name"));
        try {
            repository.persist(CategoryEntity.builder().name(message.getString("name")).build());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du traitement de la category", e);
        }
    }

}
