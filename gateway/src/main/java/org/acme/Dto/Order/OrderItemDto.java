package org.acme.dto.order;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.*;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@RegisterForReflection
public class OrderItemDto {
    private String productId;
    private String productName;
    private double unitPrice;
    private String platformId;
    private String platformName;
}
