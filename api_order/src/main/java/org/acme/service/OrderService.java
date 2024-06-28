package org.acme.service;

import java.util.List;
import org.acme.entity.OrderEntity;
import org.acme.utils.OrderMapper;

import com.stripe.exception.StripeException;
import com.stripe.model.LineItemCollection;
import com.stripe.model.checkout.Session;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import order.AddOrder;
import order.Order;

@ApplicationScoped
public class OrderService {

    private final OrderMapper orderMapper;
    private final StripeService stripeService;

    public OrderService(OrderMapper orderMapper, StripeService stripeService) {
        this.orderMapper = orderMapper;
        this.stripeService = stripeService;
    }

    public Uni<Order> getOrderById(String id) {
        return OrderEntity.findById(new org.bson.types.ObjectId(id)).onItem().transform(o -> {
            OrderEntity entity = (OrderEntity) o;
            return orderMapper.entityToOrder(entity);
        });
    }

    public Uni<Order> createOrder(AddOrder request) {
        return Uni.createFrom().item(request.getStripeSession()).onItem()
                .transformToUni(this::retrieveSessionFromStripe).onItem()
                .transformToUni(lineItem -> checkAndPersistOrder(lineItem, request));
    }

    public Uni<List<Order>> getAllOrdersByUserId(Long userId) {
        return OrderEntity.find("id_user = ?1", userId).list().onItem()
                .transform(list -> list.stream().map(o -> {
                    OrderEntity entity = (OrderEntity) o;
                    return orderMapper.entityToOrder(entity);
                }).toList());
    }

    private Uni<LineItemCollection> retrieveSessionFromStripe(String sessionId) {
        try {
            return Uni.createFrom().item(stripeService.retrieveSessionLineItems(sessionId));
        } catch (StripeException e) {
            return Uni.createFrom().failure(e);
        }
    }

    private Uni<Order> checkAndPersistOrder(LineItemCollection collectionItem, AddOrder request) {
        try {
            Session session = stripeService.retrieveSession(request.getStripeSession());
            if ("complete".equals(session.getStatus())
                    && session.getId().equals(request.getStripeSession())) {
                return OrderEntity.find("stripeSession", request.getStripeSession()).firstResult()
                        .onItem().transformToUni(existingOrder -> {
                            if (existingOrder == null) {
                                return Uni.createFrom().item(collectionItem).onItem()
                                        .transformToUni(items -> {
                                            OrderEntity entity =
                                                    orderMapper.requestToEntity(request, items);
                                            return entity.persist().replaceWith(entity).onItem()
                                                    .transform(orderMapper::entityToOrder);
                                        });
                            } else {
                                throw new IllegalStateException("Order already exists for session: "
                                        + request.getStripeSession());
                            }
                        });
            } else {
                throw new IllegalStateException(
                        "Session is not complete or invalid: " + session.getId());
            }
        } catch (StripeException e) {
            return Uni.createFrom().failure(e);
        }
    }
}
