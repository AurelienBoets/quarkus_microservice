package org.acme.service;


import org.eclipse.microprofile.config.inject.ConfigProperty;
import com.stripe.Stripe;
import com.stripe.model.Address;
import com.stripe.model.LineItemCollection;
import com.stripe.model.StripeError;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionListLineItemsParams;
import com.stripe.param.checkout.SessionRetrieveParams;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionCreateParams.LineItem;
import io.quarkus.grpc.GrpcService;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import order.AddOrder;
import order.ListOfOrder;
import order.Order;
import order.OrderGrpc;
import order.OrderId;
import order.Payment;
import order.URL;
import order.UserId;

@GrpcService
@RegisterForReflection(targets = {StripeError.class, Session.class, SessionCreateParams.class,
        Session.class, Stripe.class, Address.class, LineItem.class, LineItemCollection.class,
        SessionListLineItemsParams.class, SessionRetrieveParams.class})
public class OrderGrpcService implements OrderGrpc {

    @ConfigProperty(name = "client.url")
    String clientUrl;

    private final OrderService orderService;
    private final StripeService stripeService;

    @Inject
    public OrderGrpcService(OrderService orderService, StripeService stripeService) {
        this.orderService = orderService;
        this.stripeService = stripeService;
    }

    @Override
    public Uni<Order> getOrder(OrderId request) {
        return orderService.getOrderById(request.getId());
    }

    @Override
    public Uni<Order> createOrder(AddOrder request) {
        return orderService.createOrder(request);
    }

    @Override
    public Uni<ListOfOrder> getAllOrder(UserId request) {
        return orderService.getAllOrdersByUserId(request.getId()).onItem().transform(orders -> {
            ListOfOrder.Builder builder = ListOfOrder.newBuilder();
            builder.addAllOrders(orders);
            return builder.build();
        });
    }

    @Override
    public Uni<URL> createPayment(Payment request) {
        Session session = stripeService.createSession(request, clientUrl);
        if (session != null) {
            return Uni.createFrom().item(session).onItem()
                    .transform(s -> URL.newBuilder().setUrl(s.getUrl()).build());
        }
        return null;
    }
}
