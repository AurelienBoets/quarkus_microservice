package org.acme.entity;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@RegisterForReflection
public class OrderItem {
    private String productId;
    private String productName;
    private double unitPrice;
    private String platformId;
    private String platformName;
}
