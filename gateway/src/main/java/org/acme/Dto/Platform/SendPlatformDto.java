package org.acme.Dto.Platform;


import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@RegisterForReflection
public class SendPlatformDto {
    private String id;
    private double price;
}

