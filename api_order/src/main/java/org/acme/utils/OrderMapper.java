package org.acme.utils;

import java.time.LocalDate;

import org.acme.entity.OrderEntity;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import order.AddOrder;
import order.Order;

@ApplicationScoped
public class OrderMapper {
    @Inject
    OrderItemMapper itemMapper;

    public Order entityToOrder(OrderEntity entity){
        return Order.newBuilder()
                    .setId(entity.getId().toString())
                    .setTotalAmount(entity.getTotalAmount())
                    .setIdUser(entity.getId_user())
                    .setOrderDate(entity.getOrderDate())
                    .addAllItems(itemMapper.entityToOrderItems(entity.getItems()))
                    .build();
    }

    public OrderEntity requestToEntity(AddOrder request){
        return OrderEntity.builder()
                          .id_user(request.getIdUser())
                          .stripeSession(request.getStripeSession())
                          .orderDate(LocalDate.now().toString())
                          .totalAmount(request.getTotalAmount())
                          .items(itemMapper.orderItemsToEntity(request.getOrderItemsList()))
                          .build();
    }
}
