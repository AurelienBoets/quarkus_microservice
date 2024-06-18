package org.acme.service.grpc;

import java.util.ArrayList;
import java.util.List;

import org.acme.Dto.Order.CreateOrderDto;
import org.acme.Dto.Order.OrderDto;
import org.acme.Dto.Order.OrderItemDto;
import org.acme.utils.VerifyLogin;

import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import order.AddOrder;
import order.Order;
import order.OrderGrpc;
import order.OrderId;
import order.OrderItem;
import order.Payment;
import order.UserId;

@Path("api/order")
public class OrderGrpcService {
    @GrpcClient
    OrderGrpc orderGrpc;

    @Inject
    VerifyLogin verifyLogin;

    @GET
    public Uni<Response> getAll(@Context HttpHeaders headers) {
        return verifyLogin.isLogin(headers)
            .onItem().transformToUni(userId -> {
                if (userId == null) {
                    return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED).build());
                }
                return orderGrpc.getAllOrder(UserId.newBuilder().setId(userId).build()).onItem().transform(o -> {
                    List<OrderDto> orders = new ArrayList<>();
                    for (Order order : o.getOrdersList()) {
                        orders.add(orderGrpcToDto(order));
                    }
                    return Response.ok(orders).build();
                });
            });
    }

    @POST
    @Path("/payment/{sessionId}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> createOrder(CreateOrderDto orderDto,@Context HttpHeaders headers,@PathParam("sessionId") String sessionId) {
        return verifyLogin.isLogin(headers)
            .onItem().transformToUni(userId -> {
                if (userId == null) {
                    return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED).build());
                }
                return orderGrpc.createOrder(AddOrder.newBuilder()
                        .setStripeSession(sessionId)
                        .setTotalAmount(orderDto.getTotalAmount())
                        .addAllOrderItems(orderItemDtoToGrpc(orderDto.getItems()))
                        .setIdUser(userId)
                        .build())
                    .onItem().transform(order -> Response.ok(orderGrpcToDto(order)).build());
            });
    }

    @POST
    @Path("/payment")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> createPayment(CreateOrderDto orderDto,@Context HttpHeaders headers) {
        return verifyLogin.isLogin(headers)
            .onItem().transformToUni(userId -> {
                if (userId == null) {
                    return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED).build());
                }
                return orderGrpc.createPayment(Payment.newBuilder()
                        .setTotalAmount(orderDto.getTotalAmount())
                        .addAllOrderItems(orderItemDtoToGrpc(orderDto.getItems()))
                        .setIdUser(userId)
                        .build())
                    .onItem().transform(url -> Response.ok(url.getUrl()).build());
            });
    }

    @GET
    @Path("/{id}")
    public Uni<Response> getById(@PathParam("id") String id,@Context HttpHeaders headers) {
        return verifyLogin.isLogin(headers)
            .onItem().transformToUni(userId -> {
                if (userId == null) {
                    return Uni.createFrom().item(Response.status(Response.Status.UNAUTHORIZED).build());
                }
                return orderGrpc.getOrder(OrderId.newBuilder().setId(id).build()).onItem().transform(order -> {
                    return Response.ok(orderGrpcToDto(order)).build();
                });
            });
    }

    private OrderDto orderGrpcToDto(Order order) {
        List<OrderItemDto> items = new ArrayList<>();
        for (OrderItem item : order.getItemsList()) {
            items.add(new OrderItemDto(item.getProductId(), item.getProductName(), item.getUnitPrice(), item.getPlatformId(), item.getPlatformName()));
        }
        return new OrderDto(order.getId(), order.getOrderDate(), order.getTotalAmount(),order.getIdUser(),items);
    }

    private List<OrderItem> orderItemDtoToGrpc(List<OrderItemDto> itemsDto) {
        List<OrderItem> items = new ArrayList<>();
        for (OrderItemDto itemDto : itemsDto) {
            items.add(OrderItem.newBuilder()
                    .setProductId(itemDto.getProduct_id())
                    .setProductName(itemDto.getProduct_name())
                    .setUnitPrice(itemDto.getUnitPrice())
                    .setPlatformId(itemDto.getPlatformId())
                    .setPlatformName(itemDto.getPlatformName())
                    .build());
        }
        return items;
    }
}
