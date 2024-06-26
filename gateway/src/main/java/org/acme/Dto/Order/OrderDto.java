package org.acme.dto.order;

import java.util.List;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.*;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@RegisterForReflection
public class OrderDto {
    private String id;
    private String orderDate;
    private double totalAmount;
    private Long idUser;
    private List<OrderItemDto> items;
}
