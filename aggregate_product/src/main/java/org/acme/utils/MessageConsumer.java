package org.acme.utils;

import io.smallrye.mutiny.Uni;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.acme.dto.ProductMessageDto;
import org.acme.utils.mapper.ProductMessageMapper;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import io.quarkus.grpc.GrpcClient;
import io.vertx.core.json.JsonObject;
import jakarta.inject.Singleton;
import jakarta.inject.Inject;
import aggregate.AggregateGrpc;
import aggregate.Product;

@Singleton
public class MessageConsumer {
    private static final Logger LOGGER = Logger.getLogger(MessageConsumer.class.getName());

    @GrpcClient("aggregateGrpc")
    private AggregateGrpc service;

    @Inject
    ProductMessageMapper mapper;

    @Incoming("product-incoming")
    public Uni<Product> consume(JsonObject message) {
        LOGGER.info("Traitement du message: " + message.getString("name"));
        try {
            ProductMessageDto product = message.mapTo(ProductMessageDto.class);
            return service.createProduct(mapper.messageToGrpc(product));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du traitement du produit", e);
            return null;
        }
    }

}
