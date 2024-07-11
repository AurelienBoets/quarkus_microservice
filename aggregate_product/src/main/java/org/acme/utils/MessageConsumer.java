package org.acme.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import aggregate.AggregateGrpc;
import aggregate.ProductRequest;
import aggregate.SendPlatform;
import io.quarkus.grpc.GrpcClient;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MessageConsumer {
    private static final Logger LOGGER = Logger.getLogger(MessageConsumer.class.getName());

    @GrpcClient
    AggregateGrpc service;

    @Incoming("product-incoming")
    public void consume(JsonObject product) {
        LOGGER.info("Traitement du message: " + product.getString("name"));
        try {
            List<String> categoryList = new ArrayList<>();
            for (int i = 0; i < product.getJsonArray("category_id").size(); i++) {
                categoryList.add(product.getJsonArray("category_id").getString(i));
            }
            List<SendPlatform> platforms = new ArrayList<>();
            for (int i = 0; i < product.getJsonArray("platforms").size(); i++) {
                JsonObject jsonObject = product.getJsonArray("platforms").getJsonObject(i);
                platforms.add(SendPlatform.newBuilder().setId(jsonObject.getString("id"))
                        .setPrice(jsonObject.getDouble("price")).build());
            }
            ProductRequest request = ProductRequest.newBuilder().setName(product.getString("name"))
                    .setDescription(product.getString("description"))
                    .setFormatImg(product.getString("formatImg")).setImg(product.getString("img"))
                    .addAllCategoryId(categoryList).addAllPlatforms(platforms).build();
            service.createProduct(request);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du traitement du produit", e);
        }
    }

}
