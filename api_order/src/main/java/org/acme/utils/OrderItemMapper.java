package org.acme.utils;


import java.util.ArrayList;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import order.OrderItem;

@ApplicationScoped
public class OrderItemMapper {
    
    public List<OrderItem> entityToOrderItems(List<org.acme.entity.OrderItem> entityList){
        List<OrderItem> items=new ArrayList<>();
        for(org.acme.entity.OrderItem entity:entityList ){
            items.add(OrderItem.newBuilder()
                               .setProductName(entity.getProductName())
                               .setProductId(entity.getProductId())
                               .setUnitPrice(entity.getUnitPrice())
                               .setPlatformId(entity.getPlatformId())
                               .setPlatformName(entity.getPlatformName())
                               .build());
        }
        return items;
    } 

    public List<org.acme.entity.OrderItem> orderItemsToEntity(List<OrderItem> items){
        List<org.acme.entity.OrderItem> entityList=new ArrayList<>();
        for(OrderItem item:items ){
            entityList.add(new org.acme.entity.OrderItem(item.getProductId(),item.getProductName(),item.getUnitPrice(),item.getPlatformId(),item.getPlatformName()));
        }
        return entityList;
    }
}
