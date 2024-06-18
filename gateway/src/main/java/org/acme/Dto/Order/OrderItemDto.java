package org.acme.Dto.Order;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.*;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@RegisterForReflection
public class OrderItemDto {
    private String product_id;
    private String product_name;
    private double unitPrice;
    private String platformId;
    private String platformName;
}
