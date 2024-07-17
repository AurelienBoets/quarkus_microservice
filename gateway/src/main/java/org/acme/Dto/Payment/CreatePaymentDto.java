package org.acme.dto.payment;

import java.util.List;
import org.acme.dto.order.OrderItemDto;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.*;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@RegisterForReflection
public class CreatePaymentDto {
    private double totalAmount;
    private Long idUser;
    private List<OrderItemDto> items;
}
