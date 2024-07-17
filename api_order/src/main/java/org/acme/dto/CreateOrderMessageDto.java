package org.acme.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@RegisterForReflection
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CreateOrderMessageDto {
    private Long idUser;
    private String sessionId;
}
