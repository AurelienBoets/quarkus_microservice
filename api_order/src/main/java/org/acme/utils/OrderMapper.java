package org.acme.utils;

import java.time.LocalDate;
import java.util.List;
import org.acme.entity.OrderEntity;
import org.acme.entity.OrderItem;
import com.stripe.model.LineItemCollection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import order.AddOrder;
import order.Order;

@ApplicationScoped
public class OrderMapper {

    private final OrderItemMapper itemMapper;

    @Inject
    public OrderMapper(OrderItemMapper itemMapper) {
        this.itemMapper = itemMapper;
    }

    public Order entityToOrder(OrderEntity entity) {
        return Order.newBuilder().setId(entity.id.toString())
                .setTotalAmount(entity.getTotalAmount()).setIdUser(entity.getIdUser())
                .setOrderDate(entity.getOrderDate())
                .addAllItems(itemMapper.entityToOrderItems(entity.getItems())).build();
    }

    public OrderEntity requestToEntity(AddOrder request, LineItemCollection lineItemCollection) {
        List<OrderItem> items = itemMapper.lineItemsToOrderItems(lineItemCollection);
        double total = 0;
        for (OrderItem item : items) {
            total += item.getUnitPrice();
        }
        return OrderEntity.builder().idUser(request.getIdUser()).totalAmount(total)
                .orderDate(LocalDate.now().toString()).stripeSession(request.getStripeSession())
                .items(items).build();
    }
}
