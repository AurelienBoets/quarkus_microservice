package org.acme.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.logging.Logger;

import org.acme.entity.OrderEntity;
import org.acme.utils.OrderMapper;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.StripeError;
import com.stripe.model.Address;
import com.stripe.model.LineItem;
import com.stripe.model.LineItemCollection;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData;
import com.stripe.param.checkout.SessionListLineItemsParams;
import io.quarkus.grpc.GrpcService;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import order.AddOrder;
import order.ListOfOrder;
import order.Order;
import order.OrderGrpc;
import order.OrderId;
import order.OrderItem;
import order.Payment;
import order.URL;
import order.UserId;

@GrpcService
@RegisterForReflection(targets = {StripeError.class, Session.class, SessionCreateParams.class,
        Session.class, Stripe.class, Address.class, LineItem.class})
public class OrderGrpcService implements OrderGrpc {

    @ConfigProperty(name = "client.url")
    String clientUrl;

    private final OrderMapper orderMapper;

    @Inject
    public OrderGrpcService(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    @Override
    public Uni<Order> getOrder(OrderId request) {
        return OrderEntity.findById(new ObjectId(request.getId())).onItem().transform(o -> {
            OrderEntity entity = (OrderEntity) o;
            return orderMapper.entityToOrder(entity);
        });
    }

    @Override
    public Uni<Order> createOrder(AddOrder request) {
        return Uni.createFrom().item(request.getStripeSession()).onItem()
                .transformToUni(this::retrieveSessionFromStripe).onItem()
                .transformToUni(session -> checkAndPersistOrder(session, request));
    }

    @Override
    public Uni<ListOfOrder> getAllOrder(UserId request) {
        return OrderEntity.find("id_user = ?1", request.getId()).list().onItem().transform(list -> {
            List<Order> orders = list.stream().map(o -> {
                OrderEntity entity = (OrderEntity) o;
                return orderMapper.entityToOrder(entity);
            }).toList();
            return ListOfOrder.newBuilder().addAllOrders(orders).build();
        });
    }

    @Override
    public Uni<URL> createPayment(Payment request) {
        Stripe.apiKey =
                "sk_test_51LXT08E7l5c5NjS32ioBp2okfcM39LAR2Tz5nj6n1ZIPRVl3FPmp2eReZj1FmSdWPdbptI1glR84jtomC5EWaOn600MLGICwza";
        SessionCreateParams.Builder paramsBuilder =
                SessionCreateParams.builder().setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl(clientUrl + "/success?s={CHECKOUT_SESSION_ID}")
                        .setCancelUrl(clientUrl + "/failure");

        for (OrderItem item : request.getOrderItemsList()) {
            BigDecimal unitPrice = BigDecimal.valueOf(item.getUnitPrice() * 100);
            paramsBuilder.addLineItem(SessionCreateParams.LineItem.builder().setQuantity(1L)
                    .setPriceData(PriceData.builder()
                            .setProductData(PriceData.ProductData.builder()
                                    .putMetadata("app_id", item.getProductId())
                                    .setName(item.getProductName() + " sur "
                                            + item.getPlatformName())
                                    .build())
                            .setCurrency("eur")
                            .setUnitAmountDecimal(unitPrice.setScale(0, RoundingMode.HALF_UP))
                            .build())
                    .build());
        }

        return Uni.createFrom().item(() -> {
            try {
                Session session = Session.create(paramsBuilder.build());
                return URL.newBuilder().setUrl(session.getUrl()).build();
            } catch (StripeException e) {
                Logger logger = Logger.getLogger(getClass().getName());
                logger.warning("Failed to create Stripe session: " + e.getMessage());
                return null;
            }
        }).onFailure().recoverWithUni(
                Uni.createFrom().failure(new RuntimeException("Failed to create Stripe session")));
    }

    private Uni<Session> retrieveSessionFromStripe(String sessionId) {
        try {
            return Uni.createFrom().item(Session.retrieve(sessionId));
        } catch (StripeException e) {
            return Uni.createFrom().failure(e);
        }
    }

    private Uni<LineItemCollection> retrieveItemFromSessionStripe(Session session) {
        try {
            SessionListLineItemsParams params = SessionListLineItemsParams.builder().build();
            return Uni.createFrom().item(session.listLineItems(params));
        } catch (Exception e) {
            return Uni.createFrom().failure(e);
        }
    }

    private Uni<Order> checkAndPersistOrder(Session session, AddOrder request) {
        if (session.getStatus().equals("complete")
                && session.getId().equals(request.getStripeSession())) {
            return OrderEntity.find("stripeSession", request.getStripeSession()).firstResult()
                    .onItem().transformToUni(existingOrder -> {
                        if (existingOrder == null) {
                            Uni<LineItemCollection> lineItemsUni =
                                    retrieveItemFromSessionStripe(session);
                            return lineItemsUni.onItem().transformToUni(items -> {
                                OrderEntity entity = orderMapper.requestToEntity(request, items);
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
    }
}
