package org.acme.utils;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.acme.dto.CreateOrderMessageDto;
import org.acme.service.OrderService;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import io.vertx.core.json.JsonObject;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import order.AddOrder;

@Singleton
public class MessageConsumer {

    private static final Logger LOGGER = Logger.getLogger(MessageConsumer.class.getName());

    @Inject
    OrderService service;

    @Incoming("order-incoming")
    public void consume(JsonObject message) {
        LOGGER.info("Traitement de message");
        try {
            CreateOrderMessageDto orderDto = message.mapTo(CreateOrderMessageDto.class);
            service.createOrder(AddOrder.newBuilder().setIdUser(orderDto.getIdUser())
                    .setStripeSession(orderDto.getSessionId()).build());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du traitement de la commande", e);
        }
    }
}
