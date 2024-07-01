package org.acme.utils;


import java.util.ArrayList;
import java.util.List;
import com.stripe.model.LineItemCollection;
import jakarta.enterprise.context.ApplicationScoped;
import order.OrderItem;

@ApplicationScoped
public class OrderItemMapper {

    public List<OrderItem> entityToOrderItems(List<org.acme.entity.OrderItem> entityList) {
        List<OrderItem> items = new ArrayList<>();
        for (org.acme.entity.OrderItem entity : entityList) {
            items.add(OrderItem.newBuilder().setProductName(entity.getProductName())
                    .setProductId(entity.getProductId()).setUnitPrice(entity.getUnitPrice())
                    .setPlatformId(entity.getPlatformId()).setPlatformName(entity.getPlatformName())
                    .build());
        }
        return items;
    }

    public List<org.acme.entity.OrderItem> lineItemsToOrderItems(LineItemCollection lineItems) {
        return lineItems.getData().stream().map(item -> {
            String[] descriptionParts = item.getDescription().split(" sur ");
            String productName = descriptionParts[0];
            String platformName = descriptionParts[1];
            return org.acme.entity.OrderItem.builder()
                    .productId(item.getPrice().getProductObject().getMetadata().get("app_id"))
                    .platformId(item.getPrice().getProductObject().getMetadata().get("platformId"))
                    .productName(productName).platformName(platformName)
                    .unitPrice(item.getPrice().getUnitAmount() / 100d).build();
        }).toList();
    }

}
