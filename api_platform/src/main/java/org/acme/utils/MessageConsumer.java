package org.acme.utils;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.acme.entity.PlatformEntity;
import org.acme.repository.PlatformRepository;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class MessageConsumer {
    private static final Logger LOGGER = Logger.getLogger(MessageConsumer.class.getName());

    @Inject
    PlatformRepository repository;

    @Incoming("platform-incoming")
    public void consume(JsonObject message) {
        LOGGER.info("Traitement du message: " + message.getString("name"));
        try {
            repository.persist(PlatformEntity.builder().name(message.getString("name")).build());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du traitement de la plateforme", e);
        }
    }

}
