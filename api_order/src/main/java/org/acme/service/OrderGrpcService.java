package org.acme.service;

import java.math.BigDecimal;
import java.util.List;

import org.acme.entity.OrderEntity;
import org.acme.repository.OrderRepository;
import org.acme.utils.OrderMapper;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.StripeError;
import com.stripe.model.Address;
import com.stripe.model.LineItem;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.LineItem.PriceData;

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
@RegisterForReflection(targets = {StripeError.class,Session.class,SessionCreateParams.class,Session.class,Stripe.class,Address.class,LineItem.class})
public class OrderGrpcService implements OrderGrpc {

    @Inject
    OrderRepository repository;

    @Inject
    OrderMapper orderMapper;

    @ConfigProperty(name = "client.url")
    String clientUrl;

    @Override
    public Uni<Order> getOrder(OrderId request) {
        return OrderEntity.findById(new ObjectId(request.getId())).onItem().transform(o -> {
            OrderEntity entity = (OrderEntity) o;
            return orderMapper.entityToOrder(entity);
        });
    }

    @Override
    public Uni<Order> createOrder(AddOrder request) {
        OrderEntity entity = orderMapper.requestToEntity(request);
        try {
            Session session=Session.retrieve(request.getStripeSession());
            if( session.getStatus().equals("complete") && session.getId().equals(request.getStripeSession()))
            {
                OrderEntity orderBySession=(OrderEntity) repository.find("stripeSession=?1",request.getStripeSession()).firstResult();
                System.out.println(orderBySession);
                if(orderBySession==null){

                    return entity.persist().replaceWith(entity).onItem().transform(o -> {
                        return orderMapper.entityToOrder(entity);
                    });
                }
            }
        } catch (StripeException e) {
            e.printStackTrace();
        }
        return Uni.createFrom().item(()->null);
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
    Stripe.apiKey = "sk_test_51LXT08E7l5c5NjS32ioBp2okfcM39LAR2Tz5nj6n1ZIPRVl3FPmp2eReZj1FmSdWPdbptI1glR84jtomC5EWaOn600MLGICwza";
    SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder()
            .setMode(SessionCreateParams.Mode.PAYMENT)
            .setSuccessUrl(clientUrl+"/success/{CHECKOUT_SESSION_ID}" )
            .setCancelUrl(clientUrl + "/failure");

    for (OrderItem item : request.getOrderItemsList()) {
        paramsBuilder.addLineItem(
                SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(
                                PriceData.builder()
                                        .setProductData(PriceData.ProductData.builder()
                                                .putMetadata("app_id", item.getProductId())
                                                .setName(item.getProductName() + " sur " + item.getPlatformName())
                                                .build())
                                        .setCurrency("eur")
                                        .setUnitAmountDecimal(BigDecimal.valueOf(item.getUnitPrice()*100))
                                        .build())
                        .build());
    }

    return Uni.createFrom().item(() -> {
        try {
            Session session = Session.create(paramsBuilder.build());
            return URL.newBuilder().setUrl(session.getUrl()).build();
        } catch (StripeException e) {
            System.err.println("Failed to create Stripe session: " + e.getMessage());
            throw new RuntimeException("Failed to create Stripe session", e);
        }
    }).onFailure().recoverWithUni(Uni.createFrom().failure(new RuntimeException("Failed to create Stripe session")));
}
}
