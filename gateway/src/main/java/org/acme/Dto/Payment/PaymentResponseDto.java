package org.acme.dto.payment;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@Data
@RegisterForReflection
@NoArgsConstructor
public class PaymentResponseDto {
    private String key;
}
